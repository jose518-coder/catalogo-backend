package com.wposs.catalogo.servicio;

import com.wposs.catalogo.modelo.Categoria;
import com.wposs.catalogo.modelo.Producto;
import com.wposs.catalogo.dto.ProductoResumen;

import com.wposs.catalogo.repositorio.CategoriaRepositorio;
import com.wposs.catalogo.repositorio.ProductoRepositorio;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
class ProductoServicioTest {

    @Autowired
    private ProductoServicio servicio;

    @Autowired
    private ProductoRepositorio productoRepositorio;

    @Autowired
    private CategoriaRepositorio categoriaRepositorio;

    @Test
    void transferenciaInvalidaNoModificaProductos() {

        Categoria categoria =
                categoriaRepositorio.save(
                        new Categoria("tecnologia"));

        Producto origen =
                productoRepositorio.save(
                        new Producto(
                                "Origen",
                                new BigDecimal("100.00"),
                                categoria,
                                5));

        Producto destino =
                productoRepositorio.save(
                        new Producto(
                                "Destino",
                                new BigDecimal("100.00"),
                                categoria,
                                10));

        assertThatThrownBy(() ->
                servicio.transferirExistencias(
                        origen.getId(),
                        destino.getId(),
                        10))
                .isInstanceOf(IllegalArgumentException.class);

        Producto origenActual =
                productoRepositorio
                        .findById(origen.getId())
                        .orElseThrow();

        Producto destinoActual =
                productoRepositorio
                        .findById(destino.getId())
                        .orElseThrow();

        assertThat(origenActual.getExistencias())
                .isEqualTo(5);

        assertThat(destinoActual.getExistencias())
                .isEqualTo(10);
    }

    @Test
    @Transactional
    void dirtyCheckingActualizaPrecio() {

        Categoria categoria =
                categoriaRepositorio.save(
                        new Categoria("libros"));

        Producto producto =
                productoRepositorio.save(
                        new Producto(
                                "Libro",
                                new BigDecimal("100.00"),
                                categoria,
                                5));

        servicio.actualizarPrecioSinSave(
                producto.getId(),
                new BigDecimal("150.00"));

        Producto actualizado =
                productoRepositorio
                        .findById(producto.getId())
                        .orElseThrow();

        assertThat(actualizado.getPrecio())
                .isEqualByComparingTo("150.00");
    }

    @Test
    void listarDevuelveImagenesTrasCerrarLaTransaccion() {
        Categoria categoria = categoriaRepositorio.save(
                new Categoria("fotografia"));
        Producto guardado = productoRepositorio.saveAndFlush(
                new Producto(
                        "Camara compacta",
                        new BigDecimal("125.00"),
                        categoria,
                        3,
                        "Cámara compacta para uso diario.",
                        List.of("https://ejemplo.com/camara.jpg")
                )
        );

        ProductoResumen resumen = servicio.buscarTodos().stream()
                .filter(producto -> producto.id().equals(guardado.getId()))
                .findFirst()
                .orElseThrow();

        assertThat(resumen.imagenes())
                .containsExactly("https://ejemplo.com/camara.jpg");
    }
}
