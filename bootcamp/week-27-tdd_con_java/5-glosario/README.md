# Glosario Semana 27 - TDD con Java

## A

- **`@AnalyzeClasses`**: anotación de ArchUnit que indica qué paquetes cargar para comprobar las reglas de la clase de test.
- **`@ArchTest`**: marca un campo `static final ArchRule` (o un método) como test de ArchUnit.
- **ArchUnit**: librería de Java que comprueba reglas de arquitectura leyendo el bytecode de las clases.

## C

- **`Clock`**: clase de `java.time` que da la fecha y la hora. Se pasa por el constructor para que los tests usen `Clock.fixed`.
- **`Clock.fixed`**: reloj que siempre devuelve el mismo instante. Un fake de la librería estándar.

## D

- **Diseño emergente**: diseño que aparece al escribir los tests antes del código, en lugar de dibujarse por adelantado.

## F

- **Fitness function de arquitectura**: comprobación automática de una decisión de diseño, ejecutada con la suite.
- **`FreezingArchRule`**: envoltorio de ArchUnit que guarda las violaciones existentes y solo falla con las nuevas.

## I

- **Inside-out**: TDD que empieza por las piezas sin dependencias y sube hacia el servicio. También llamado escuela clásica o de Chicago.

## L

- **`layeredArchitecture()`**: regla de ArchUnit que define capas por paquete y qué capas pueden usar a cuáles.
- **Lista de tests**: los comportamientos que esperas implementar, apuntados antes de empezar. Se cambia durante el trabajo.

## O

- **Outside-in**: TDD que empieza por el test del servicio, con mocks para las dependencias que aún no existen. También llamado escuela de Londres.

## R

- **Red de aserción**: el test se ejecuta y falla porque el resultado no es el esperado.
- **Red de compilación**: el test no compila porque falta una clase o un método. En Java es el primer paso al crear algo nuevo.
- **Refactor**: cambio de estructura del código que no cambia su comportamiento. Se hace con la suite en verde.

## T

- **Test de confirmación**: test nuevo que pasa a la primera. Se comprueba rompiendo el código a propósito.
- **Triangular**: añadir un test con otro valor para que un Green que devuelve una constante deje de servir.

## V

- **Violación (ArchUnit)**: cada línea del mensaje de una regla que falla: quién, qué hace, a quién y en qué línea.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Recursos — web](../4-recursos/webgrafia/README.md) | — |

[↑ Semana 27](../README.md)
