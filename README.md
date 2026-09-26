# Catálogo Backend — Backend 04

En este módulo se implementó la autenticación y autorización mediante Spring Security y JWT.

## Tecnologías

* Java 21
* Spring Boot 3.5.11
* Spring Security
* JWT
* BCrypt
* Spring Data JPA
* PostgreSQL
* H2 para pruebas
* Maven
* JUnit 5
* MockMvc

## Objetivos del módulo

* Autenticación mediante JWT.
* Registro e inicio de sesión.
* Consulta del perfil autenticado.
* Contraseñas protegidas con BCrypt.
* Autorización basada en roles.
* Configuración de seguridad sin estado (stateless).
* Protección de endpoints.
* Manejo de errores HTTP 401 y 403.
* Configuración de CORS.
* Pruebas automatizadas.

## Endpoints de autenticación

| Método | Endpoint             | Descripción       | Acceso      |
| ------ | -------------------- | ----------------- | ----------- |
| POST   | `/api/auth/registro` | Registrar usuario | Público     |
| POST   | `/api/auth/login`    | Iniciar sesión    | Público     |
| GET    | `/api/auth/yo`       | Consultar perfil  | Autenticado |

## Roles y permisos

| Rol    | Permisos                                     |
| ------ | -------------------------------------------- |
| USER   | Consultar productos y categorías públicas    |
| EDITOR | Consultar, crear y modificar productos       |
| ADMIN  | Administrar productos, categorías y usuarios |

El registro público asigna automáticamente el rol `USER`. No se permite asignar privilegios mediante el cuerpo de la solicitud de registro.

## Seguridad JWT

La API utiliza tokens JWT para identificar a los usuarios autenticados.

El secreto JWT se configura mediante la variable de entorno `JWT_SECRETO`. No debe incluirse el secreto real en el repositorio.

La duración del token es de una hora.

Las sesiones se configuran como `STATELESS`, por lo que el servidor no mantiene sesiones HTTP tradicionales.

## Respuestas de seguridad

* `401 Unauthorized`: la solicitud requiere autenticación o las credenciales no son válidas.
* `403 Forbidden`: el usuario está autenticado, pero no tiene permisos suficientes.
* `409 Conflict`: el usuario que se intenta registrar ya existe.

## Ejecutar la aplicación

Configurar la variable de entorno `JWT_SECRETO` y ejecutar:

```powershell
.\mvnw.cmd spring-boot:run
```

## Ejecutar las pruebas

Para ejecutar todas las pruebas:

```powershell
.\mvnw.cmd clean test
```

