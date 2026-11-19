# Lab 02 - Partición y valores límite

## Objetivo

Aplicar partición de equivalencia y valores límite a un formulario con tres campos y una regla que los combina, y reducir el resultado a una suite sin casos repetidos.

## Tiempo estimado

70 minutos.

## Requisitos

- La [teoría 02](../../1-teoria/02-tecnicas-de-diseno.md), secciones 1 a 3.
- La carpeta `~/labs-semana-02` del [lab 01](../lab-01-analisis-de-casos/README.md).

## Punto de partida

```bash
cd ~/labs-semana-02
cp <ruta-del-bootcamp>/bootcamp/week-02-casos_de_prueba_y_tecnicas_de_diseno/2-practicas/lab-02-particion-y-valores-limite/starter/analisis.md .
```

## Contexto

Un planetario abre reservas online. El formulario de registro de visitantes tiene tres campos, y sus reglas están al principio de [`analisis.md`](starter/analisis.md). Tienes que diseñar los casos antes de que el formulario esté terminado.

---

## Paso 1: partición (parte A, 20 min)

Para cada campo, una clase válida y las inválidas que salgan de **cada** regla. Para `nombre`, piensa en la longitud por los dos lados, el campo vacío, los números y los símbolos. Para `edad`, en el rango por los dos lados y en lo que no es un entero: `12.5` o `doce`.

En la tabla de `grupoEscolar`, las clases combinan los dos campos: G1 es Sí con una edad permitida para grupos, G2 es No, G3 es Sí con una edad que solo los grupos no permiten.

Escribe valores concretos. Para `nombre`, escribe el texto (`Ana Ruiz`), no «un nombre válido».

## Paso 2: valores límite (parte B, 20 min)

Aplica la variante de 3 valores a los dos rangos y al máximo de los grupos.

Para los nombres largos, la terminal te construye el dato y te dice su longitud. `printf 'a%.0s' {1..51}` repite `a` 51 veces; `wc -m` cuenta los caracteres de lo que recibe:

```bash
bash -c "printf 'a%.0s' {1..51}; echo"
# aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
bash -c "printf 'a%.0s' {1..51} | wc -m"
# 51
```

- `bash -c "..."` ejecuta el comando con bash, aunque tu terminal use otra shell. `{1..51}` es una expansión de bash.
- `|` pasa la salida de `printf` a `wc`.
- `; echo` añade un salto de línea al final para que el prompt no quede pegado.

En la tabla de `nombre` escribe el dato así: «`a` repetida 51 veces». Para los cortos, escríbelo tal cual: `Al` (2).

### Caracteres y bytes

`wc -m` cuenta caracteres; `wc -c` cuenta bytes. Con letras sin tilde coinciden. Con tildes y ñ, no:

```bash
printf 'Ñandú' | wc -m
# 5
printf 'Ñandú' | wc -c
# 7
```

La Ñ y la ú ocupan 2 bytes cada una en UTF-8. Un sistema que mide la longitud en bytes rechazaría un nombre de 49 letras con dos tildes, porque son 51 bytes. Añade en la parte C un caso con un nombre de exactamente 50 caracteres que incluya tildes o ñ. Es un valor límite y a la vez una clase que la tabla de la parte A no tenía.

## Paso 3: la suite mínima (parte C, 20 min)

Combina las partes A y B en la tabla de casos:

1. Empieza por los valores límite: cada uno es un caso.
2. Añade los valores de clase que ningún límite cubre: los números, los símbolos, el vacío, el decimal, el texto, G3.
3. En cada caso, una sola clase inválida; el resto de campos con valores válidos.
4. Combina clases válidas cuando puedas: el caso de `edad` 99 puede llevar el nombre de 50 caracteres.

En la columna **Esperado** escribe «Visitante registrado» o el campo bajo el que sale el error: «Error en edad».

## Paso 4: el conteo (parte D, 10 min)

Cuenta y responde la pregunta del final. Si la suite tiene más de 25 casos, busca casos que cubran lo mismo; si tiene menos de 15, busca clases o límites sin caso.

---

## Comprobación final

```bash
grep -c TODO analisis.md
# 0
grep -c '^| TC-PLAN-REG-' analisis.md
# entre 15 y 25
```

`grep -c '^| TC-PLAN-REG-'` cuenta las líneas que empiezan (`^`) por `| TC-PLAN-REG-`: las filas de la suite.

Repasa:

- [ ] Cada clase de la parte A y cada valor de la parte B aparece en la columna **Cubre** de algún caso.
- [ ] Ningún caso tiene dos datos inválidos.
- [ ] El 17 y el 18 con `grupoEscolar` Sí están en la suite, y el 18 con No también.
- [ ] Hay un caso con nombre de 50 caracteres con tildes o ñ.

## Qué has aprendido

- Las clases salen de cada regla del campo, no solo del rango.
- Una regla que combina campos crea clases nuevas y límites nuevos.
- La longitud de un texto depende de cómo se cuente: con tildes, caracteres y bytes no coinciden.
- Juntar clases válidas y separar inválidas da una suite pequeña sin perder cobertura.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 01 - Análisis de casos](../lab-01-analisis-de-casos/README.md) | [Lab 03 - Tabla de decisión](../lab-03-tabla-de-decision/README.md) → |

[↑ Semana 02](../../README.md)
