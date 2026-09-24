package com.wposs.catalogo.controlador;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wposs.catalogo.modelo.Categoria;
import com.wposs.catalogo.modelo.Producto;
import com.wposs.catalogo.servicio.ProductoServicio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductoControlador.class)
class ProductoControladorTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductoServicio servicio;

    @Test
    void debeListarProductos() throws Exception {

        Categoria libros =
                new Categoria(1L, "libros");

        Producto producto =
                new Producto(
                        1L,
                        "Clean Code",
                        new BigDecimal("45.90"),
                        libros,
                        10);

        when(servicio.buscarTodos())
                .thenReturn(List.of(producto));

        mockMvc.perform(
                get("/api/productos"))
                .andExpect(status().isOk());
    }

    @Test
    void debeRetornar404CuandoProductoNoExiste()
            throws Exception {

        when(servicio.buscarPorId(999L))
                .thenThrow(
                        new java.util.NoSuchElementException());

        mockMvc.perform(
                get("/api/productos/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void debeCrearProducto()
            throws Exception {

        Categoria tecnologia =
                new Categoria(2L, "tecnologia");

        Producto entrada =
                new Producto(
                        null,
                        "Laptop Lenovo",
                        new BigDecimal("2500.00"),
                        tecnologia,
                        5);

        Producto creado =
                new Producto(
                        9L,
                        "Laptop Lenovo",
                        new BigDecimal("2500.00"),
                        tecnologia,
                        5);

        when(servicio.guardar(any(Producto.class)))
                .thenReturn(creado);

        mockMvc.perform(
                post("/api/productos")
                        .contentType(
                                MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(
                                        entrada)))
                .andExpect(status().isCreated())
                .andExpect(
                        header().string(
                                "Location",
                                "/api/productos/9"));
    }

    @Test
    void debeRechazarPrecioNegativo()
            throws Exception {

        Producto producto =
                new Producto(
                        null,
                        "Producto inválido",
                        new BigDecimal("-10"),
                        new Categoria(
                                2L,
                                "tecnologia"),
                        5);

        when(servicio.guardar(any(Producto.class)))
                .thenThrow(
                        new IllegalArgumentException());

        mockMvc.perform(
                post("/api/productos")
                        .contentType(
                                MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(
                                        producto)))
                .andExpect(
                        status().isBadRequest());
    }

    @Test
    void debeEliminarProducto()
            throws Exception {

        doNothing()
                .when(servicio)
                .eliminar(9L);

        mockMvc.perform(
                delete("/api/productos/9"))
                .andExpect(
                        status().isNoContent());
    }
}