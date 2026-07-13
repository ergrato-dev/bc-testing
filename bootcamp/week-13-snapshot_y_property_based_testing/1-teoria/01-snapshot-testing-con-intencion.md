# 01 - Snapshot Testing con Intencion

> **Lenguaje:** JavaScript (Jest)

![Signal vs Noise en snapshots](../0-assets/01-snapshot-signal-vs-noise.svg)

---

## Objetivo

Usar snapshots para detectar cambios relevantes sin generar mantenimiento innecesario.

---

## Cuando conviene snapshot

- Serializacion estable de salidas.
- Estructuras de respuesta con forma amplia pero controlada.
- Componentes o payloads donde importa estructura completa.
- Salidas de renderizado (HTML, markup, texto formateado) que deben mantenerse estables entre releases.

---

## Cuando evitarlo

- Datos con timestamps, ids random o campos volatiles sin normalizar.
- Objetos enormes que no expresan intencion de negocio.
- Casos donde una asercion explicita comunica mejor.
- Payloads que cambian de forma en cada iteracion del feature (todavia inestables).

---

## toMatchSnapshot vs toMatchInlineSnapshot

| Aspecto | `toMatchSnapshot()` | `toMatchInlineSnapshot()` |
|---|---|---|
| Almacenamiento | Archivo `__snapshots__/*.snap` separado | Dentro del propio archivo de test |
| Revision en PR | Requiere abrir el `.snap` aparte | Visible directo en el diff del test |
| Uso recomendado | Snapshots grandes o muchos por archivo | Snapshots pequeños, 1-5 lineas |
| Actualizacion | `jest --updateSnapshot` reescribe el `.snap` | `jest --updateSnapshot` reescribe el literal inline |

```javascript
test("should format museum ticket code inline", () => {
  const code = formatTicketCode({ hall: "paleontologia", visitorType: "adulto" });

  expect(code).toMatchInlineSnapshot(`"PALEO-ADT"`);
});
```

Si el snapshot cabe en una linea y aporta valor de lectura inmediata en el PR, preferi inline. Si es una estructura grande (payload JSON completo, arbol de componentes), usa el archivo `.snap`.

---

## Snapshot amplio vs snapshot enfocado

**Demasiado amplio** — captura el objeto completo, incluye campos volatiles y no comunica que se esta validando:

```javascript
test("should match exhibit payload snapshot (too broad)", () => {
  const exhibit = buildExhibitPayload({ id: "exh-04", title: "Sala de Ceramica" });

  // createdAt e internalId cambian en cada corrida -> snapshot fragil
  expect(exhibit).toMatchSnapshot();
});
```

**Enfocado** — se queda solo con los campos que expresan la regla de negocio bajo prueba:

```javascript
test("should match exhibit public summary snapshot", () => {
  const exhibit = buildExhibitPayload({ id: "exh-04", title: "Sala de Ceramica" });

  const { title, hall, isOpenToPublic } = exhibit;

  expect({ title, hall, isOpenToPublic }).toMatchSnapshot();
});
```

La segunda version falla solo cuando cambia algo que le importa al negocio, no cuando cambia un `id` interno o un timestamp.

---

## Ejemplo

```javascript
test("should match public profile payload snapshot", () => {
  const payload = buildPublicProfile({
    id: "u-10",
    name: "Ada",
    role: "mentor",
  });

  expect(payload).toMatchSnapshot();
});
```

---

## Errores frecuentes

- Aceptar `--updateSnapshot` sin revisar el diff, "porque el test ya pasa".
- Meter el objeto completo de un ORM/DB en el snapshot en vez de proyectar solo los campos relevantes.
- Usar snapshot para validar un unico valor primitivo (ahi una asercion directa es mas clara).

---

## Regla practica

Snapshot pequeño y estable > snapshot gigante que nadie revisa.
