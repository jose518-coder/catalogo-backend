package com.wposs.catalogo.repositorio;

import com.wposs.catalogo.modelo.Categoria;
import com.wposs.catalogo.modelo.Producto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ProductoRepositorioTest {

    @Autowired
    private ProductoRepositorio productoRepositorio;

    @Autowired
    private CategoriaRepositorio categoriaRepositorio;

    @Test
    void debeGuardarPrecioConDosDecimales() {

        Categoria categoria =
                categoriaRepositorio.save(
                        new Categoria("libros"));

        Producto producto =
                new Producto(
                        "Clean Code",
                        new BigDecimal("1999.99"),
                        categoria,
                        10);

        Producto guardado =
                productoRepositorio.save(producto);

        Producto recuperado =
                productoRepositorio.findById(
                        guardado.getId()).orElseThrow();

        assertThat(recuperado.getPrecio())
                .isEqualByComparingTo("1999.99");
    }

    @Test
    void debeFiltrarPorCategoria() {

        Categoria libros =
                categoriaRepositorio.save(
                        new Categoria("libros"));

        Categoria tecnologia =
                categoriaRepositorio.save(
                        new Categoria("tecnologia"));

        productoRepositorio.save(
                new Producto(
                        "Clean Code",
                        new BigDecimal("45.90"),
                        libros,
                        10));

        productoRepositorio.save(
                new Producto(
                        "Laptop",
                        new BigDecimal("2500.00"),
                        tecnologia,
                        5));

        List<Producto> resultado =
                productoRepositorio
                        .findByCategoriaNombre("libros");

        assertThat(resultado)
                .hasSize(1);

        assertThat(resultado.get(0).getTitulo())
                .isEqualTo("Clean Code");
    }

    @Test
    void debeIgnorarMayusculasEnTitulo() {

        Categoria libros =
                categoriaRepositorio.save(
                        new Categoria("libros"));

        productoRepositorio.save(
                new Producto(
                        "Clean Code",
                        new BigDecimal("45.90"),
                        libros,
                        10));

        Optional<Producto> resultado =
                productoRepositorio
                        .findByTituloIgnoreCase(
                                "CLEAN CODE");

        assertThat(resultado)
                .isPresent();
    }

    @Test
    void joinFetchDebeTraerCategoria() {

        Categoria libros =
                categoriaRepositorio.save(
                        new Categoria("libros"));

        productoRepositorio.save(
                new Producto(
                        "Clean Code",
                        new BigDecimal("45.90"),
                        libros,
                        10));

        List<Producto> resultado =
                productoRepositorio
                        .buscarConCategoria();

        assertThat(resultado)
                .hasSize(1);

        assertThat(resultado.get(0)
                .getCategoria()
                .getNombre())
                .isEqualTo("libros");
    }
}