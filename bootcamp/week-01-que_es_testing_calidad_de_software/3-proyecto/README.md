# Proyecto - Reporte de bug profesional

## Descripción

Encuentra un bug real en un software que exista y documéntalo con un reporte que otra persona pueda reproducir sin preguntarte nada. Es lo mismo que has hecho en los labs, con una diferencia: aquí nadie te da el fallo. Lo buscas, lo reproduces y decides cómo clasificarlo.

## Tiempo estimado

3 horas.

## Punto de partida

```bash
mkdir -p ~/proyecto-semana-01
cd ~/proyecto-semana-01
cp <ruta-del-bootcamp>/bootcamp/week-01-que_es_testing_calidad_de_software/3-proyecto/starter/reporte-bug.md .
```

[`starter/reporte-bug.md`](starter/reporte-bug.md) es la plantilla: cada campo tiene un `TODO` con lo que va en él.

---

## Paso 1: elegir el software (30 min)

Si puedes, usa un software relacionado con el dominio que te asignó el instructor. Si no encuentras ninguno, sirve cualquiera que uses: una web, una app del móvil, un programa de escritorio, una herramienta de terminal.

Dónde suele haber bugs fáciles de reproducir:

- **Formularios:** campos que aceptan lo que no deberían (letras en un teléfono, fechas imposibles como el 31 de febrero), mensajes de error que no dicen qué está mal.
- **Límites:** un texto muy largo, un archivo grande, una lista vacía, el valor 0.
- **Idioma y formato:** textos sin traducir, fechas o números con el formato de otro país, acentos que salen mal.
- **Tamaños de pantalla:** la misma página en el móvil y en el ordenador.

Dos reglas:

- **No inventes el bug.** Tiene que ser algo que has visto tú.
- **No pruebes en sistemas que no son tuyos de formas que puedan hacer daño:** nada de intentar entrar en cuentas ajenas ni de enviar cientos de peticiones. Usa el software como lo usaría cualquier usuario.

## Paso 2: reproducirlo (45 min)

Antes de escribir:

1. Reprodúcelo al menos **3 veces** desde el mismo punto de partida. Apunta cuántas veces sale de cuántos intentos: un fallo que sale 2 de 5 veces también se reporta, y ese dato ayuda.
2. Apunta los pasos exactos mientras lo reproduces, no después.
3. Apunta el entorno con versiones. Dónde encontrarlas:
   - **Navegador:** en Chrome y Edge, menú ⋮ > Ayuda > Información; en Firefox, menú ☰ > Ayuda > Acerca de Firefox.
   - **App del móvil:** Ajustes > Aplicaciones > la app.
   - **Sistema operativo:** en Windows, `winver` (pulsa Windows+R y escríbelo); en macOS, menú Apple > Acerca de este Mac; en Linux, `lsb_release -d` en la terminal.
4. Guarda la evidencia: una captura, un vídeo corto o el mensaje de error copiado tal cual.

Si no consigues reproducirlo de nuevo, vuelve al paso 1. Un reporte que no se puede reproducir acaba en `Cannot Reproduce`.

## Paso 3: escribir el reporte (60 min)

Rellena `reporte-bug.md`, en este orden:

1. **Pasos y punto de partida.** Que alguien que nunca ha usado el software llegue al fallo.
2. **Resultado obtenido.** Lo que pasa, sin diagnosticar.
3. **Resultado esperado.** Concreto, y dónde lo dice: la documentación, un requisito, o lo que hace el mismo software en un caso parecido.
4. **Título.** Al final, cuando ya sabes qué está mal y dónde: `[Componente] Qué está mal`.
5. **Entorno, evidencia y notas.**

## Paso 4: clasificar (20 min)

Asigna severidad y prioridad con las tablas de la [teoría 03](../1-teoria/03-ciclo-de-vida-del-bug.md#3-severidad-y-prioridad). Cada una lleva su justificación en una o dos frases: qué daño hace y a quién (severidad), qué urgencia tiene (prioridad). Si hay un camino alternativo para evitar el fallo, la severidad baja: dilo.

## Paso 5: comprobar y revisar (25 min)

```bash
PROYECTO=~/proyecto-semana-01 ./scripts/verificar-semana.sh 01
```

Ejecútalo desde la carpeta del bootcamp. El script lee las comprobaciones de [`checks.json`](../checks.json) y te dice cuáles no se cumplen y en qué paso se arreglan ([cómo funciona](../../../docs/autograding.md)).

Después, la prueba de verdad: pásale el reporte a otra persona (un compañero, alguien de tu casa) y pídele que reproduzca el fallo solo con lo que has escrito. Cada pregunta que te haga es un dato que le falta al reporte.

Por último, busca si el fallo ya está reportado (en el GitHub del proyecto, en su foro o en su página de soporte) y apúntalo en **Notas**. Si es de código abierto y no lo está, puedes reportarlo de verdad.

---

## ✅ Requisitos verificables

Las comprobaciones de [`checks.json`](../checks.json):

| Comprobación | Qué pide |
|---|---|
| `completo` | `reporte-bug.md` existe y no le queda ningún `TODO` |
| `titulo` | El título sigue el formato `[Componente] Qué está mal` |
| `clasificacion` | Severidad (Critical, High, Medium o Low) y prioridad (P1 a P4), cada una con su justificación |
| `pasos` | Al menos tres pasos numerados, con punto de partida y cuántas veces se reprodujo |
| `resultados` | Resultado obtenido y esperado escritos y distintos |
| `entorno` | El entorno dice sistema operativo y navegador o app con un número de versión |

El script comprueba la forma del reporte. Que sea reproducible lo comprueba la persona del paso 5, y la [rúbrica](../rubrica-evaluacion.md).

## Entrega

`reporte-bug.md` con la evidencia que cites, por el canal que indique el instructor. De la semana 1 a la 22 cada proyecto es independiente; el repositorio `qa-<nombre>-<apellido>` empieza en la semana 23.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Lab 04 - Triage y ciclo de vida](../2-practicas/lab-04-triage-y-ciclo-de-vida/README.md) | [Rúbrica de evaluación](../rubrica-evaluacion.md) → |

[↑ Semana 01](../README.md)
