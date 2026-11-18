# Webgrafía - Semana 36

| Recurso | URL | Para qué |
|---|---|---|
| Bash Reference Manual: The Set Builtin | https://www.gnu.org/software/bash/manual/html_node/The-Set-Builtin.html | La referencia de `-e`, `-u` y `-o pipefail`, con los casos en los que `-e` no para el script. |
| Data Race Detector: Options | https://go.dev/doc/articles/race_detector#Options | Las opciones de `GORACE`, entre ellas `atexit_sleep_ms`: la pausa de 1 s al final de cada binario de test del lab 02. |
| cmd/go: Testing flags | https://pkg.go.dev/cmd/go#hdr-Testing_flags | `-count`, `-list`, `-shuffle`, `-coverpkg` y `-coverprofile`, todas las opciones de `verify.sh`. |
| cmd/go: Test packages | https://pkg.go.dev/cmd/go#hdr-Test_packages | Cuándo `go test` guarda resultados en caché y los muestra como `(cached)`, el experimento 1 del lab 02. |
| cmd/cover | https://pkg.go.dev/cmd/cover | `go tool cover -func` y `-html`, de donde salen el total y las líneas sin cubrir. |
| The cover story | https://go.dev/blog/cover | Cómo instrumenta Go el código para medir la cobertura y qué es un bloque del perfil, la unidad que cuenta el `awk` del lab 02. |

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Recursos — videos](../videografia/README.md) | [Glosario](../../5-glosario/README.md) → |

[↑ Semana 36](../../README.md)
