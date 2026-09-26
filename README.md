# Catálogo Backend — Backend 03

En este módulo se implementan DTOs para separar las entidades JPA de los datos expuestos por la API, validaciones mediante Bean Validation, reglas de negocio y un manejo global de errores mediante `@RestControllerAdvice`.

## Tecnologías

- Java 21
- Spring Boot 3.5.11
- Spring Web
- Spring Data JPA
- Hibernate
- Spring Validation
- PostgreSQL
- H2 para pruebas
- Maven
- JUnit 5
- MockMvc

## Objetivos del módulo

En este módulo se implementó:

- DTOs de entrada y salida.
- Separación entre entidades JPA y respuestas de la API.
- Validaciones con Bean Validation.
- Validación de parámetros de consulta.
- Reglas de negocio.
- Excepciones personalizadas.
- Manejo global de errores.
- Respuestas de error estandarizadas.
- Mapeo entre entidades y DTOs.
- Pruebas automatizadas con MockMvc.
- Demostración del comportamiento de `@Transactional`.
- Demostración de la diferencia entre entidades y DTOs.

## DTOs

Los DTOs se encuentran en:

```text
src/main/java/com/wposs/catalogo/dto
```

## Demo 1 — Transacciones y rollback

Se realizó una demostración para comprobar el comportamiento de `@Transactional`.

La prueba utilizó dos escenarios.

### Excepción checked

Se modificó el precio de un producto:

```text
100.00 → 999.99
```

Después se lanzó una excepción checked:

```java
throw new Exception("Excepción checked de demostración");
```

Resultado:

```text
El cambio NO fue revertido.
```

El precio permaneció en:

```text
999.99
```

### RuntimeException

Se modificó el precio:

```text
100.00 → 888.88
```

Después se lanzó:

```java
throw new RuntimeException("RuntimeException de demostración");
```

Resultado:

```text
La transacción realizó rollback.
```

El precio volvió a:

```text
100.00
```

### Prueba ejecutada

Resultado:

```text
Tests run: 2
Failures: 0
Errors: 0
Skipped: 0

BUILD SUCCESS
```

## Demo 2 — Entidad vs DTO

Se realizó una demostración para comprobar la diferencia entre serializar directamente una entidad JPA y serializar un DTO.

Para la demostración se agregó temporalmente el campo:

```text
costoProveedor
```

a la entidad `Producto`.

Se asignó:

```text
1800.00
```

### Resultado de la entidad

Al serializar directamente la entidad, el campo apareció:

```json
{
  "id": 1,
  "titulo": "Laptop Demo",
  "precio": 2500.00,
  "categoria": {
    "id": 1,
    "nombre": "Tecnología",
    "descripcion": "Productos tecnológicos",
    "productos": []
  },
  "existencias": 5,
  "costoProveedor": 1800.00
}
```

### Resultado del DTO

Al serializar `ProductoDetalle`:

```json
{
  "id": 1,
  "titulo": "Laptop Demo",
  "precio": 2500.00,
  "existencias": 5,
  "categoria": "Tecnología"
}
```

El campo:

```text
costoProveedor
```

no apareció porque no forma parte del DTO.

### Prueba ejecutada

Resultado:

```text
Tests run: 1
Failures: 0
Errors: 0
Skipped: 0

BUILD SUCCESS
```


## Ejecutar pruebas

Para ejecutar todas las pruebas:

```powershell
.\mvnw.cmd clean test
```

Para ejecutar las pruebas del controlador:

```powershell
.\mvnw.cmd -Dtest=ProductoControladorTest test
```

Para ejecutar las pruebas del repositorio:

```powershell
.\mvnw.cmd -Dtest=ProductoRepositorioTest test
```

Para ejecutar las pruebas del servicio:

```powershell
.\mvnw.cmd -Dtest=ProductoServicioTest test
```


## Estructura del módulo

```text
src/
├── main/
│   ├── java/
│   │   └── com/wposs/catalogo/
│   │       ├── controlador/
│   │       │   ├── CategoriaControlador.java
│   │       │   ├── ManejoErrores.java
│   │       │   └── ProductoControlador.java
│   │       │
│   │       ├── dto/
│   │       │   ├── CategoriaDetalle.java
│   │       │   ├── CategoriaNueva.java
│   │       │   ├── ErrorRespuesta.java
│   │       │   ├── EstadisticasCatalogo.java
│   │       │   ├── ProductoActualizar.java
│   │       │   ├── ProductoDetalle.java
│   │       │   ├── ProductoNuevo.java
│   │       │   └── ProductoResumen.java
│   │       │
│   │       ├── excepcion/
│   │       │   ├── RecursoDuplicadoException.java
│   │       │   ├── RecursoNoEncontradoException.java
│   │       │   └── ReglaDeNegocioException.java
│   │       │
│   │       ├── mapper/
│   │       │   ├── CategoriaMapper.java
│   │       │   └── ProductoMapper.java
│   │       │
│   │       ├── modelo/
│   │       │   ├── Categoria.java
│   │       │   └── Producto.java
│   │       │
│   │       ├── repositorio/
│   │       │   ├── CategoriaRepositorio.java
│   │       │   └── ProductoRepositorio.java
│   │       │
│   │       ├── servicio/
│   │       │   ├── CategoriaServicio.java
│   │       │   └── ProductoServicio.java
│   │       │
│   │       └── CatalogoApplication.java
│   │
│   └── resources/
│       └── application.properties
│
└── test/
    ├── java/
    │   └── com/wposs/catalogo/
    │       ├── controlador/
    │       │   └── ProductoControladorTest.java
    │       ├── repositorio/
    │       │   └── ProductoRepositorioTest.java
    │       ├── servicio/
    │       │   └── ProductoServicioTest.java
    │       └── CatalogoApplicationTests.java
    │
    └── resources/
        └── application-test.properties
```

## Verificación final

Para comprobar el estado definitivo del módulo:

```powershell
.\mvnw.cmd clean test
```

Resultado esperado:

```text
Tests run: 17
Failures: 0
Errors: 0
Skipped: 0

BUILD SUCCESS
```
