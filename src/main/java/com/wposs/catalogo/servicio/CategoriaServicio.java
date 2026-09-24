package com.wposs.catalogo.servicio;

import com.wposs.catalogo.modelo.Categoria;
import com.wposs.catalogo.repositorio.CategoriaRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CategoriaServicio {

    private final CategoriaRepositorio repositorio;

    public CategoriaServicio(CategoriaRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Transactional(readOnly = true)
    public List<Categoria> buscarTodas() {
        return repositorio.findAll();
    }

    @Transactional(readOnly = true)
    public Categoria buscarPorId(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "Categoría no encontrada: " + id));
    }

    @Transactional
    public Categoria guardar(Categoria categoria) {

        if (categoria.getNombre() == null
                || categoria.getNombre().isBlank()) {

            throw new IllegalArgumentException(
                    "El nombre de la categoría no puede estar vacío");
        }

        if (repositorio.existsByNombreIgnoreCase(
                categoria.getNombre())) {

            throw new IllegalArgumentException(
                    "La categoría ya existe");
        }

        return repositorio.save(categoria);
    }

    @Transactional
    public void eliminar(Long id) {

        Categoria categoria = buscarPorId(id);

        if (!categoria.getProductos().isEmpty()) {
            throw new IllegalStateException(
                    "No se puede eliminar una categoría que tiene productos");
        }

        repositorio.delete(categoria);
    }
}