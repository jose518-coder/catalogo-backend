package com.wposs.catalogo;

import com.wposs.catalogo.modelo.Categoria;
import com.wposs.catalogo.modelo.Producto;
import com.wposs.catalogo.repositorio.CategoriaRepositorio;
import com.wposs.catalogo.repositorio.ProductoRepositorio;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;

@Configuration
@Profile("dev")
public class CatalogoDataLoader {

    @Bean
    CommandLineRunner cargarDatos(
            CategoriaRepositorio categoriaRepositorio,
            ProductoRepositorio productoRepositorio) {

        return args -> {

            if (categoriaRepositorio.count() > 0) {
                return;
            }

            Categoria libros =
                    categoriaRepositorio.save(
                            new Categoria("libros"));

            Categoria tecnologia =
                    categoriaRepositorio.save(
                            new Categoria("tecnologia"));

            Categoria hogar =
                    categoriaRepositorio.save(
                            new Categoria("hogar"));

            Categoria oficina =
                    categoriaRepositorio.save(
                            new Categoria("oficina"));

            productoRepositorio.save(
                    new Producto(
                            "Clean Code",
                            new BigDecimal("45.90"),
                            libros,
                            10));

            productoRepositorio.save(
                    new Producto(
                            "Effective Java",
                            new BigDecimal("55.90"),
                            libros,
                            8));

            productoRepositorio.save(
                    new Producto(
                            "Spring Boot",
                            new BigDecimal("60.00"),
                            libros,
                            6));

            productoRepositorio.save(
                    new Producto(
                            "Java 21",
                            new BigDecimal("50.00"),
                            libros,
                            4));

            productoRepositorio.save(
                    new Producto(
                            "Angular 17",
                            new BigDecimal("48.50"),
                            libros,
                            5));

            productoRepositorio.save(
                    new Producto(
                            "Laptop Lenovo",
                            new BigDecimal("2500.00"),
                            tecnologia,
                            5));

            productoRepositorio.save(
                    new Producto(
                            "Mouse Logitech",
                            new BigDecimal("80.00"),
                            tecnologia,
                            15));

            productoRepositorio.save(
                    new Producto(
                            "Teclado Mecánico",
                            new BigDecimal("220.00"),
                            tecnologia,
                            10));

            productoRepositorio.save(
                    new Producto(
                            "Monitor 24",
                            new BigDecimal("700.00"),
                            tecnologia,
                            7));

            productoRepositorio.save(
                    new Producto(
                            "Webcam HD",
                            new BigDecimal("180.00"),
                            tecnologia,
                            3));

            productoRepositorio.save(
                    new Producto(
                            "Lámpara LED",
                            new BigDecimal("75.00"),
                            hogar,
                            12));

            productoRepositorio.save(
                    new Producto(
                            "Silla",
                            new BigDecimal("450.00"),
                            hogar,
                            6));

            productoRepositorio.save(
                    new Producto(
                            "Escritorio",
                            new BigDecimal("900.00"),
                            hogar,
                            4));

            productoRepositorio.save(
                    new Producto(
                            "Cafetera",
                            new BigDecimal("300.00"),
                            hogar,
                            2));

            productoRepositorio.save(
                    new Producto(
                            "Organizador",
                            new BigDecimal("50.00"),
                            hogar,
                            8));

            productoRepositorio.save(
                    new Producto(
                            "Cuaderno",
                            new BigDecimal("15.00"),
                            oficina,
                            20));

            productoRepositorio.save(
                    new Producto(
                            "Bolígrafos",
                            new BigDecimal("10.00"),
                            oficina,
                            30));

            productoRepositorio.save(
                    new Producto(
                            "Archivador",
                            new BigDecimal("25.00"),
                            oficina,
                            10));

            productoRepositorio.save(
                    new Producto(
                            "Carpeta",
                            new BigDecimal("8.00"),
                            oficina,
                            25));

            productoRepositorio.save(
                    new Producto(
                            "Grapadora",
                            new BigDecimal("20.00"),
                            oficina,
                            7));
        };
    }
}