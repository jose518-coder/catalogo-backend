package com.wposs.catalogo.modelo;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

    @Column(nullable = false, length = 500)
    private String descripcion;

    @ElementCollection
    @CollectionTable(
            name = "producto_imagenes",
            joinColumns = @JoinColumn(name = "producto_id")
    )
    @Column(name = "imagen_url", nullable = false, length = 2048)
    @OrderColumn(name = "orden")
    private List<String> imagenes = new ArrayList<>();

    protected Producto() {
    }

    public Producto(Long id, String titulo, BigDecimal precio,
                    Categoria categoria, Integer existencias) {
        this(id, titulo, precio, categoria, existencias,
                "Descripción pendiente para " + titulo + ".", List.of());
    }

    public Producto(
            Long id,
            String titulo,
            BigDecimal precio,
            Categoria categoria,
            Integer existencias,
            String descripcion,
            List<String> imagenes
    ) {
        this.id = id;
        this.titulo = titulo;
        this.precio = precio;
        this.categoria = categoria;
        this.existencias = existencias;
        this.descripcion = descripcion;
        this.imagenes = new ArrayList<>(imagenes);
    }

    public Producto(
            String titulo,
            BigDecimal precio,
            Categoria categoria,
            Integer existencias
    ) {
        this(titulo, precio, categoria, existencias,
                "Descripción pendiente para " + titulo + ".", List.of());
    }

    public Producto(
            String titulo,
            BigDecimal precio,
            Categoria categoria,
            Integer existencias,
            String descripcion,
            List<String> imagenes
    ) {
        this.titulo = titulo;
        this.precio = precio;
        this.categoria = categoria;
        this.existencias = existencias;
        this.descripcion = descripcion;
        this.imagenes = new ArrayList<>(imagenes);
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

    public String getDescripcion() { return descripcion; }

    public List<String> getImagenes() { return imagenes; }

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

    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public void setImagenes(List<String> imagenes) {
        this.imagenes.clear();
        this.imagenes.addAll(imagenes);
    }
}
