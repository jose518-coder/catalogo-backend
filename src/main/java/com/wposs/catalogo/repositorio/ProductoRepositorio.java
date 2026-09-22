package com.wposs.catalogo.repositorio;

import com.wposs.catalogo.modelo.Producto;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ProductoRepositorio {

    private final ConcurrentHashMap<Long, Producto> productos = new ConcurrentHashMap<>();
    private final AtomicLong siguienteId = new AtomicLong(1);

    public ProductoRepositorio() {
        guardar(new Producto(null, "Clean Code", new BigDecimal("45.90"), "libros", 10));
        guardar(new Producto(null, "Java: The Complete Reference", new BigDecimal("60.50"), "libros", 8));
        guardar(new Producto(null, "Spring Boot en Acción", new BigDecimal("52.75"), "libros", 12));

        guardar(new Producto(null, "Teclado mecánico", new BigDecimal("120.00"), "tecnologia", 15));
        guardar(new Producto(null, "Mouse inalámbrico", new BigDecimal("75.50"), "tecnologia", 20));
        guardar(new Producto(null, "Monitor 24 pulgadas", new BigDecimal("650.00"), "tecnologia", 6));

        guardar(new Producto(null, "Cuaderno universitario", new BigDecimal("12.90"), "papeleria", 30));
        guardar(new Producto(null, "Lapicero azul", new BigDecimal("3.50"), "papeleria", 50));
    }

    public List<Producto> buscarTodos() {
        return new ArrayList<>(productos.values());
    }

    public List<Producto> buscarPorCategoria(String categoria) {
        return productos.values()
                .stream()
                .filter(producto -> producto.categoria().equalsIgnoreCase(categoria))
                .toList();
    }

    public Optional<Producto> buscarPorId(Long id) {
        return Optional.ofNullable(productos.get(id));
    }

    public Producto guardar(Producto producto) {
        Long id = producto.id();

        if (id == null) {
            id = siguienteId.getAndIncrement();
        }

        Producto productoConId = new Producto(
                id,
                producto.titulo(),
                producto.precio(),
                producto.categoria(),
                producto.existencias()
        );

        productos.put(id, productoConId);

        return productoConId;
    }

    public boolean eliminar(Long id) {
        return productos.remove(id) != null;
    }
}