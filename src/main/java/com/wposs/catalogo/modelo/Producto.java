package com.wposs.catalogo.modelo;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String titulo;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precio;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @Column(nullable = false)
    private Integer existencias;

    protected Producto() {
    }

    public Producto(
            Long id,
            String titulo,
            BigDecimal precio,
            Categoria categoria,
            Integer existencias
    ) {
        this.id = id;
        this.titulo = titulo;
        this.precio = precio;
        this.categoria = categoria;
        this.existencias = existencias;
    }

    public Producto(
            String titulo,
            BigDecimal precio,
            Categoria categoria,
            Integer existencias
    ) {
        this.titulo = titulo;
        this.precio = precio;
        this.categoria = categoria;
        this.existencias = existencias;
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public Integer getExistencias() {
        return existencias;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public void setExistencias(Integer existencias) {
        this.existencias = existencias;
    }
}