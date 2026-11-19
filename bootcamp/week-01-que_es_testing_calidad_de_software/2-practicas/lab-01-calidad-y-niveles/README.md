# Lab 01 - Calidad y niveles

## Objetivo

Aplicar la teoría a casos concretos de una tienda online: a qué dimensión de calidad afecta cada fallo, a qué nivel pertenece cada test, si una suite tiene forma de pirámide o de cono y qué conviene probar a mano.

## Tiempo estimado

60 minutos.

## Requisitos

- Las teorías [01](../../1-teoria/01-calidad-y-tipos-de-testing.md) y [02](../../1-teoria/02-piramide-y-niveles.md).
- Una terminal y un editor de texto.

## Punto de partida

Crea una carpeta para los labs de la semana y copia en ella la plantilla:

```bash
mkdir -p ~/labs-semana-01
cd ~/labs-semana-01
cp <ruta-del-bootcamp>/bootcamp/week-01-que_es_testing_calidad_de_software/2-practicas/lab-01-calidad-y-niveles/starter/clasificacion.md .
```

- `mkdir -p` crea la carpeta; `-p` evita el error si ya existe.
- `cd` entra en ella.
- `cp <origen> .` copia el archivo a la carpeta actual (el `.`). Sustituye `<ruta-del-bootcamp>` por la carpeta donde clonaste el bootcamp.

Abre [`clasificacion.md`](starter/clasificacion.md) en tu editor. Tiene cuatro partes con `TODO`.

---

## Paso 1: dimensiones de calidad (parte A, 10 min)

Para cada fallo, escribe la dimensión de la tabla de la [sección 1 de la teoría 01](../../1-teoria/01-calidad-y-tipos-de-testing.md#1-qué-es-la-calidad-del-software) a la que afecta y por qué.

Pregunta guía: si este fallo no existiera, ¿qué mejoraría para el usuario? ¿Que el resultado sea correcto, que no se caiga, que lo entienda, que vaya rápido, que sus datos estén protegidos, o que el equipo pueda cambiarlo sin miedo?

Un fallo puede tocar dos dimensiones. Escribe la principal y menciona la otra en el porqué.

## Paso 2: el nivel de cada test (parte B, 15 min)

Para cada test, rellena primero la columna **Qué usa** (una función con datos, una base de datos, un archivo, un servidor, un navegador) y después el nivel. Aplica en orden las preguntas de la [sección 3 de la teoría 02](../../1-teoria/02-piramide-y-niveles.md#3-cómo-decidir-el-nivel-de-un-test).

Dos casos para pensar despacio:

- **B4** usa HTTP, pero sin navegador.
- **B6** tiene dos respuestas a la vez: por quién escribió el criterio y por lo que usa.

## Paso 3: pirámide o cono (parte C, 20 min)

Con los números de la tabla:

1. Calcula qué porcentaje de cada suite son E2E. Por ejemplo, en Tienda: 15 de 495 tests.
2. Decide la forma de cada proyecto.
3. Para Reservas, escribe dos síntomas de la [sección 2 de la teoría 02](../../1-teoria/02-piramide-y-niveles.md#el-antipatrón-el-cono-de-helado) que esperarías ver en el equipo.
4. Inventa un E2E típico de una app de reservas (por ejemplo, «reservar una habitación para dos noches y comprobar el precio») y escribe qué comprobaría la versión unitaria o de integración de ese test. No pierdes la comprobación: la haces más abajo, donde cuesta menos.

## Paso 4: manual o automatizado (parte D, 15 min)

Para cada situación, decide con la [sección 3 de la teoría 01](../../1-teoria/01-calidad-y-tipos-de-testing.md#3-testing-manual-y-testing-automatizado). Pregunta guía: ¿cuántas veces se va a repetir, y el resultado se puede comparar con un valor esperado?

---

## Comprobación final

```bash
grep -c TODO clasificacion.md
# 0
```

`grep -c TODO` cuenta las líneas que contienen `TODO`. `0` quiere decir que has respondido todo. Con 0 coincidencias, `grep` termina con código de salida 1: es normal.

Repasa también:

- [ ] Cada respuesta de la parte B tiene rellena la columna **Qué usa**, y el nivel se deduce de ella.
- [ ] La parte C tiene números, no solo «muchos» o «pocos».
- [ ] Ninguna respuesta de la parte D dice «automatizado» para algo que juzga una persona.

## Qué has aprendido

- La calidad no es solo que el resultado sea correcto: un fallo de rendimiento, de seguridad o de usabilidad también es un bug.
- El nivel de un test lo decide lo que usa, no su nombre.
- La forma de una suite se mide con números, y un cono se corrige bajando comprobaciones de nivel.
- Lo que se repite se automatiza; lo que juzga una persona se prueba a mano.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Índice de labs](../README.md) | [Lab 02 - Análisis de reportes](../lab-02-analisis-de-reportes/README.md) → |

[↑ Semana 01](../../README.md)
