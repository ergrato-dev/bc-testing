# Lab 02 - Los datos del reporte

## Objetivo

Sacar de las salidas de `go test` los datos del reporte de testing: cuántos tests tiene cada fase y cuánto tarda, cuáles son los más lentos, dónde se va el tiempo que no es de ningún test, y la cobertura de cada paquete. Con ellos, escribir `testing-report-go.md`.

## Tiempo estimado

75 minutos.

## Preparación

El starter es la biblioteca del [lab 01](../lab-01-una-suite-un-comando/README.md) con `verify.sh` terminado y la plantilla `testing-report-go.md`. Con Docker en marcha:

```bash
cd starter
./verify.sh
# ...
# == umbral de cobertura
# total: 92.2 % (mínimo: 85 %)
```

`verify.sh` deja `cover.out`, que usa el Paso 4.

## Paso a paso

### Paso 1: el tiempo de cada fase

`time` delante de un comando mide cuánto tarda; la línea que interesa es `real`, el tiempo de reloj.

```bash
time go test -count=1 -race -shuffle=on ./...
# ok  	bootcamp/lab02/catalog	1.032s
# ?   	bootcamp/lab02/pgstore	[no test files]
# real	0m1.472s

time go test -count=1 -race -shuffle=on -tags integration ./...
# ok  	bootcamp/lab02/catalog	1.016s
# ok  	bootcamp/lab02/pgstore	5.018s
# real	0m6.016s
```

En zsh, `time` imprime una sola línea con el formato `... 1.441 total`: el último número es el mismo `real`. La fase unitaria tarda 1.5 s; con la integración, 6 s.

### Paso 2: cuántos tests tiene cada fase

Con `-v`, `go test` imprime una línea `--- PASS` por test. Las funciones de test van al principio de la línea; los subtests, con sangría.

1. Guarda la salida de cada fase:

   ```bash
   go test -count=1 -race -v ./... > unit.log
   go test -count=1 -race -tags integration -v ./... > all.log
   ```

2. Cuenta. `grep -c` cuenta las líneas que cumplen el patrón; `^` es el principio de la línea y ` +` uno o más espacios:

   ```bash
   grep -cE '^--- PASS' unit.log
   # 3
   grep -cE '^ +--- PASS' unit.log
   # 11
   grep -cE '^--- PASS' all.log
   # 6
   grep -cE '^ +--- PASS' all.log
   # 11
   ```

   La fase unitaria tiene 3 funciones con 11 subtests; la integración añade 3 funciones sin subtests. Si algún test falla, cuenta también `--- FAIL` y arréglalo antes de escribir el reporte.

### Paso 3: los tests más lentos

1. Ordena los tests por su tiempo. `sort -t'(' -k2 -rn` parte cada línea por el `(` y ordena por lo que viene después, como número y de mayor a menor:

   ```bash
   grep -E -- '--- PASS' all.log | sort -t'(' -k2 -rn | head -3
   # --- PASS: TestLendUntilNoCopies (0.00s)
   # --- PASS: TestLendUnknownBook (0.00s)
   # --- PASS: TestLendEndpoint (0.00s)
   ```

   `--` le dice a `grep` que lo siguiente ya no es una opción, aunque empiece por `-`.

2. Ningún test pasa de 0.00 s, pero los paquetes tardan segundos:

   ```bash
   grep -E '^(ok|FAIL)' all.log
   # ok  	bootcamp/lab02/catalog	1.017s
   # ok  	bootcamp/lab02/pgstore	4.996s
   ```

   En `pgstore`, unos 3 s son de `TestMain`: arrancar el contenedor y migrar. No aparecen en ningún test porque `TestMain` no es un test.

3. ¿Y el segundo de `catalog`, que no tiene `TestMain`? Repite sin la pausa final del race detector. `GORACE` es la variable de entorno con las opciones del race detector; `atexit_sleep_ms` es cuánto espera al terminar, 1000 ms por defecto:

   ```bash
   GORACE=atexit_sleep_ms=0 go test -count=1 -race ./catalog
   # ok  	bootcamp/lab02/catalog	0.016s
   ```

   El race detector espera 1 s al final de cada binario de test para informar de carreras en goroutines que siguen vivas. No lo quites de `verify.sh`: es un coste fijo por paquete, no un test lento.

### Paso 4: la cobertura de cada paquete

1. Las funciones con menos cobertura, de menor a mayor. `sort -k3 -n` ordena por el tercer campo como número:

   ```bash
   go tool cover -func=cover.out | grep -v '^total:' | sort -k3 -n | head -3
   # bootcamp/lab02/pgstore/store.go:33:	Migrate		70.0%
   # bootcamp/lab02/pgstore/store.go:73:	Lend		87.5%
   # bootcamp/lab02/catalog/catalog.go:20:	LateFee		100.0%
   ```

   `Migrate` no prueba qué pasa si un archivo de migración tiene un error de SQL. Abre `go tool cover -html=cover.out` para ver en rojo las líneas exactas ([semana 35](../../../week-35-concurrencia_y_calidad_en_go/2-practicas/lab-04-cobertura-y-linters/README.md)).

