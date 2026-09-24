package com.wposs.catalogo.repositorio;

import com.wposs.catalogo.modelo.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductoRepositorio
        extends JpaRepository<Producto, Long> {

    List<Producto> findByCategoriaNombre(String nombre);

    List<Producto> findByPrecioBetween(
            BigDecimal minimo,
            BigDecimal maximo);

    Optional<Producto> findByTituloIgnoreCase(String titulo);

    List<Producto> findByExistenciasLessThan(Integer limite);

    @Query("""
        SELECT p
        FROM Producto p
        JOIN FETCH p.categoria
        """)
    List<Producto> buscarConCategoria();

    @Query("""
        SELECT p.categoria.nombre AS categoria,
               COUNT(p) AS cantidad
        FROM Producto p
        GROUP BY p.categoria.nombre
        """)
    List<Object[]> contarPorCategoria();
}