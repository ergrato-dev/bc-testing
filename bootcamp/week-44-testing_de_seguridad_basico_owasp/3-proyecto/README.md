# Proyecto - Seguridad de la API del dominio

## Objetivo

Añadir a tu repositorio `qa-<nombre>-<apellido>` los tests de seguridad de la API Go de tu dominio (cabeceras, acceso sin token, IDOR, inyección y manipulación de parámetros), corregir lo que encuentren, y la carpeta `security/` con un escaneo baseline de ZAP, la auditoría de dependencias de cada lenguaje del repositorio y un informe.

## Tiempo estimado

3 horas.

## Punto de partida

La API Go de las semanas [34](../../week-34-http_bd_e_integracion_en_go/3-proyecto/README.md) y [43](../../week-43-testing_de_performance_con_k6_y_locust/3-proyecto/README.md): `NewHandler`, `pgstore` y el servidor `go/cmd/server`. No tiene autenticación: cualquiera que llegue a la API pide préstamos, y a nombre de la institución que quiera.

| Archivo | Qué es | Qué haces |
|---|---|---|
| `go/museum/security_test.go` | El ayudante `send` (una petición con la cabecera `Authorization` que elijas), la lista `securityHeaders` y los tests por escribir | Escribes los tests en tu paquete de dominio (pasos 2 a 5) |
| `security/zap.sh` | Arranca PostgreSQL, crea de cero la base de datos `museum_sec`, arranca la API con un token, carga `perf/seed.sql` y ejecuta ZAP baseline | Nada, salvo `TARGET` si tu ruta de lectura no es `/api/pieces/AAA-000` |
| `security/zap.conf` | Las reglas de ZAP que se ignoran, con su motivo | Lo completas (paso 7) |
| `security/audit.sh` | Ejecuta el auditor de cada lenguaje y guarda su salida en `security/results/` | Lo adaptas a las carpetas de tu repositorio (paso 8) |
| `security/security-report.md` | La plantilla del informe | La completas (paso 9) |
| `security/.gitignore` | Excluye el binario y el log de la API | Nada |

El ejemplo es el museo: rutas `GET /api/pieces/{code}` y `POST /api/pieces/{code}/loan`, préstamos a instituciones. Cambia rutas, cuerpos y fakes por los de tu dominio.

## Paso a paso

### Paso 1: la rama y las herramientas

1. Crea la rama:

   ```bash
   git switch -c week-44
   ```

2. Copia `starter/go/museum/security_test.go` a la carpeta de tu paquete de dominio (cambia `package museum` por el tuyo) y `starter/security/` a la raíz del repositorio.
3. Las herramientas:

   ```bash
   docker pull zaproxy/zap-stable:2.17.0
   docker pull ghcr.io/google/osv-scanner:v2.7.1
   cargo install --locked cargo-audit@0.23.0
   cd go && go get -tool golang.org/x/vuln/cmd/govulncheck@v1.10.0 && cd ..
   # go: added golang.org/x/vuln v1.10.0
   ```

   `go get -tool` registra `govulncheck` en tu `go.mod`, como `staticcheck` en la [semana 35](../../week-35-concurrencia_y_calidad_en_go/README.md): se ejecuta con `go tool govulncheck` y todo el equipo usa la misma versión.

Cada test de esta semana se llama `TestSecurity...`. Así se ejecutan todos juntos:

```bash
cd go
go test -run '^TestSecurity' ./...
```

### Paso 2: cabeceras de seguridad

Escribe `TestSecurityHeaders`: un test de tabla con una petición de cada tipo y, en cada respuesta, las cabeceras de `securityHeaders`. Como en el [lab 03](../2-practicas/lab-03-cabeceras-y-zap-baseline/README.md), incluye los errores:

```go
requests := []struct{ method, target, auth string }{
	{http.MethodGet, "/api/pieces/CER-001", ""},                 // 200
	{http.MethodGet, "/api/pieces/CER-999", ""},                 // 404 del dominio
	{http.MethodPost, "/api/pieces/CER-001/loan", ""},           // 401 (paso 3)
	{http.MethodGet, "/api/pieces/CER-001/loan", "Bearer mali"}, // 405 del ServeMux
	{http.MethodGet, "/no-existe", ""},                          // 404 del ServeMux
}
```

