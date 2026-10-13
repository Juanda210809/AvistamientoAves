# API REST de avistamientos de aves

Trabajo en clase de Ingeniería de Software 2 (ISW-2), Sesión 17.
API REST para registrar avistamientos de aves observados en el campo, hecha con Spring Boot, Spring Data JPA y una base de datos H2 embebida.

**Autor:** TU NOMBRE COMPLETO

## Requisitos

- Java 17 o superior
- Git (para clonar el repositorio)
- `curl` (viene instalado en Windows 10/11, Linux y macOS)

No necesitas instalar Maven (el proyecto trae el wrapper `mvnw`) ni ninguna base de datos (H2 se guarda en un archivo local).

## Instalación y ejecución

```bash
git clone https://github.com/Juanda210809/AvistamientoAves.git
cd AvistamientoAves
```

Ejecutar la API:

- **Linux / macOS:**
  ```bash
  ./mvnw spring-boot:run
  ```
- **Windows (CMD o PowerShell):**
  ```bash
  mvnw.cmd spring-boot:run
  ```

Cuando en la consola aparezca `Started AvesApplication`, la API está disponible en `http://localhost:8080`.

Los datos se guardan en la carpeta `data/`, que se crea sola la primera vez. La base de datos empieza vacía y cada máquina tiene su propia copia.

## Modelo

Cada avistamiento tiene:

| Campo       | Tipo   | Descripción                                          |
|-------------|--------|------------------------------------------------------|
| `id`        | número | Lo asigna el sistema                                 |
| `especie`   | texto  | Nombre de la especie (por ejemplo "colibri")         |
| `lugar`     | texto  | Dónde se observó                                     |
| `fecha`     | texto  | Formato `AAAA-MM-DD`                                 |
| `observador`| texto  | Quién registró el avistamiento                       |

Todos los campos son obligatorios al crear o actualizar. Si falta alguno, o la fecha no tiene el formato correcto, la API responde `400`.

## Endpoints

| Operación                  | Petición                       | Respuesta                                  |
|----------------------------|--------------------------------|--------------------------------------------|
| Listar avistamientos       | `GET /avistamientos`           | `200` con la lista en JSON                 |
| Ver un avistamiento        | `GET /avistamientos/{id}`      | `200` con el avistamiento, o `404`         |
| Registrar un avistamiento  | `POST /avistamientos`          | `201` con el avistamiento creado, o `400`  |
| Actualizar un avistamiento | `PUT /avistamientos/{id}`      | `200` con el avistamiento, o `404` (`400` si los datos son inválidos) |
| Eliminar un avistamiento   | `DELETE /avistamientos/{id}`   | `204` sin contenido, o `404`               |
| Resumen por especie (bonus)| `GET /avistamientos/resumen`   | `200` con el conteo por especie            |

## Ejemplos con curl

La opción `-i` muestra el código de estado en la primera línea de la respuesta. Los ejemplos usan comillas dobles escapadas para que funcionen en Windows, Linux y macOS.

### Registrar un avistamiento (201)

```bash
curl -i -X POST http://localhost:8080/avistamientos -H "Content-Type: application/json" -d "{\"especie\":\"colibri\",\"lugar\":\"Chingaza\",\"fecha\":\"2026-09-29\",\"observador\":\"Ana\"}"
```

### Registrar con datos incompletos (400)

```bash
curl -i -X POST http://localhost:8080/avistamientos -H "Content-Type: application/json" -d "{\"especie\":\"colibri\"}"
```

### Listar todos (200)

```bash
curl -i http://localhost:8080/avistamientos
```

### Ver uno por id (200, o 404 si no existe)

```bash
curl -i http://localhost:8080/avistamientos/1
curl -i http://localhost:8080/avistamientos/999
```

### Actualizar (200, o 404 si no existe)

```bash
curl -i -X PUT http://localhost:8080/avistamientos/1 -H "Content-Type: application/json" -d "{\"especie\":\"colibri\",\"lugar\":\"Sumapaz\",\"fecha\":\"2026-09-30\",\"observador\":\"Ana\"}"
```

### Eliminar (204, o 404 si no existe)

```bash
curl -i -X DELETE http://localhost:8080/avistamientos/1
```

### Resumen por especie (200)

```bash
curl -i http://localhost:8080/avistamientos/resumen
```

Respuesta de ejemplo:

```json
{"colibri": 2, "aguila": 1}
```

## Estructura del proyecto

```
com.avistamientos.aves
├── AvesApplication.java        Clase principal
├── model/Avistamiento.java     Entidad y validaciones
├── repository/                 Acceso a la base de datos (Spring Data JPA)
├── service/                    Lógica de negocio
└── controller/                 Endpoints REST y códigos de estado
```

## Tecnologías

- Java 17
- Spring Boot (Spring Web, Spring Data JPA, Validation)
- H2 Database (modo archivo)
- Maven (wrapper incluido)

## Uso de IA

Para este trabajo usé **Claude (Anthropic), modelo Sonnet 5.5**, como apoyo para estructurar el proyecto, escribir el código base y preparar la documentación. Revisé el código y entiendo cómo funciona cada capa (controller, service, repository y model), el manejo de `Optional`, `ResponseEntity` y la validación con `@Valid`.
