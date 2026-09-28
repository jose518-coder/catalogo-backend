# Catálogo Backend — Backend 05

API REST para la gestión de un catálogo de productos, desarrollada con Spring Boot 3.5 y Java 21. El proyecto incluye autenticación mediante JWT, persistencia con PostgreSQL, migraciones con Flyway, documentación con Swagger y configuración para distintos entornos.

## Tecnologías utilizadas

* Java 21
* Spring Boot 3.5
* Spring Data JPA
* Spring Security
* JWT
* PostgreSQL
* Flyway
* Swagger / OpenAPI
* Docker y Docker Compose
* Maven

## Requisitos previos

Para ejecutar el proyecto localmente se necesita:

* Java 21
* Maven
* PostgreSQL, si se ejecuta sin Docker
* Docker y Docker Compose, si se utiliza el entorno de contenedores

## Perfiles de configuración

La aplicación cuenta con los siguientes perfiles:

* `dev`: configuración para desarrollo local. Habilita la documentación Swagger y el registro de consultas SQL.
* `test`: configuración para ejecutar las pruebas automatizadas.
* `prod`: configuración para producción. Utiliza variables de entorno y deshabilita Swagger.

La configuración general se encuentra en `src/main/resources/application.properties`. Las configuraciones específicas están en los archivos `application-dev.properties`, `application-test.properties` y `application-prod.properties`.

## Variables de entorno

La aplicación utiliza las siguientes variables de entorno:

| Variable        | Descripción                                         |
| --------------- | --------------------------------------------------- |
| `DATABASE_URL`  | URL de conexión a PostgreSQL.                       |
| `DB_USERNAME`   | Usuario de la base de datos.                        |
| `DB_PASSWORD`   | Contraseña de la base de datos.                     |
| `JWT_SECRETO`   | Clave secreta utilizada para firmar los tokens JWT. |
| `CORS_ORIGENES` | Orígenes permitidos para las solicitudes CORS.      |
| `PORT`          | Puerto en el que se ejecuta la aplicación.          |

El archivo `.env.ejemplo` contiene una plantilla de las variables necesarias para el entorno local.

Se debe crear un archivo `.env` con los valores correspondientes. Este archivo no debe subirse al repositorio, ya que puede contener información sensible.

## Migraciones de base de datos

El esquema de la base de datos se administra mediante Flyway. Las migraciones se encuentran en `src/main/resources/db/migration`.

* `V1__esquema_inicial.sql`: crea las tablas iniciales de categorías y productos.
* `V2__usuarios_y_roles.sql`: crea las tablas y restricciones relacionadas con usuarios y roles.
* `V3__datos_semilla.sql`: inserta los datos iniciales.

Las migraciones se ejecutan automáticamente al iniciar la aplicación, cuando Flyway está habilitado.

Hibernate utiliza `ddl-auto=validate` para comprobar que el esquema existente coincida con las entidades, sin modificarlo.

**Importante:** las migraciones que ya se hayan aplicado no deben modificarse. Para realizar cambios en el esquema se debe crear una nueva migración.

## Ejecución local

1. Clonar el repositorio:

   ```bash
   git clone https://github.com/jose518-coder/catalogo-backend.git
   ```

2. Ingresar al directorio del proyecto:

   ```bash
   cd catalogo-backend
   ```

3. Configurar las variables de entorno necesarias y verificar la conexión con PostgreSQL.

4. Ejecutar la aplicación con el perfil de desarrollo:

   ```bash
   mvn spring-boot:run -Dspring-boot.run.profiles=dev
   ```

La API estará disponible en `http://localhost:8080`, siempre que el puerto configurado sea el 8080.

## Ejecución con Docker Compose

Docker Compose permite iniciar la API y PostgreSQL mediante los servicios definidos en `compose.yaml`.

1. Configurar las variables de entorno en el archivo `.env`.

2. Desde el directorio donde se encuentra `compose.yaml`, ejecutar:

   ```bash
   docker compose up --build
   ```

3. Para detener los servicios:

   ```bash
   docker compose down
   ```

Los datos de PostgreSQL se conservan mediante el volumen definido en Docker Compose.


## Pruebas automatizadas

Para ejecutar las pruebas del proyecto, utilizar:

```bash
mvn clean test
```

El resultado de las pruebas se puede consultar en la salida de Maven y en los informes generados dentro de `target/surefire-reports`.

## Despliegue

La aplicación se desplegará en una plataforma compatible con Spring Boot y PostgreSQL administrado.

* **Plataforma:** render.com
* **URL pública de la API:** 
* **Base de datos:** PostgreSQL administrado

En el entorno de producción se deben configurar las variables de entorno necesarias para la conexión a la base de datos, la autenticación JWT, CORS y el puerto de ejecución.

## Verificaciones del despliegue

Una vez desplegada la aplicación, se deben comprobar las siguientes operaciones:

* Registro de usuarios.
* Inicio de sesión y generación del token JWT.
* Consulta y creación de productos.
* Acceso a las rutas protegidas con autenticación.
* Restricciones de acceso según el rol del usuario.
* Persistencia de los datos después de reiniciar la aplicación.

También se debe verificar que un usuario con rol `USER` no pueda eliminar productos y reciba una respuesta HTTP 403.

## Solución de problemas

### Error de validación del esquema

Si Hibernate informa que el esquema de la base de datos no coincide con las entidades, se debe revisar que las migraciones de Flyway se hayan ejecutado correctamente y que la estructura de las tablas corresponda con las entidades del proyecto.

No se debe solucionar este error cambiando `ddl-auto` a `create` o `update` en producción.

### Swagger devuelve 404 en producción

Swagger está deshabilitado en el perfil `prod`. Por este motivo, sus rutas no están disponibles en el entorno de producción. La documentación se consulta en el perfil de desarrollo.

## Repositorio

[Catálogo Backend — GitHub](https://github.com/jose518-coder/catalogo-backend)
