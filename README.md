\# API REST de avistamientos de aves



API REST para registrar avistamientos de aves observados en el campo, hecha con Spring Boot, Spring Data JPA y una base de datos H2 embebida.



\*\*Autor:\*\* Juan David Guarnizo Martinez



\## Requisitos



\- Java 17 o superior

\- Git (para clonar el repositorio)

\- `curl` (viene instalado en Windows 10/11, Linux y macOS)



No necesitas instalar Maven (el proyecto trae el wrapper `mvnw`) ni ninguna base de datos (H2 se guarda en un archivo local).



\## Instalación y ejecución



```bash

git clone https://github.com/Juanda210809/AvistamientoAves.git

cd AvistamientoAves/avistamiento\_aves

```



Ejecutar la API:



\- \*\*Linux / macOS:\*\*

&#x20; ```bash

&#x20; ./mvnw spring-boot:run

&#x20; ```

\- \*\*Windows (CMD o PowerShell):\*\*

&#x20; ```bash

&#x20; mvnw.cmd spring-boot:run

&#x20; ```



Cuando en la consola aparezca `Started AvesApplication`, la API está disponible en `http://localhost:8080`.



Los datos se guardan en la carpeta `data/`, que se crea sola la primera vez. La base de datos empieza vacía y cada máquina tiene su propia copia.



\## Modelo



Cada avistamiento tiene:



| Campo       | Tipo   | Descripción                                          |

|-------------|--------|------------------------------------------------------|

| `id`        | número | Lo asigna el sistema                                 |

| `especie`   | texto  | Nombre de la especie (por ejemplo "colibri")         |

| `lugar`     | texto  | Dónde se observó                                     |

| `fecha`     | texto  | Formato `AAAA-MM-DD`                                 |

| `observador`| texto  | Quién registró el avistamiento                       |



\## Regla de negocio



Un avistamiento es válido si:



\- la \*\*especie\*\* tiene entre 3 y 50 caracteres (sin contar espacios al inicio y al final),

\- la \*\*fecha\*\* tiene formato `AAAA-MM-DD`, es una fecha real y no es posterior a hoy,

\- el \*\*lugar\*\* y el \*\*observador\*\* no están vacíos.



Si no es válido, `POST` y `PUT` responden `400` con un JSON que indica qué campo falló:



```json

{"campo": "especie", "error": "La especie debe tener entre 3 y 50 caracteres"}

```



La validación vive en `ValidadorDeAvistamiento`, separada del controlador, y la fecha de hoy se obtiene a través de la interfaz `Reloj`, para poder probarla sin levantar la API.



\## Endpoints



| Operación                  | Petición                       | Respuesta                                  |

|----------------------------|--------------------------------|--------------------------------------------|

| Listar avistamientos       | `GET /avistamientos`           | `200` con la lista en JSON                 |

| Ver un avistamiento        | `GET /avistamientos/{id}`      | `200` con el avistamiento, o `404`         |

| Registrar un avistamiento  | `POST /avistamientos`          | `201` con el avistamiento creado, o `400`  |

| Actualizar un avistamiento | `PUT /avistamientos/{id}`      | `200` con el avistamiento, `400` si los datos son inválidos, o `404` |

| Eliminar un avistamiento   | `DELETE /avistamientos/{id}`   | `204` sin contenido, o `404`               |

| Resumen por especie (bonus)| `GET /avistamientos/resumen`   | `200` con el conteo por especie            |



\## Ejemplos con curl



La opción `-i` muestra el código de estado en la primera línea de la respuesta. Los ejemplos usan comillas dobles escapadas para que funcionen en Windows, Linux y macOS.



\### Registrar un avistamiento (201)



```bash

curl -i -X POST http://localhost:8080/avistamientos -H "Content-Type: application/json" -d "{\\"especie\\":\\"colibri\\",\\"lugar\\":\\"Chingaza\\",\\"fecha\\":\\"2026-09-29\\",\\"observador\\":\\"Ana\\"}"

```



\### Registrar con datos inválidos (400)



```bash

curl -i -X POST http://localhost:8080/avistamientos -H "Content-Type: application/json" -d "{\\"especie\\":\\"ab\\",\\"lugar\\":\\"Chingaza\\",\\"fecha\\":\\"2026-09-29\\",\\"observador\\":\\"Ana\\"}"

```



\### Listar todos (200)



```bash

curl -i http://localhost:8080/avistamientos

```



\### Ver uno por id (200, o 404 si no existe)



```bash

curl -i http://localhost:8080/avistamientos/1

curl -i http://localhost:8080/avistamientos/999

```



\### Actualizar (200, o 404 si no existe)



```bash

curl -i -X PUT http://localhost:8080/avistamientos/1 -H "Content-Type: application/json" -d "{\\"especie\\":\\"colibri\\",\\"lugar\\":\\"Sumapaz\\",\\"fecha\\":\\"2026-09-30\\",\\"observador\\":\\"Ana\\"}"

```



\### Eliminar (204, o 404 si no existe)



```bash

curl -i -X DELETE http://localhost:8080/avistamientos/1

```



\### Resumen por especie (200)



```bash

curl -i http://localhost:8080/avistamientos/resumen

```



Respuesta de ejemplo:



```json

{"colibri": 2, "aguila": 1}

```



\## Pruebas y cobertura



Todos los comandos se ejecutan desde la carpeta `avistamiento\_aves/`.



\- \*\*Correr las pruebas\*\* (unitarias y de integración):

&#x20; ```bash

&#x20; ./mvnw test          # Linux / macOS

&#x20; mvnw.cmd test        # Windows

&#x20; ```

\- \*\*Generar la cobertura\*\* (JaCoCo):

&#x20; ```bash

&#x20; ./mvnw verify        # Linux / macOS

&#x20; mvnw.cmd verify      # Windows

&#x20; ```

&#x20; El reporte queda en `target/site/jacoco/index.html`.



Las pruebas de integración usan una base H2 en memoria (`src/test/resources/application.properties`), así que no modifican la carpeta `data/`.




\### Organización de las pruebas



| Carpeta (`src/test/java/com/avistamientos/aves/`) | Nivel | Qué prueba |

|---|---|---|

| `validacion/` | Unitarias | `ValidadorDeAvistamiento`, con `RelojFijo` (stub) |

| `service/` | Unitarias | `AvistamientoService`, con un doble (Mockito) del repositorio |

| `integracion/` | Integración | Los endpoints por HTTP con MockMvc y H2 en memoria |



\## Estructura del proyecto



```

com.avistamientos.aves

├── AvesApplication.java        Clase principal

├── model/Avistamiento.java     Entidad

├── validacion/                 Validador, Reloj y manejo de errores 400

├── repository/                 Acceso a la base de datos (Spring Data JPA)

├── service/                    Lógica de negocio

└── controller/                 Endpoints REST y códigos de estado

```



\## Tecnologías



\- Java 17

\- Spring Boot (Spring Web, Spring Data JPA)

\- H2 Database (modo archivo)

\- JUnit 5, Mockito y MockMvc para las pruebas, JaCoCo para la cobertura

\- Maven (wrapper incluido)



\## Uso de IA



Para este trabajo usé \*\*Claude (Anthropic), modelo Sonnet 5.5\*\*, como apoyo para estructurar el proyecto, escribir el código base, preparar la documentación y guiar la escritura de las pruebas. Revisé el código y entiendo cómo funciona cada capa (controller, service, repository y model), el manejo de `Optional`, `ResponseEntity` y el validador.

