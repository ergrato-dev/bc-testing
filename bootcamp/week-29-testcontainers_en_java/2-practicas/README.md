# Prácticas - Semana 29

Cuatro labs guiados (4 h en total). Cada uno trae un `starter/` con el código de producción completo y los tests comentados por PASOS. Con todos los PASOS descomentados, el lab queda resuelto. Los labs no se entregan: el entregable es el [proyecto](../3-proyecto/README.md).

Cada lab es un proyecto Maven independiente con solo lo que necesita. Los labs 01, 02 y 04 no arrancan Spring: heredan de `spring-boot-starter-parent` solo para no escribir versiones.

| Lab | Qué practicas | Tiempo |
|---|---|---|
| [Lab 01 - GenericContainer y PostgreSQLContainer](lab-01-genericcontainer-y-postgresqlcontainer/README.md) | `GenericContainer` a mano, `@Testcontainers`, `@Container`, puerto publicado, `static` frente a instancia, `withReuse(true)` | 60 min |
| [Lab 02 - El esquema con init.sql y con Flyway](lab-02-esquema-con-init-sql-y-flyway/README.md) | `withInitScript`, restricciones por SQLState, migraciones de Flyway, `flyway_schema_history`, una migración rota | 60 min |
| [Lab 03 - @ServiceConnection con Spring Boot](lab-03-serviceconnection-con-spring-boot/README.md) | `@JdbcTest`, `JdbcClient`, `@DynamicPropertySource`, `@ServiceConnection`, contenedor como bean, rollback por test | 60 min |
| [Lab 04 - Kafka y el contenedor compartido](lab-04-kafka-y-contenedor-compartido/README.md) | `KafkaContainer`, orden por clave entre particiones, consumo con plazo, contenedor singleton | 60 min |

## Antes de empezar

```bash
java -version                                # openjdk version "21..." o superior
mvn -v                                       # Apache Maven 3.9 o superior
docker version --format '{{.Server.Version}}'  # cualquier versión: si responde, Docker está listo
```

Cada lab se ejecuta desde su `starter/`:

```bash
cd lab-NN-.../starter
mvn test
```

La primera ejecución de cada lab descarga las imágenes de Docker que usa. Las siguientes las reutilizan.

Para descomentar un PASO, selecciona sus líneas y usa el atajo de comentar de tu editor (`Ctrl+/` en VS Code e IntelliJ): quita un nivel de `//`. Un PASO puede tener varios bloques en el mismo archivo (sus `import`, un campo, un test): descomenta todos los que lleven su número. Cuando un archivo entero pertenece a un PASO, descoméntalo todo menos la línea `package`.

Al terminar, `docker ps` no debería mostrar ningún contenedor de los labs: Ryuk los borra. La excepción es el experimento 3 del lab 01, que deja uno a propósito y te dice cómo borrarlo.

<!-- nav -->
---

## 🔗 Navegación

| Anterior | Siguiente |
|---|---|
| ← [Teoría — 04 - Contenedores compartidos, reutilización y Kafka](../1-teoria/04-contenedores-compartidos-y-kafka.md) | [Lab 01 - GenericContainer y PostgreSQLContainer](lab-01-genericcontainer-y-postgresqlcontainer/README.md) → |

[↑ Semana 29](../README.md)
