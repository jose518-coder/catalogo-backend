package com.wposs.catalogo.mapper;

import com.wposs.catalogo.dto.CategoriaDetalle;
import com.wposs.catalogo.dto.CategoriaNueva;
import com.wposs.catalogo.modelo.Categoria;
import org.springframework.stereotype.Component;

@Component
public class CategoriaMapper {

    public CategoriaDetalle aDetalle(Categoria categoria) {
        return new CategoriaDetalle(
                categoria.getId(),
                categoria.getNombre(),
                categoria.getDescripcion(),
                categoria.getProductos().size()
        );
    }

    public Categoria aEntidad(CategoriaNueva dto) {
        return new Categoria(
                dto.nombre(),
                dto.descripcion()
        );
    }

    public void actualizarEntidad(
            Categoria categoria,
            CategoriaNueva dto
    ) {
        categoria.setNombre(dto.nombre());
        categoria.setDescripcion(dto.descripcion());
    }
}