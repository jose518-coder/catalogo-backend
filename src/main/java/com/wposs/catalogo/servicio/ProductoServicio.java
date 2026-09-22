package com.wposs.catalogo.servicio;

import com.wposs.catalogo.modelo.Producto;
import com.wposs.catalogo.modelo.ProductoEstadisticas;
import com.wposs.catalogo.repositorio.ProductoRepositorio;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class ProductoServicio {

    private final ProductoRepositorio repositorio;
    private final BigDecimal iva;

    public ProductoServicio(
            ProductoRepositorio repositorio,
            @Value("${catalogo.iva:0.19}") BigDecimal iva) {

        this.repositorio = repositorio;
        this.iva = iva;
    }

    public List<Producto> buscarTodos() {
        return repositorio.buscarTodos();
    }

    public List<Producto> buscarPorCategoria(String categoria) {
        return repositorio.buscarPorCategoria(categoria);
    }

    public Producto buscarPorId(Long id) {
        return repositorio.buscarPorId(id)
                .orElseThrow(() -> new NoSuchElementException(
                        "Producto no encontrado: " + id
                ));
    }

    public Producto guardar(Producto producto) {
        validarProducto(producto);
        return repositorio.guardar(producto);
    }

    public Producto actualizar(Long id, Producto producto) {
        buscarPorId(id);
        validarProducto(producto);

        Producto productoActualizado = new Producto(
                id,
                producto.titulo(),
                producto.precio(),
                producto.categoria(),
                producto.existencias()
        );

        return repositorio.guardar(productoActualizado);
    }

    public void eliminar(Long id) {
        if (!repositorio.eliminar(id)) {
            throw new NoSuchElementException(
                    "Producto no encontrado: " + id
            );
        }
    }

    public ProductoEstadisticas obtenerEstadisticas() {

        List<Producto> productos = repositorio.buscarTodos();

        int totalProductos = productos.size();

        BigDecimal valorTotalInventario = productos.stream()
                .map(producto -> producto.precio()
                        .multiply(
                                BigDecimal.valueOf(
                                        producto.existencias()
                                )
                        )
                        .multiply(BigDecimal.ONE.add(iva))
                )
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Integer> cantidadPorCategoria =
                productos.stream()
                        .collect(Collectors.groupingBy(
                                Producto::categoria,
                                Collectors.summingInt(
                                        Producto::existencias
                                )
                        ));

        return new ProductoEstadisticas(
                totalProductos,
                valorTotalInventario,
                cantidadPorCategoria
        );
    }

    private void validarProducto(Producto producto) {

        if (producto.precio() == null ||
                producto.precio().signum() <= 0) {

            throw new IllegalArgumentException(
                    "El precio debe ser mayor que 0"
            );
        }

        if (producto.titulo() == null ||
                producto.titulo().isBlank()) {

            throw new IllegalArgumentException(
                    "El título no puede estar vacío"
            );
        }

        if (producto.existencias() == null ||
                producto.existencias() < 0) {

            throw new IllegalArgumentException(
                    "Las existencias no pueden ser negativas"
            );
        }
    }
}