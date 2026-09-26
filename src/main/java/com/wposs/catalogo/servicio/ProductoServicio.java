package com.wposs.catalogo.servicio;

import com.wposs.catalogo.dto.*;
import com.wposs.catalogo.excepcion.RecursoDuplicadoException;
import com.wposs.catalogo.excepcion.RecursoNoEncontradoException;
import com.wposs.catalogo.mapper.ProductoMapper;
import com.wposs.catalogo.modelo.Categoria;
import com.wposs.catalogo.modelo.Producto;
import com.wposs.catalogo.repositorio.CategoriaRepositorio;
import com.wposs.catalogo.repositorio.ProductoRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductoServicio {

    private final ProductoRepositorio productoRepositorio;
    private final CategoriaRepositorio categoriaRepositorio;
    private final ProductoMapper productoMapper;

    public ProductoServicio(
            ProductoRepositorio productoRepositorio,
            CategoriaRepositorio categoriaRepositorio,
            ProductoMapper productoMapper
    ) {
        this.productoRepositorio = productoRepositorio;
        this.categoriaRepositorio = categoriaRepositorio;
        this.productoMapper = productoMapper;
    }

    @Transactional(readOnly = true)
    public List<ProductoResumen> buscarTodos() {
        return productoRepositorio.findAll()
                .stream()
                .map(productoMapper::aResumen)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductoDetalle buscarPorId(Long id) {
        Producto producto = productoRepositorio.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "No existe el producto con id " + id
                        )
                );

        producto.getCategoria().getNombre();

        return productoMapper.aDetalle(producto);
    }

    @Transactional
    public ProductoDetalle guardar(ProductoNuevo dto) {

        if (productoRepositorio
                .findByTituloIgnoreCase(dto.titulo())
                .isPresent()) {

            throw new RecursoDuplicadoException(
                    "Ya existe un producto con el título: " + dto.titulo()
            );
        }

        Categoria categoria = categoriaRepositorio
                .findById(dto.categoriaId())
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "No existe la categoría con id "
                                        + dto.categoriaId(),
                                400
                        )
                );

        Producto producto = productoMapper.aEntidad(dto, categoria);

        Producto guardado = productoRepositorio.save(producto);

        return productoMapper.aDetalle(guardado);
    }

    @Transactional
    public ProductoDetalle actualizar(
            Long id,
            ProductoActualizar dto
    ) {
        Producto producto = productoRepositorio.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "No existe el producto con id " + id
                        )
                );

        productoRepositorio.findByTituloIgnoreCase(dto.titulo())
                .filter(existente -> !existente.getId().equals(id))
                .ifPresent(existente -> {
                    throw new RecursoDuplicadoException(
                            "Ya existe un producto con el título: "
                                    + dto.titulo()
                    );
                });

        Categoria categoria = categoriaRepositorio
                .findById(dto.categoriaId())
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "No existe la categoría con id "
                                        + dto.categoriaId(),
                                400
                        )
                );

        productoMapper.actualizarEntidad(
                producto,
                dto,
                categoria
        );

        return productoMapper.aDetalle(producto);
    }

    @Transactional
    public void eliminar(Long id) {
        Producto producto = productoRepositorio.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "No existe el producto con id " + id
                        )
                );

        productoRepositorio.delete(producto);
    }

    @Transactional(readOnly = true)
    public List<ProductoDetalle> buscarPorCategoria(String nombre) {
        return productoRepositorio.findByCategoriaNombre(nombre)
                .stream()
                .map(productoMapper::aDetalle)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProductoDetalle> buscarPorPrecio(
            BigDecimal minimo,
            BigDecimal maximo
    ) {
        return productoRepositorio
                .findByPrecioBetween(minimo, maximo)
                .stream()
                .map(productoMapper::aDetalle)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProductoDetalle> buscarSinStock(int limite) {
        return productoRepositorio
                .findByExistenciasLessThan(limite)
                .stream()
                .map(productoMapper::aDetalle)
                .toList();
    }

    @Transactional(readOnly = true)
    public EstadisticasCatalogo obtenerEstadisticas() {

        List<Producto> productos =
                productoRepositorio.buscarConCategoria();

        BigDecimal valorTotal = productos.stream()
                .map(producto ->
                        producto.getPrecio()
                                .multiply(
                                        BigDecimal.valueOf(
                                                producto.getExistencias()
                                        )
                                )
                )
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal iva = valorTotal
                .multiply(new BigDecimal("0.19"));

        Map<String, Integer> cantidadPorCategoria =
                new LinkedHashMap<>();

        productoRepositorio.contarPorCategoria()
                .forEach(fila ->
                        cantidadPorCategoria.put(
                                (String) fila[0],
                                ((Number) fila[1]).intValue()
                        )
                );

        return new EstadisticasCatalogo(
                productos.size(),
                valorTotal.add(iva),
                cantidadPorCategoria
        );
    }

    @Transactional
    public void transferirExistencias(
            Long origenId,
            Long destinoId,
            int cantidad
    ) {
        Producto origen = productoRepositorio.findById(origenId)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "No existe el producto origen con id "
                                        + origenId
                        )
                );

        Producto destino = productoRepositorio.findById(destinoId)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "No existe el producto destino con id "
                                        + destinoId
                        )
                );

        if (cantidad <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad debe ser mayor que cero"
            );
        }

        if (origen.getExistencias() < cantidad) {
            throw new IllegalArgumentException(
                    "No hay existencias suficientes"
            );
        }

        origen.setExistencias(
                origen.getExistencias() - cantidad
        );

        destino.setExistencias(
                destino.getExistencias() + cantidad
        );
    }

    @Transactional
    public void actualizarPrecioSinSave(
            Long id,
            BigDecimal nuevoPrecio
    ) {
        Producto producto = productoRepositorio.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "No existe el producto con id " + id
                        )
                );

        producto.setPrecio(nuevoPrecio);
    }
}