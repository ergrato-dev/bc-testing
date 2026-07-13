# 03 - Introduccion a Mocks, Stubs y Spies

**Tipo**: JavaScript (Jest)

![Test doubles en Jest](../0-assets/03-test-doubles-jest.svg)

---

## Por que existen

Un test unitario no deberia depender de una base de datos real, un servicio externo o un temporizador del sistema. Los test doubles reemplazan esas dependencias por versiones controladas, para que el test sea rapido, repetible y aislado. Esta semana solo se ve el nivel introductorio; la profundidad completa (matchers de mocks, timers falsos, modulos completos mockeados) se cubre en la semana 10.

---

## Diferencias esenciales

- **Mock**: reemplaza una dependencia completa y permite verificar como fue llamada (cuantas veces, con que argumentos).
- **Stub**: devuelve datos controlados para un caso especifico, sin que el test le importe si fue llamado o no.
- **Spy**: observa llamadas sobre una implementacion real o parcial, dejando que el codigo original siga corriendo si no se le indica lo contrario.

---

## Ejemplos minimos

### Mock

```javascript
const sendEmail = jest.fn();

service.notify(sendEmail);

expect(sendEmail).toHaveBeenCalledTimes(1);
```

El foco esta en la llamada misma: cuantas veces se invoco y con que datos.

### Stub

```javascript
const repo = { findById: jest.fn().mockReturnValue({ id: 1, active: true }) };

const exhibit = repo.findById(1);

expect(exhibit.active).toBe(true);
```

El foco esta en el dato devuelto: el test no verifica si `findById` fue llamado, solo usa lo que retorna.

### Spy

```javascript
const spy = jest.spyOn(logger, "info");
service.run();
expect(spy).toHaveBeenCalled();
```

`logger.info` sigue ejecutando su codigo real; `spy` solo agrega observabilidad encima.

---

## Cuando se parecen y cuando no

Con `jest.fn()` se puede tanto verificar llamadas (uso de mock) como devolver datos fijos (uso de stub); la diferencia esta en la intencion del test, no en la API usada. Un mismo `jest.fn()` actua como stub si el test solo lee su valor de retorno, y como mock si el test hace `expect(fn).toHaveBeenCalledWith(...)`.

`jest.spyOn` se distingue de ambos porque por defecto no reemplaza el comportamiento: envuelve la funcion real. Solo se vuelve un stub o mock si ademas se le encadena `.mockImplementation()` o `.mockReturnValue()`.

| Pregunta que responde el test | Double recomendado |
|---|---|
| "¿Que devolvio la dependencia?" | Stub |
| "¿Se llamo con estos argumentos?" | Mock |
| "¿La funcion real se ejecuto, y ademas se llamo?" | Spy |

---

## Criterios de uso

- Aisla colaboraciones externas en unit tests (red, disco, tiempo, servicios de terceros).
- No mockees todo por defecto; primero identifica la dependencia critica que hace el test lento o no determinista.
- Verifica comportamiento observable, no detalles internos irrelevantes.
- Si el test necesita tanto un dato controlado como confirmar la llamada, usar mock (un stub que ademas se puede assertar con `toHaveBeenCalled`).

---

## Errores frecuentes a este nivel

- Confundir "usé `jest.fn()`" con "usé un mock": el nombre del double depende de que verifica el test, no de la funcion de Jest usada para crearlo.
- Usar un spy cuando en realidad se necesita reemplazar el comportamiento (la funcion real corre y produce efectos secundarios no deseados en el test).
- Verificar `toHaveBeenCalled` sobre un stub que solo deberia aportar datos, acoplando el test a un detalle de implementacion que no aporta valor.

![Flujo de ejecucion de test en Jest](../0-assets/04-flujo-ejecucion-jest.svg)
