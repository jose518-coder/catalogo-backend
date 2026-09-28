INSERT INTO categorias (nombre, descripcion)
VALUES
    ('libros', NULL),
    ('tecnologia', NULL),
    ('hogar', NULL),
    ('oficina', NULL);

INSERT INTO productos (titulo, precio, categoria_id, existencias)
VALUES
    ('Clean Code', 45.90,
        (SELECT id FROM categorias WHERE nombre = 'libros'), 10),
    ('Effective Java', 55.90,
        (SELECT id FROM categorias WHERE nombre = 'libros'), 8),
    ('Spring Boot', 60.00,
        (SELECT id FROM categorias WHERE nombre = 'libros'), 6),
    ('Java 21', 50.00,
        (SELECT id FROM categorias WHERE nombre = 'libros'), 4),
    ('Angular 17', 48.50,
        (SELECT id FROM categorias WHERE nombre = 'libros'), 5),

    ('Laptop Lenovo', 2500.00,
        (SELECT id FROM categorias WHERE nombre = 'tecnologia'), 5),
    ('Mouse Logitech', 80.00,
        (SELECT id FROM categorias WHERE nombre = 'tecnologia'), 15),
    ('Teclado Mecánico', 220.00,
        (SELECT id FROM categorias WHERE nombre = 'tecnologia'), 10),
    ('Monitor 24', 700.00,
        (SELECT id FROM categorias WHERE nombre = 'tecnologia'), 7),
    ('Webcam HD', 180.00,
        (SELECT id FROM categorias WHERE nombre = 'tecnologia'), 3),

    ('Lámpara LED', 75.00,
        (SELECT id FROM categorias WHERE nombre = 'hogar'), 12),
    ('Silla', 450.00,
        (SELECT id FROM categorias WHERE nombre = 'hogar'), 6),
    ('Escritorio', 900.00,
        (SELECT id FROM categorias WHERE nombre = 'hogar'), 4),
    ('Cafetera', 300.00,
        (SELECT id FROM categorias WHERE nombre = 'hogar'), 2),
    ('Organizador', 50.00,
        (SELECT id FROM categorias WHERE nombre = 'hogar'), 8),

    ('Cuaderno', 15.00,
        (SELECT id FROM categorias WHERE nombre = 'oficina'), 20),
    ('Bolígrafos', 10.00,
        (SELECT id FROM categorias WHERE nombre = 'oficina'), 30),
    ('Archivador', 25.00,
        (SELECT id FROM categorias WHERE nombre = 'oficina'), 10),
    ('Carpeta', 8.00,
        (SELECT id FROM categorias WHERE nombre = 'oficina'), 25),
    ('Grapadora', 20.00,
        (SELECT id FROM categorias WHERE nombre = 'oficina'), 7);