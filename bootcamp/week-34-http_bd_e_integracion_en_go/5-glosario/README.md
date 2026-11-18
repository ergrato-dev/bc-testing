# Glosario Semana 34 - HTTP, BD e Integración en Go

## B

- **Build tag**: nombre que se pasa con `-tags` y que activa los archivos cuya línea `//go:build` lo pide. Sin la tag, el archivo no se compila.

## C

- **`context.Context`**: valor que lleva un plazo y una señal de cancelación de una llamada a otra. Va como primer parámetro, `ctx`.
- **Contrato JSON**: los nombres y tipos de los campos que una API promete a sus clientes. Se prueba decodificando la respuesta en un `map[string]any`.

## D

- **`DBTX`**: interfaz con los métodos de consulta (`Exec`, `QueryRow`) que cumplen el pool y una transacción. Permite pasarle al repositorio una transacción en los tests.

## E

- **`//go:embed`**: directiva que incrusta archivos en el binario, como las migraciones `.sql`.

## H

- **`httptest.NewRecorder`**: `http.ResponseWriter` en memoria que guarda lo que escribe un handler. `Result()` lo devuelve como un `*http.Response`.
- **`httptest.NewServer`**: servidor HTTP de verdad en `127.0.0.1` y un puerto libre, para probar clientes. `NewTLSServer` es su versión HTTPS.

## M

- **Migración**: archivo `.sql` numerado que cambia el esquema. Se aplican en orden, en producción y en los tests.

## P

- **`PathValue`**: método de `*http.Request` que devuelve el valor de un comodín de la ruta, como `{isbn}`.
- **pgx**: driver de PostgreSQL para Go. `pgxpool` es su pool de conexiones.

## R

- **Rollback por test**: cada test trabaja dentro de una transacción que se deshace al terminar, así que no deja datos para el siguiente.
- **Ryuk**: contenedor de testcontainers que borra los contenedores de un proceso de test que terminó sin pararlos.

## S

- **Savepoint**: punto dentro de una transacción al que se puede volver sin deshacerla entera. En pgx, `Begin` sobre una `pgx.Tx`.
- **`-short`**: flag de `go test`. Con él, `testing.Short()` devuelve `true` y los tests lentos pueden saltarse con `t.Skip`.
- **Snapshot**: copia de una base de datos ya migrada que el módulo `postgres` restaura entre tests con `Restore`.

## T

- **`TestMain`**: función que, si existe, ejecuta `go test` en lugar de los tests. Prepara y libera lo que comparte el paquete alrededor de `m.Run()`.
- **testcontainers-go**: librería que arranca contenedores de Docker desde los tests de Go y los para al terminar.
- **Transacción abortada** (`25P02`): estado de una transacción de PostgreSQL después de una sentencia fallida. Rechaza todo hasta el `ROLLBACK`.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Recursos — web](../4-recursos/webgrafia/README.md) | — |

[↑ Semana 34](../README.md)
