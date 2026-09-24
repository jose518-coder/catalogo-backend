# Catálogo Backend — Backend 02

Persistencia con Spring Data JPA, Hibernate y PostgreSQL.

## Tecnologías

- Java 21
- Spring Boot 3.5.11
- Spring Data JPA
- Hibernate
- PostgreSQL 16
- H2 para pruebas

## Base de datos

PostgreSQL se ejecuta mediante Docker:

```powershell

docker run -d --name catalogo-db -p 5432:5432 -e POSTGRES_PASSWORD=catalogo -e POSTGRES_USER=catalogo -e POSTGRES_DB=catalogo postgres:16-alpine