```bash
go test -run '^TestSecurityHeaders' ./museum
# --- FAIL: TestSecurityHeaders (0.00s)
#     --- FAIL: TestSecurityHeaders/GET_/api/pieces/CER-001 (0.00s)
#         security_test.go:54: X-Content-Type-Options = "", want "nosniff"
#         security_test.go:54: Content-Security-Policy = "", want "default-src 'none'; frame-ancestors 'none'"
#         security_test.go:54: Cache-Control = "", want "no-store"
#         security_test.go:54: Referrer-Policy = "", want "no-referrer"
#         security_test.go:54: Cross-Origin-Resource-Policy = "", want "same-origin"
# ...
```

Corrige con el *middleware* `secureHeaders` del lab 03, con las cinco cabeceras de tu lista, y envuelve el `ServeMux` en la última línea de `NewHandler` (`return secureHeaders(mux)`), no en `main`.

### Paso 3: sin token, 401

La API necesita saber quién pide el préstamo. Cada institución tendrá un token; `NewHandler` recibe el mapa de token a institución:

```go
func NewHandler(s *LendingService, tokens map[string]string) http.Handler {
```

El cambio de firma rompe la compilación de los tests que llaman a `NewHandler(s)`. Arréglalos antes de seguir:

1. En `api_test.go`, los tokens de las pruebas y el ayudante `serve`, que ahora manda siempre el de MALI:

   ```go
   // testTokens son los tokens de las pruebas: cada uno identifica a una institución.
   var testTokens = map[string]string{"mali": "MALI", "mnba": "MNBA", "moma": "MOMA"}

   // serve envía la petición al handler con el token de MALI y devuelve la respuesta grabada.
   func serve(t *testing.T, h http.Handler, method, target, body string) *http.Response {
   	t.Helper()
   	return send(t, h, method, target, "Bearer mali", body)
   }
   ```

2. Cambia `NewHandler(s)` por `NewHandler(s, testTokens)` en todos los tests. El test que usa un servidor real (`httptest.NewServer`, el de la concurrencia de la semana 35) crea la petición con `http.NewRequest` y le añade la cabecera `Authorization: Bearer mali`.

Escribe `TestSecurityLendRequiresToken` con cuatro casos (sin cabecera, `Bearer not-a-token`, el esquema `Basic` y `Bearer ` vacío). Cada uno comprueba el 401, la cabecera `WWW-Authenticate: Bearer` y que el almacén no cambió (`cmp.Diff` con las piezas de antes):

```bash
go test -run '^TestSecurityLendRequiresToken' ./museum
# --- FAIL: TestSecurityLendRequiresToken (0.00s)
#     --- FAIL: TestSecurityLendRequiresToken/no_header (0.00s)
#         security_test.go:78: status = 422, want 401
#         security_test.go:81: WWW-Authenticate = "", want "Bearer"
# ...
```

Un 422: la petición sin token llegó hasta el dominio y lo rechazó porque la institución vacía no está acreditada. Rechazado, pero por casualidad. Corrige el handler del préstamo: lo primero, el token.

```go
institution, ok := tokens[bearerToken(r)]
if !ok {
	w.Header().Set("WWW-Authenticate", "Bearer")
	writeError(w, http.StatusUnauthorized, errUnauthorized)
	return
}
```

```go
var (
	errUnauthorized     = errors.New("missing or invalid token")
	errOtherInstitution = errors.New("cannot lend on behalf of another institution")
)

// bearerToken devuelve el token de la cabecera Authorization: Bearer <token>, o "".
func bearerToken(r *http.Request) string {
	token, _ := strings.CutPrefix(r.Header.Get("Authorization"), "Bearer ")
	return token
}
```

El servidor (`go/cmd/server/main.go`) lee los tokens de una variable de entorno, `API_TOKENS="token1:MALI,token2:MNBA"`:

```go
// parseTokens lee "token1:MALI,token2:MNBA" como un mapa de token a institución.
func parseTokens(v string) map[string]string {
	tokens := map[string]string{}
	for pair := range strings.SplitSeq(v, ",") {
		token, institution, ok := strings.Cut(pair, ":")
		if ok && token != "" && institution != "" {
			tokens[token] = institution
		}
	}
	return tokens
}
```

y lo pasa: `museum.NewHandler(service, parseTokens(os.Getenv("API_TOKENS")))`. Añade un `main_test.go` en `cmd/server` con un test de `parseTokens` (pares válidos, uno sin `:`, uno con el token o la institución vacíos).

Las pruebas de rendimiento de la semana 43 también piden préstamos. Sin token, recibirían 401:

- `perf/run.sh`, después de `export DATABASE_URL=...`: `export API_TOKENS="${API_TOKENS:-perf-token:MALI}"`.
- `perf/k6/load.js`, en las cabeceras del préstamo: `Authorization: 'Bearer perf-token'`.

