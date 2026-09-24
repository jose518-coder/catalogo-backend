package com.wposs.catalogo.servicio;

import com.wposs.catalogo.modelo.Producto;
import com.wposs.catalogo.modelo.ProductoEstadisticas;
import com.wposs.catalogo.repositorio.CategoriaRepositorio;
import com.wposs.catalogo.repositorio.ProductoRepositorio;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class ProductoServicio {

    private final ProductoRepositorio repositorio;
    private final CategoriaRepositorio categoriaRepositorio;
    private final BigDecimal iva;

    public ProductoServicio(
            ProductoRepositorio repositorio,
            CategoriaRepositorio categoriaRepositorio,
            @Value("${catalogo.iva:0.19}") BigDecimal iva) {

        this.repositorio = repositorio;
        this.categoriaRepositorio = categoriaRepositorio;
        this.iva = iva;
    }

    @Transactional(readOnly = true)
    public List<Producto> buscarTodos() {

        List<Producto> productos = repositorio.findAll();

        // Acceso intencional a la relación LAZY.
        // Se utiliza para demostrar el comportamiento N+1.
        productos.forEach(producto ->
                producto.getCategoria().getNombre());

        return productos;
    }

    @Transactional(readOnly = true)
    public List<Producto> buscarTodosConCategoria() {
        return repositorio.buscarConCategoria();
    }

    @Transactional(readOnly = true)
    public List<Producto> buscarPorCategoria(String categoria) {
        return repositorio.findByCategoriaNombre(categoria);
    }

    @Transactional(readOnly = true)
    public Producto buscarPorId(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "Producto no encontrado: " + id));
    }

    @Transactional(readOnly = true)
    public List<Producto> buscarSinStock(Integer limite) {
        return repositorio.findByExistenciasLessThan(limite);
    }

    @Transactional
    public Producto guardar(Producto producto) {

        validarProducto(producto);

        validarCategoria(producto);

        return repositorio.save(producto);
    }

    @Transactional
    public Producto actualizar(Long id, Producto producto) {

        buscarPorId(id);

        validarProducto(producto);
        validarCategoria(producto);

        Producto productoActualizado = new Producto(
                id,
                producto.getTitulo(),
                producto.getPrecio(),
                producto.getCategoria(),
                producto.getExistencias()
        );

        return repositorio.save(productoActualizado);
    }

    @Transactional
    public void eliminar(Long id) {

        if (!repositorio.existsById(id)) {
            throw new NoSuchElementException(
                    "Producto no encontrado: " + id);
        }

        repositorio.deleteById(id);
    }

    @Transactional(readOnly = true)
    public ProductoEstadisticas obtenerEstadisticas() {

        List<Producto> productos = repositorio.buscarConCategoria();

        int totalProductos = productos.size();

        BigDecimal valorTotalInventario =
                productos.stream()
                        .map(producto ->
                                producto.getPrecio()
                                        .multiply(
                                                BigDecimal.valueOf(
                                                        producto.getExistencias()))
                                        .multiply(
                                                BigDecimal.ONE.add(iva)))
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add);

        Map<String, Integer> cantidadPorCategoria =
                productos.stream()
                        .collect(Collectors.groupingBy(
                                producto ->
                                        producto.getCategoria().getNombre(),
                                Collectors.summingInt(
                                        Producto::getExistencias)
                        ));

        return new ProductoEstadisticas(
                totalProductos,
                valorTotalInventario,
                cantidadPorCategoria
        );
    }

    @Transactional
    public void transferirExistencias(
            Long origen,
            Long destino,
            int cantidad) {

        if (cantidad <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad debe ser mayor que 0");
        }

        Producto productoOrigen = buscarPorId(origen);
        Producto productoDestino = buscarPorId(destino);

        int nuevasExistenciasOrigen =
                productoOrigen.getExistencias() - cantidad;

        if (nuevasExistenciasOrigen < 0) {
            throw new IllegalArgumentException(
                    "El producto origen no tiene existencias suficientes");
        }

        productoOrigen.setExistencias(nuevasExistenciasOrigen);

        productoDestino.setExistencias(
                productoDestino.getExistencias() + cantidad
        );
    }

    @Transactional
    public void actualizarPrecioSinSave(
            Long id,
            BigDecimal nuevoPrecio) {

        Producto producto = buscarPorId(id);

        producto.setPrecio(nuevoPrecio);

        // No se llama a save().
        // Hibernate debe detectar el cambio mediante dirty checking.
    }

    private void validarProducto(Producto producto) {

        if (producto.getPrecio() == null
                || producto.getPrecio().signum() <= 0) {

            throw new IllegalArgumentException(
                    "El precio debe ser mayor que 0");
        }

        if (producto.getTitulo() == null
                || producto.getTitulo().isBlank()) {

            throw new IllegalArgumentException(
                    "El título no puede estar vacío");
        }

        if (producto.getExistencias() == null
                || producto.getExistencias() < 0) {

            throw new IllegalArgumentException(
                    "Las existencias no pueden ser negativas");
        }
    }

    private void validarCategoria(Producto producto) {

        if (producto.getCategoria() == null
                || producto.getCategoria().getId() == null) {

            throw new IllegalArgumentException(
                    "La categoría es obligatoria");
        }

        categoriaRepositorio.findById(
                        producto.getCategoria().getId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "La categoría no existe"));
    }
}