# Prácticas - Semana 27

Cuatro labs guiados (4 h en total). Cada uno trae un `starter/` con los tests comentados por PASOS y una `solution/` con el resultado. Los labs no se entregan: el entregable es el [proyecto](../3-proyecto/README.md).

En los labs 01 a 03 el código de producción **no** está en el starter: lo escribes tú, ciclo a ciclo. El README de cada lab da el Green de cada paso para que puedas comparar el tuyo.

| Lab | Qué practicas | Tiempo |
|---|---|---|
| [Lab 01 - Kata Prime Factors](lab-01-prime-factors/README.md) | Red de compilación y de aserción, Green mínimo, test de confirmación, `if` → `while` | 55 min |
| [Lab 02 - Kata Game of Life](lab-02-game-of-life/README.md) | Diseño emergente con `enum`, `record` y una clase inmutable; refactor con la suite como red | 75 min |
| [Lab 03 - Outside-in con Mockito](lab-03-outside-in-con-mockito/README.md) | Interfaces descubiertas desde el test, `Clock.fixed`, bajar un nivel sin mocks | 65 min |
| [Lab 04 - Fitness functions con ArchUnit](lab-04-archunit/README.md) | `@ArchTest`, dependencias entre paquetes, llamadas prohibidas, campos `final`, capas | 45 min |

## Antes de empezar

```bash
java -version    # openjdk version "21..." o superior
mvn -v           # Apache Maven 3.9 o superior
```

Cada lab es un proyecto Maven independiente:

```bash
cd lab-NN-.../starter
mvn test
```

Para descomentar un PASO, selecciona sus líneas y usa el atajo de comentar de tu editor (`Ctrl+/` en VS Code e IntelliJ): quita un nivel de `//`. Las líneas que llevan doble comentario (`// //`) son comentarios de verdad y se quedan con uno.

Haz un commit por ciclo también en los labs si quieres practicar el ritmo del [proyecto](../3-proyecto/README.md): `git init` dentro de `starter/` basta.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Teoría — 04 - Fitness functions de arquitectura con ArchUnit](../1-teoria/04-archunit-fitness-functions.md) | [Lab 01 - Kata Prime Factors](lab-01-prime-factors/README.md) → |

[↑ Semana 27](../README.md)
