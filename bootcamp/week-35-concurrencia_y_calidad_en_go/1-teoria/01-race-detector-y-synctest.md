# 01 - Race detector y testing/synctest

> Lenguaje: **Go**

En Go, lanzar trabajo concurrente es tan fácil como escribir `go f()`, y un servidor HTTP ya atiende cada petición en su propia goroutine ([semana 34](../../week-34-http_bd_e_integracion_en_go/README.md)). Eso trae dos problemas de testing que en JavaScript o Python apenas aparecían:

- **Carreras de datos.** Dos goroutines tocan la misma variable a la vez. El test pasa casi siempre, y falla en producción.
- **Código que depende del tiempo.** Una reserva caduca a las 48 horas y un reintento espera 1, 2 y 4 segundos. Un test que espera de verdad es lento, y si acorta los plazos se vuelve inestable.

Go trae una herramienta para cada uno: el **race detector** (`-race`) y el paquete **`testing/synctest`**.

---

## 🎯 Objetivos

- Reconocer una carrera de datos y por qué un test verde no demuestra que no la haya.
- Ejecutar los tests con `-race` y leer su informe.
- Probar código con `time.Sleep`, temporizadores y plazos de `context` en una burbuja de `synctest`, con tiempo virtual.
- Saber qué bloqueos hacen avanzar el reloj de la burbuja y cuáles no.

---

## 1. Qué es una carrera de datos

Hay una **carrera de datos** cuando dos goroutines acceden a la misma variable al mismo tiempo, al menos una de ellas escribe, y nada las sincroniza (ni un `sync.Mutex`, ni un canal, ni una operación de `sync/atomic`).

```go
type Views struct {
	byISBN map[string]int
}

func (v *Views) Record(isbn string) {
	v.byISBN[isbn]++ // leer, sumar 1 y escribir: tres pasos que otra goroutine puede cortar
}
```