### Paso 4: el IDOR al escribir

Con el token de MALI y `"institutionId": "MNBA"` en el cuerpo, la API presta la pieza a MNBA: MALI actúa en nombre de otra institución. Escribe dos tests:

- `TestSecurityLendOnBehalfOfAnotherInstitution`: MNBA acreditada en el registro (`s.Registry = stubRegistry{"MALI": true, "MNBA": true}`), token de MALI, cuerpo con MNBA. Quiere 403 y la pieza sigue disponible.
- `TestSecurityLoanBelongsToTokenInstitution`: token de MALI y cuerpo sin institución (`{"days":30}`). Quiere 200 y un préstamo a nombre de MALI en el fake.

La corrección: la institución del préstamo es la del token. El campo del cuerpo se mantiene por compatibilidad, pero si viene tiene que coincidir:

```go
if req.InstitutionID != "" && req.InstitutionID != institution {
	writeError(w, http.StatusForbidden, errOtherInstitution)
	return
}
p, err := s.Lend(r.Context(), r.PathValue("code"), institution, req.Days)
```

Un test antiguo cae con la corrección:

```bash
go test ./museum
# --- FAIL: TestLendEndpointRejects (0.00s)
#     --- FAIL: TestLendEndpointRejects/institution_not_accredited (0.00s)
#         api_test.go:90: status = 403, want 422
```

El caso "institución no acreditada" mandaba MOMA en el cuerpo con el token de MALI: ahora es una suplantación (403). Para probar de verdad una institución no acreditada, la petición tiene que llevar el token de MOMA. Añade a la tabla un campo `token` (vacío: el de MALI) y en esa fila usa `"moma"` con el cuerpo `{"days":30}`.

### Paso 5: inyección y manipulación de parámetros

Tres tests más.

**`TestSecurityPieceCodeInjection`**: códigos con SQL (`' OR '1'='1`, `CER-001' --`, `CER-001'; DROP TABLE pieces; --`), una ruta de archivo (`../../etc/passwd`), HTML y un código de 10 000 caracteres, codificados con `url.PathEscape`. Todos 404, nunca 500. Pasa a la primera: el handler no hace nada peligroso con el código. Pero el fake no ejecuta SQL; la inyección se prueba contra PostgreSQL en `pgstore`, en un `security_test.go` con la etiqueta `integration`:

```go
//go:build integration

func TestSecuritySQLInjectionInCode(t *testing.T) {
	payloads := []string{
		`' OR '1'='1`,
		`CER-001' --`,
		`CER-001'; DROP TABLE pieces; --`,
		`' UNION SELECT code, name, declared_value, status FROM pieces --`,
	}
	// por cada carga: guardar una pieza, FindByCode(carga) devuelve ErrUnknownPiece
	// y la pieza sigue ahí
}
```

Y `TestSecuritySQLInjectionInName`: una pieza con el nombre `Vasija'); DROP TABLE pieces; --` se guarda y se lee igual. Los dos pasan, porque `pgstore` usa parámetros desde la semana 34. Comprueba que detectan el fallo: cambia temporalmente la consulta de `FindByCode` por una concatenada (`WHERE code = '` + code + `'`):

```bash
go test -tags integration ./pgstore -run 'SecuritySQLInjectionInCode'
# --- FAIL: TestSecuritySQLInjectionInCode (0.01s)
#     --- FAIL: TestSecuritySQLInjectionInCode/'_OR_'1'='1 (0.00s)
#         security_test.go:31: FindByCode("' OR '1'='1"): err = <nil>, want ErrUnknownPiece
#     --- FAIL: TestSecuritySQLInjectionInCode/CER-001'_-- (0.00s)
#         security_test.go:31: FindByCode("CER-001' --"): err = <nil>, want ErrUnknownPiece
#     --- FAIL: TestSecuritySQLInjectionInCode/CER-001';_DROP_TABLE_pieces;_-- (0.00s)
#         security_test.go:31: FindByCode("CER-001'; DROP TABLE pieces; --"): err = ERROR: cannot insert multiple commands into a prepared statement (SQLSTATE 42601), want ErrUnknownPiece
#         security_test.go:34: the piece is gone after the payload: ERROR: current transaction is aborted, commands ignored until end of transaction block (SQLSTATE 25P02)
#     --- FAIL: TestSecuritySQLInjectionInCode/'_UNION_SELECT_code,_name,_declared_value,_status_FROM_pieces_-- (0.00s)
#         security_test.go:31: FindByCode("' UNION SELECT code, name, declared_value, status FROM pieces --"): err = <nil>, want ErrUnknownPiece
# FAIL
```

