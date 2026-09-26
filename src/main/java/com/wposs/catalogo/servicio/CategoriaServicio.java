package com.wposs.catalogo.servicio;

import com.wposs.catalogo.dto.CategoriaDetalle;
import com.wposs.catalogo.dto.CategoriaNueva;
import com.wposs.catalogo.excepcion.RecursoDuplicadoException;
import com.wposs.catalogo.excepcion.RecursoNoEncontradoException;
import com.wposs.catalogo.excepcion.ReglaDeNegocioException;
import com.wposs.catalogo.mapper.CategoriaMapper;
import com.wposs.catalogo.modelo.Categoria;
import com.wposs.catalogo.repositorio.CategoriaRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoriaServicio {

    private final CategoriaRepositorio categoriaRepositorio;
    private final CategoriaMapper categoriaMapper;

    public CategoriaServicio(
            CategoriaRepositorio categoriaRepositorio,
            CategoriaMapper categoriaMapper
    ) {
        this.categoriaRepositorio = categoriaRepositorio;
        this.categoriaMapper = categoriaMapper;
    }

    @Transactional(readOnly = true)
    public List<CategoriaDetalle> buscarTodas() {
        return categoriaRepositorio.findAll()
                .stream()
                .map(categoriaMapper::aDetalle)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoriaDetalle buscarPorId(Long id) {
        Categoria categoria = categoriaRepositorio.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "No existe la categoría con id " + id
                        )
                );

        return categoriaMapper.aDetalle(categoria);
    }

    @Transactional
    public CategoriaDetalle guardar(CategoriaNueva dto) {

        if (categoriaRepositorio.existsByNombreIgnoreCase(
                dto.nombre()
        )) {
            throw new RecursoDuplicadoException(
                    "Ya existe una categoría con el nombre: "
                            + dto.nombre()
            );
        }

        Categoria categoria = categoriaMapper.aEntidad(dto);

        Categoria guardada = categoriaRepositorio.save(categoria);

        return categoriaMapper.aDetalle(guardada);
    }

    @Transactional
    public CategoriaDetalle actualizar(
            Long id,
            CategoriaNueva dto
    ) {
        Categoria categoria = categoriaRepositorio.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "No existe la categoría con id " + id
                        )
                );

        categoriaRepositorio.findByNombreIgnoreCase(dto.nombre())
                .filter(existente -> !existente.getId().equals(id))
                .ifPresent(existente -> {
                    throw new RecursoDuplicadoException(
                            "Ya existe una categoría con el nombre: "
                                    + dto.nombre()
                    );
                });

        categoriaMapper.actualizarEntidad(categoria, dto);

        return categoriaMapper.aDetalle(categoria);
    }

    @Transactional
    public void eliminar(Long id) {

        Categoria categoria = categoriaRepositorio.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "No existe la categoría con id " + id
                        )
                );

        if (!categoria.getProductos().isEmpty()) {
            throw new ReglaDeNegocioException(
                    "No se puede eliminar una categoría que tiene productos"
            );
        }

        categoriaRepositorio.delete(categoria);
    }
}