Si dos peticiones llaman a `Record` a la vez, el resultado depende del orden en que el procesador intercale los pasos: se puede perder una visita, o el mapa puede quedar corrupto. Go detecta algunos casos sin ayuda: con mucha concurrencia, el programa muere con `fatal error: concurrent map writes` ([experimento 1 del lab 01](../2-practicas/lab-01-race-detector-y-synctest/README.md#experimento-1-sin--race-y-con-mucha-concurrencia)). Con poca, el test pasa en verde: dos goroutines que registran una visita cada una terminan casi siempre antes de pisarse.

Un test concurrente en verde no demuestra nada. Hace falta una herramienta que vea los accesos, no solo el resultado.

---

## 2. El race detector

```bash
go test -race ./...
```

`-race` compila el código con instrumentación: cada lectura y escritura de memoria queda registrada junto con la goroutine que la hizo y las sincronizaciones entre goroutines. Si dos accesos a la misma dirección no están ordenados por ninguna sincronización y uno es una escritura, el detector imprime un informe y el test falla:

```text
==================
WARNING: DATA RACE
Read at 0x00c00019c660 by goroutine 9:
  bootcamp/lab01.(*Views).Record()
      <lab>/views.go:15 +0x56
  ...
Previous write at 0x00c00019c660 by goroutine 10:
  bootcamp/lab01.(*Views).Record()
      <lab>/views.go:15 +0x8e
  ...
Goroutine 9 (running) created at:
  sync.(*WaitGroup).Go()
  ...
==================
--- FAIL: TestViewsConcurrent (0.00s)
    testing.go:1712: race detected during execution of test
```

| Bloque del informe | Qué dice |
|---|---|
| `Read at ... by goroutine 9` | El acceso que chocó, con su pila de llamadas: la línea 15 de `views.go` |
| `Previous write at ... by goroutine 10` | El otro acceso, sin sincronización con el primero |
| `Goroutine 9 (running) created at` | Dónde se lanzó cada goroutine: aquí, el `wg.Go` del test |
| `race detected during execution of test` | El test falla aunque sus comprobaciones hayan pasado |

Tres cosas que conviene saber:

| Propiedad | Consecuencia |
|---|---|
| Solo ve las carreras que **ocurren** durante la ejecución | Un código sin test concurrente no se revisa. El test tiene que provocar los accesos simultáneos |
| No da falsos positivos | Si informa de una carrera, la hay. No se silencia, se arregla |
| Necesita cgo y un compilador de C | Con `CGO_ENABLED=0` o sin `gcc`: `go: -race requires cgo` |
| Es más lento y usa más memoria | Según la documentación de Go, de 2 a 20 veces más tiempo y de 5 a 10 veces más memoria. Se usa en tests y en CI, no en producción |

Por la segunda fila, la regla en CI es sencilla: la suite se ejecuta con `-race` y no se admite ninguna carrera.

Con `-race`, la cobertura usa el modo `atomic`, porque los contadores de cobertura también se escriben desde varias goroutines ([teoría 04](04-cobertura-y-linters.md#2-los-tres-modos)).

---

## 3. synctest: tiempo virtual

El paquete `testing/synctest` (estable desde Go 1.25) ejecuta una función dentro de una **burbuja**: un grupo de goroutines aislado, con su propio reloj.

```go
func TestHoldExpires(t *testing.T) {
	synctest.Test(t, func(t *testing.T) {
		h := NewHolds(48 * time.Hour) // la reserva caduca con un time.AfterFunc
		h.Place("978-84-376-0494-7", "ana")

		time.Sleep(48 * time.Hour) // no espera: el reloj de la burbuja salta
		synctest.Wait()            // deja que el AfterFunc termine

		if _, ok := h.Holder("978-84-376-0494-7"); ok {
			t.Error("still held after 48h")
		}
	})
}
```

Dentro de la burbuja:

| Regla | Qué significa |
|---|---|
| El reloj empieza el 1 de enero de 2000 a medianoche UTC | `time.Now()` no es la hora real. Lo que importa son las diferencias: `time.Since(start)` mide el tiempo virtual |
| El tiempo solo avanza cuando **todas** las goroutines de la burbuja están bloqueadas de forma duradera | Entonces el reloj salta al siguiente temporizador. 48 horas de `time.Sleep` tardan 0.00 s |
| `synctest.Wait()` espera a que las demás goroutines de la burbuja estén bloqueadas | Después de `Wait`, lo que el reloj disparó ya terminó de ejecutarse |
| `synctest.Test` espera a que todas las goroutines de la burbuja terminen | Si alguna queda bloqueada para siempre, el test falla con `deadlock` |

Como el reloj solo avanza cuando nada más puede ejecutarse, las duraciones son **exactas**: un reintento con esperas de 1, 2 y 4 segundos tarda exactamente `7s` en la burbuja, y el test lo puede comparar con `==` ([lab 01, paso 3](../2-practicas/lab-01-race-detector-y-synctest/README.md#paso-3-reintentos-con-tiempo-exacto-paso-3)). Fuera de la burbuja tardaría 7 segundos reales y daría `7.002622367s`.

### Bloqueos duraderos

Un bloqueo es **duradero** cuando solo otra goroutine de la misma burbuja puede desbloquearlo. Son duraderos:

- Enviar o recibir por un canal creado dentro de la burbuja, y un `select` en el que todos los canales son de la burbuja.
- `time.Sleep`, y esperar un `time.Timer` o un `time.Ticker` creados en la burbuja.
- `sync.WaitGroup.Wait`, si el `Add` o el `Go` se llamó dentro de la burbuja, y `sync.Cond.Wait`.

**No** son duraderos, porque algo de fuera podría desbloquearlos: esperar un `sync.Mutex`, la entrada y salida de red o disco, y las llamadas al sistema. Un test que dentro de la burbuja espera una respuesta de un servidor real (aunque sea un `httptest.NewServer`) no hace avanzar el reloj.

### Por qué hace falta Wait

`time.Sleep(48 * time.Hour)` devuelve el control al test en cuanto el reloj llega a esa hora. A la misma hora, el temporizador de la reserva lanza su función en otra goroutine. Sin `synctest.Wait()`, el test y esa función compiten: unas veces el test mira la reserva antes de que se borre y otras después. En el [experimento 2 del lab 01](../2-practicas/lab-01-race-detector-y-synctest/README.md#experimento-2-sin-synctestwait) falla 9 veces de 20. `Wait` cierra esa ventana: no devuelve el control hasta que las demás goroutines están bloqueadas, es decir, hasta que la función del temporizador terminó.

### Goroutines que no terminan

Al acabar la función, `synctest.Test` espera a todas las goroutines de la burbuja. Una goroutine que nadie para hace fallar el test:

```text
panic: deadlock: main bubble goroutine has exited but blocked goroutines remain
```

Es un detector de **fugas de goroutines** gratuito: un `go func()` que se olvida de terminar ya no pasa desapercibido.

---

## 4. Dónde se usan juntos

`-race` y `synctest` no compiten: `synctest` decide **cuándo** avanza el tiempo y `-race` vigila **cómo** se accede a la memoria. Un test con `synctest` también se ejecuta con `-race`, y el race detector ve las goroutines de la burbuja igual que las demás.

| Situación | Herramienta |
|---|---|
| Un tipo que reciben varias goroutines (un contador, una caché, un almacén en memoria) | Un test que lo llame desde varias goroutines, con `-race` |
| Un temporizador, un `time.Sleep`, un reintento con espera | `synctest.Test` y `synctest.Wait` |
| Un plazo de `context.WithTimeout` | `synctest.Test`: el plazo vence en tiempo virtual |
| Un handler HTTP que recibe peticiones a la vez | `httptest.NewServer` y varias goroutines, con `-race` (proyecto) |

Comparado con lo que viste en otros lenguajes: Jest simula el reloj con `jest.useFakeTimers()` ([semana 9](../../week-09-testing_asincrono_con_jest/1-teoria/03-fake-timers.md)), pero tienes que avanzarlo tú con `advanceTimersByTime`. En `synctest` el reloj avanza solo, cuando todo está bloqueado, y no hace falta cambiar el código de producción para inyectar un reloj.

---

## Resumen

| Herramienta | Para qué |
|---|---|
| `go test -race ./...` | Detectar carreras de datos que ocurren durante los tests |
| `sync.Mutex` | Proteger un mapa o un contador compartido |
| `synctest.Test(t, f)` | Ejecutar `f` en una burbuja con reloj virtual |
| `synctest.Wait()` | Esperar a que las demás goroutines de la burbuja estén bloqueadas |
| `time.Since(start)` en la burbuja | Medir duraciones exactas |

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Semana 35 - Concurrencia y Calidad en Go](../README.md) | [Teoría — 02 - Fuzzing nativo](02-fuzzing-nativo.md) → |

[↑ Semana 35](../README.md)