2. La tabla por paquete. Cada línea de `cover.out` después de la primera es un bloque de código con este formato:

   ```text
   bootcamp/lab02/catalog/catalog.go:20.41,21.18 1 6
   ```

   Archivo y posición del bloque, número de sentencias y cuántas veces se ejecutó. Con `-coverpkg=./...`, cada binario de test escribe todos los bloques del módulo, así que el mismo bloque aparece una vez por paquete con tests. El `awk` cuenta cada bloque una sola vez y lo da por cubierto si algún binario lo ejecutó:

   ```bash
   awk 'NR>1 {pkg=$1; sub(/\/[^\/]*$/, "", pkg); stmts[$1]=$2; pkgof[$1]=pkg; if ($3>0) hit[$1]=1}
   END {for (b in stmts) {total[pkgof[b]]+=stmts[b]; if (b in hit) cov[pkgof[b]]+=stmts[b]}
        for (p in total) printf "| %s | %d | %d | %.1f %% |\n", p, cov[p], total[p], 100*cov[p]/total[p]}' cover.out | sort
   # | bootcamp/lab02/catalog | 26 | 26 | 100.0 % |
   # | bootcamp/lab02/pgstore | 21 | 25 | 84.0 % |
   ```

   | Parte | Qué hace |
   |---|---|
   | `NR>1` | Salta la primera línea, `mode: atomic` |
   | `sub(/\/[^\/]*$/, "", pkg)` | Quita de la ruta el último `/` y el nombre del archivo: queda el paquete |
   | `stmts[$1]=$2` | Sentencias de cada bloque, con el bloque como clave: los repetidos se pisan |
   | `if ($3>0) hit[$1]=1` | El bloque está cubierto si alguna línea tiene un contador mayor que 0 |

   Compruébalo: (26 + 21) / (26 + 25) = 47 / 51 = 92.2 %, el total de `go tool cover -func`. Y compáralo con las líneas de `go test`: `catalog ... coverage: 51.0% of statements in ./...` era la parte del módulo que ejecutan los tests de `catalog`; su propia cobertura es el 100 %.

### Paso 5: escribir el reporte

Rellena `testing-report-go.md` con los datos de los pasos anteriores:

1. **Suite por capas:** una fila por fase, con sus paquetes, las funciones y subtests del Paso 2, el comando y el tiempo del Paso 1.
2. **Cobertura:** la tabla del Paso 4 con una fila de total, y qué fase cubre cada paquete: `pgstore` solo lo cubre la integración.
3. **Tests más lentos:** los tres del Paso 3 y, sobre todo, a dónde va el tiempo de cada paquete.
4. **Riesgos y huecos:** al menos tres cosas que la suite no prueba. Pistas en esta biblioteca: ¿hay algún test que pase por la API y por PostgreSQL a la vez? ¿Qué pasa si dos peticiones prestan la última copia al mismo tiempo? ¿Qué devuelve `Lend` si la migración no se ha aplicado?
5. **Próximos pasos:** los tests que añadirías primero, en orden.

No dejes ningún `TODO`:

```bash
grep -c TODO testing-report-go.md
# 0
```

## Experimentos

### Experimento 1: tiempos de la caché

1. Ejecuta la suite completa dos veces seguidas **sin** `-count=1`:

   ```bash
   go test -race -tags integration -v ./... | grep -E -- '--- PASS: TestLendUnknown|^ok'
   # --- PASS: TestLendUnknownBook (0.00s)
   # ok  	bootcamp/lab02/catalog	1.019s
   # ok  	bootcamp/lab02/pgstore	4.633s
   go test -race -tags integration -v ./... | grep -E -- '--- PASS: TestLendUnknown|^ok'
   # ok  	bootcamp/lab02/catalog	(cached)
   # --- PASS: TestLendUnknownBook (0.00s)
   # ok  	bootcamp/lab02/pgstore	(cached)
   ```

2. La segunda vez no arranca ningún contenedor: `go test` repite la salida guardada, con los tiempos de entonces, y `(cached)` en lugar del tiempo del paquete. Un reporte con esos tiempos describe una ejecución que no ha pasado. Por eso todos los comandos de este lab llevan `-count=1`.

## Comprobación final

```bash
./verify.sh > /dev/null; echo "exit=$?"
# exit=0
grep -c TODO testing-report-go.md
# 0
grep -cE '^## (Suite por capas|Cobertura|Tests más lentos|Riesgos y huecos|Próximos pasos)$' testing-report-go.md
# 5
```

## Preguntas de cierre

1. ¿Por qué el tiempo de un paquete no es la suma de los tiempos de sus tests? Nombra dos cosas que lo explican.
2. ¿Qué significa `coverage: 51.0% of statements in ./...` en la línea de `catalog`? ¿Dónde está la cobertura de `catalog`?
3. ¿Por qué el `awk` del Paso 4 necesita quitar los bloques repetidos?
4. ¿Qué riesgo de tu reporte no se arreglaría subiendo la cobertura?

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 01 - Una suite, un comando](../lab-01-una-suite-un-comando/README.md) | [Proyecto — Proyecto Integrador - Suite Go completa del dominio](../../3-proyecto/README.md) → |

[↑ Semana 36](../../README.md)
