package com.wposs.catalogo.mapper;

import com.wposs.catalogo.dto.ProductoActualizar;
import com.wposs.catalogo.dto.ProductoDetalle;
import com.wposs.catalogo.dto.ProductoNuevo;
import com.wposs.catalogo.dto.ProductoResumen;
import com.wposs.catalogo.modelo.Categoria;
import com.wposs.catalogo.modelo.Producto;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {

    public ProductoDetalle aDetalle(Producto producto) {
        return new ProductoDetalle(
                producto.getId(),
                producto.getTitulo(),
                producto.getPrecio(),
                producto.getExistencias(),
                producto.getCategoria().getNombre()
        );
    }

    public ProductoResumen aResumen(Producto producto) {
        return new ProductoResumen(
                producto.getId(),
                producto.getTitulo(),
                producto.getPrecio()
        );
    }

    public Producto aEntidad(ProductoNuevo dto, Categoria categoria) {
        return new Producto(
                dto.titulo(),
                dto.precio(),
                categoria,
                dto.existencias()
        );
    }

    public void actualizarEntidad(
            Producto producto,
            ProductoActualizar dto,
            Categoria categoria
    ) {
        producto.setTitulo(dto.titulo());
        producto.setPrecio(dto.precio());
        producto.setExistencias(dto.existencias());
        producto.setCategoria(categoria);
    }
}