Deshaz el cambio.

**`TestSecurityLendBodyTampering`**: valores que un cliente normal no manda, con el token de MALI. Cada fila quiere su código y que nada cambie:

| Cuerpo | Quiere |
|---|---|
| `{"days":9223372036854775807}` | 422 |
| `{"days":36500}` | 422 |
| `{"days":-30}` | 422 |
| `{"days":"30"}` | 400 |
| `{"days":1e30}` | 400 |

```bash
go test -run '^TestSecurityLendBodyTampering' ./museum
# --- FAIL: TestSecurityLendBodyTampering (0.00s)
#     --- FAIL: TestSecurityLendBodyTampering/days_overflow (0.00s)
#         lending.go:142: Unexpected call to *museum.MockNotifier.Send([{MALI CER-001 9223372036854775807 100 2026-03-09 09:00:00 +0000 UTC}]) ...
```

Un hallazgo del museo, A06 (diseño inseguro) y A10 (condiciones excepcionales): con 9 223 372 036 854 775 807 días (el máximo de `int64`), `valor × días` desborda y el seguro queda en el mínimo, 100; la fecha de vencimiento también desborda y cae el 9 de marzo de 2026, el día antes del préstamo. Se presta para siempre por el seguro mínimo. Con 36 500 días (un siglo) no hay desbordamiento, pero tampoco tiene sentido. Antes de este paso, este test pasaba: la petición no llevaba institución y el dominio la rechazaba con 422 por no acreditada. Por eso se revisa por qué pasa un test, no solo que pasa.

La corrección es una regla de negocio que faltaba: un préstamo dura como mucho 365 días. En `fees.go`, una constante `MaxLoanDays = 365`, un error `ErrTooManyDays` que `LoanPremium` devuelve cuando `days > MaxLoanDays`, y en `statusFor` el 422 también para `ErrTooManyDays`.

**`TestSecurityLendIgnoresPieceFields`** (asignación masiva): `{"days":30,"declaredValue":1,"status":"AVAILABLE","code":"ORO-007"}` presta la pieza y no cambia su valor ni su código. Pasa a la primera: el handler decodifica en un `struct` con solo `InstitutionID` y `Days`. Se queda como guardia.

```bash
go test -run '^TestSecurity' -v ./museum | grep -c -- '--- PASS'
# 27
```

### Paso 6: `verify.sh`

```bash
cd go
bash verify.sh
# ...
# total: 75.9 % (mínimo: 85 %)
# FALLO: la cobertura no llega al 85 %
```

La cobertura baja por algo de la semana 43: `-coverpkg=./...` incluye `cmd/server`, que no tiene tests de su `main`. Mide la cobertura de los paquetes con lógica:

```bash
go test ... -coverpkg=./museum,./pgstore -coverprofile=cover.out ./...
```

(cambia solo el valor de `-coverpkg` en la línea de `verify.sh`, con tus paquetes). `parseTokens` tiene su test en `cmd/server/main_test.go`, que se ejecuta igual.

```bash
bash verify.sh 2>&1 | tail -2
# total: 92.3 % (mínimo: 85 %)
```

### Paso 7: ZAP baseline

```bash
bash security/zap.sh
echo $?
```

`zap.sh` arranca todo desde cero y ejecuta ZAP desde `TARGET` (por defecto `/api/pieces/AAA-000`, una pieza de `perf/seed.sql`). Una API JSON no tiene enlaces que seguir: ZAP solo ve esa ruta, `/`, `robots.txt` y `sitemap.xml`. Contra el código anterior al paso 2, el museo daba tres avisos:

```text
WARN-NEW: X-Content-Type-Options Header Missing [10021] x 1
WARN-NEW: Storable and Cacheable Content [10049] x 2
WARN-NEW: Cross-Origin-Resource-Policy Header Missing or Invalid [90004] x 1
FAIL-NEW: 0	FAIL-INPROG: 0	WARN-NEW: 3	WARN-INPROG: 0	INFO: 0	IGNORE: 0	PASS: 65
```

Con las cabeceras queda la regla informativa 10049 (`Non-Storable Content`), como en el lab 03. Añádela a `security/zap.conf` con su motivo en un comentario:

```text
# Informativa: avisa de que la respuesta no se guarda en caché, que es lo que pide Cache-Control: no-store.
10049	IGNORE	(Non-Storable Content)
```

