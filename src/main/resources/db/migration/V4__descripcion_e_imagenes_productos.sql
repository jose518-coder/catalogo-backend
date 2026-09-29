ALTER TABLE productos
    ADD COLUMN descripcion VARCHAR(500) NOT NULL DEFAULT '';

UPDATE productos
SET descripcion = 'Descripción del producto ' || titulo || '.'
WHERE descripcion = '';

CREATE TABLE producto_imagenes (
    producto_id BIGINT NOT NULL,
    orden INTEGER NOT NULL,
    imagen_url VARCHAR(2048) NOT NULL,

    CONSTRAINT pk_producto_imagenes
        PRIMARY KEY (producto_id, orden),
    CONSTRAINT fk_producto_imagenes_productos
        FOREIGN KEY (producto_id)
        REFERENCES productos(id)
        ON DELETE CASCADE
);
