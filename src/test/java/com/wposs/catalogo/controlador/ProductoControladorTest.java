package com.wposs.catalogo.controlador;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wposs.catalogo.dto.ProductoDetalle;
import com.wposs.catalogo.dto.ProductoNuevo;
import com.wposs.catalogo.dto.ProductoResumen;
import com.wposs.catalogo.excepcion.RecursoDuplicadoException;
import com.wposs.catalogo.excepcion.RecursoNoEncontradoException;
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
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

        ProductoResumen producto = new ProductoResumen(
                1L,
                "Clean Code",
                new BigDecimal("45.90")
        );

        when(servicio.buscarTodos())
                .thenReturn(List.of(producto));

        mockMvc.perform(
                get("/api/productos")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].titulo").value("Clean Code"))
                .andExpect(jsonPath("$[0].precio").value(45.90));
    }

    @Test
    void debeRetornar404CuandoProductoNoExiste()
            throws Exception {

        when(servicio.buscarPorId(999L))
                .thenThrow(
                        new RecursoNoEncontradoException(
                                "No existe el producto con id 999"
                        )
                );

        mockMvc.perform(
                get("/api/productos/999")
        )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje").value(
                        "No existe el producto con id 999"
                ))
                .andExpect(jsonPath("$.ruta").value(
                        "/api/productos/999"
                ));
    }

    @Test
    void debeCrearProducto()
            throws Exception {

        ProductoNuevo entrada = new ProductoNuevo(
                "Laptop Lenovo",
                new BigDecimal("2500.00"),
                5,
                2L
        );

        ProductoDetalle creado = new ProductoDetalle(
                9L,
                "Laptop Lenovo",
                new BigDecimal("2500.00"),
                5,
                "tecnologia"
        );

        when(servicio.guardar(any(ProductoNuevo.class)))
                .thenReturn(creado);

        mockMvc.perform(
                post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(
                                        entrada
                                )
                        )
        )
                .andExpect(status().isCreated())
                .andExpect(
                        header().string(
                                "Location",
                                "/api/productos/9"
                        )
                )
                .andExpect(jsonPath("$.id").value(9))
                .andExpect(jsonPath("$.titulo").value(
                        "Laptop Lenovo"
                ))
                .andExpect(jsonPath("$.categoria").value(
                        "tecnologia"
                ));
    }

    @Test
    void debeRechazarPrecioNegativo()
            throws Exception {

        ProductoNuevo producto = new ProductoNuevo(
                "Producto inválido",
                new BigDecimal("-10"),
                5,
                2L
        );

        mockMvc.perform(
                post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(
                                        producto
                                )
                        )
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.precio").exists());
    }

    @Test
    void debeRechazarTituloEnBlanco()
            throws Exception {

        ProductoNuevo producto = new ProductoNuevo(
                " ",
                new BigDecimal("100.00"),
                5,
                2L
        );

        mockMvc.perform(
                post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(
                                        producto
                                )
                        )
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.titulo").exists());
    }

    @Test
    void debeRechazarTituloDe200Caracteres()
            throws Exception {

        String titulo = "A".repeat(200);

        ProductoNuevo producto = new ProductoNuevo(
                titulo,
                new BigDecimal("100.00"),
                5,
                2L
        );

        mockMvc.perform(
                post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(
                                        producto
                                )
                        )
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.titulo").exists());
    }

    @Test
    void debeRetornar400CuandoCategoriaNoExiste()
            throws Exception {

        ProductoNuevo producto = new ProductoNuevo(
                "Laptop Lenovo",
                new BigDecimal("2500.00"),
                5,
                9999L
        );

        when(servicio.guardar(any(ProductoNuevo.class)))
                .thenThrow(
                        new RecursoNoEncontradoException(
                                "No existe la categoría con id 9999",
                                400
                        )
                );

        mockMvc.perform(
                post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(
                                        producto
                                )
                        )
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value(
                        "No existe la categoría con id 9999"
                ));
    }

    @Test
    void debeRetornar409CuandoTituloEstaDuplicado()
            throws Exception {

        ProductoNuevo producto = new ProductoNuevo(
                "Laptop Lenovo",
                new BigDecimal("2500.00"),
                5,
                2L
        );

        when(servicio.guardar(any(ProductoNuevo.class)))
                .thenThrow(
                        new RecursoDuplicadoException(
                                "Ya existe un producto con el título: Laptop Lenovo"
                        )
                );

        mockMvc.perform(
                post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(
                                        producto
                                )
                        )
        )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensaje").value(
                        "Ya existe un producto con el título: Laptop Lenovo"
                ));
    }

    @Test
    void debeRechazarJsonMalFormado()
            throws Exception {

        mockMvc.perform(
                post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                """
                                {
                                    "titulo": "Laptop Lenovo",
                                    "precio": 2500.00,
                                    "existencias": 5,
                                    "categoriaId":
                                }
                                """
                        )
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").exists());
    }

    @Test
    void detalleNoDebeExponerCategoriaIdNiObjetoCategoria()
            throws Exception {

        ProductoDetalle detalle = new ProductoDetalle(
                1L,
                "Clean Code",
                new BigDecimal("45.90"),
                10,
                "libros"
        );

        when(servicio.buscarPorId(1L))
                .thenReturn(detalle);

        mockMvc.perform(
                get("/api/productos/1")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.titulo").value("Clean Code"))
                .andExpect(jsonPath("$.categoria").value("libros"))
                .andExpect(jsonPath("$.categoriaId").doesNotExist())
                .andExpect(jsonPath("$.categoria.id").doesNotExist());
    }
}