```bash
bash security/zap.sh > /dev/null 2>&1; echo $?
# 0
grep -E "^(IGNORE|FAIL-NEW)" security/results/zap-baseline.txt
# IGNORE: Non-Storable Content [10049] x 3
# FAIL-NEW: 0	FAIL-INPROG: 0	WARN-NEW: 0	WARN-INPROG: 0	INFO: 0	IGNORE: 1	PASS: 67
```

Solo se ignora una regla con un motivo que puedas defender. Un aviso de cabecera que falta se corrige.

### Paso 8: auditoría de dependencias

Abre `security/audit.sh`. Cada línea `audit` es una auditoría: nombre, carpeta y comando. Ajusta las carpetas a tu repositorio; las del museo son `go`, `python` (semana 24), `perf/locust` (semana 43), `java`, `rust` y `e2e` (semana 42). Si `e2e/` no tiene `pnpm-lock.yaml`, créalo con `pnpm install` en esa carpeta: `pnpm audit` lo necesita.

```bash
bash security/audit.sh
```

Al final imprime el código de salida de cada una. En el museo, en noviembre de 2026:

```text
| Auditoría | Código de salida |
|---|---:|
| go | 3 |
| python | 0 |
| locust | 0 |
| java | 1 |
| rust | 0 |
| e2e | 0 |
```

Lee `security/results/audit-go.txt` y `audit-java.txt`:

- **Go**: vulnerabilidades alcanzables de la biblioteca estándar de Go 1.26.5. Por ejemplo, `GO-2026-6859` (*HTTP/1 server crash due to MaxBytesReader header race in net/http*), con la traza `cmd/server/main.go:77:31: server.main calls http.ListenAndServe`. Se corrigen con `go get toolchain@go1.26.11` en `go/` (la [teoría 04](../1-teoria/04-cabeceras-zap-y-dependencias.md#go-govulncheck-y-la-alcanzabilidad) lo explica).
- **Java**: 11 vulnerabilidades en `tomcat-embed-core` 11.0.24 y Jackson 3.1.5, que llegan con Spring Boot 4.1.1. Se corrigen con Spring Boot 4.1.2 en el `<parent>` del `pom.xml`; comprueba después que `mvn verify` sigue pasando.

Tus resultados pueden ser otros: la base de datos de vulnerabilidades cambia cada día. Corrige lo que encuentres, vuelve a ejecutar `audit.sh` y guarda la salida final. Si algo no se puede corregir todavía, ignóralo en el comando de su auditor con el identificador del aviso y explica el motivo en el informe.

### Paso 9: el informe

Completa `security/security-report.md`. Cada sección se apoya en los resultados guardados:

1. **Alcance y entorno**: qué se probó y qué no; versiones de ZAP y de cada auditor.
2. **Tests de seguridad**: una fila por test, con su categoría del OWASP Top 10 2025 y si fue rojo o verde contra el código de antes (y por qué).
3. **Hallazgos y correcciones**: cada fallo con la petición que lo demuestra, la corrección y el test que lo vigila.
4. **ZAP baseline**: antes y después, y cada regla ignorada con su motivo.
5. **Auditoría de dependencias**: una fila por auditoría.
6. **Riesgos y lo que no se probó**.

### Paso 10: comprobar y entregar

```bash
PROYECTO=~/qa-<nombre>-<apellido> ./scripts/verificar-semana.sh 44
```

Sube `security/results/` (los informes son la evidencia) y abre el pull request de `week-44` a `main`.

## ✅ Requisitos verificables

Las comprobaciones de [checks.json](../checks.json):

| Comprobación | Qué verifica |
|---|---|
| `tests-seguridad` | Los tests `TestSecurity...` pasan, con los de integración contra PostgreSQL, y son al menos 5 |
| `riesgos-cubiertos` | Los tests de seguridad cubren 401, 403, cabeceras e inyección SQL |
| `verify` | `go/verify.sh` pasa: vet, staticcheck, tests y cobertura mínima con la autenticación nueva |
| `zap` | El informe de ZAP baseline está en `security/results/` y no tiene avisos de riesgo bajo o mayor |
| `auditorias` | Hay una auditoría de dependencias por lenguaje en `security/results/` (go, python, java, rust, e2e) |
| `informe` | El informe de seguridad tiene sus seis secciones |
| `sin-todo` | No queda ningún TODO en `security/` ni en los tests de seguridad |

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 04 - Auditoría de dependencias](../2-practicas/lab-04-auditoria-de-dependencias/README.md) | [Rúbrica de evaluación](../rubrica-evaluacion.md) → |

[↑ Semana 44](../README.md)
