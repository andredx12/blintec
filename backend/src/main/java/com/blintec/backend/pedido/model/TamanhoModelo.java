package com.blintec.backend.pedido.model;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "tamanho_modelo")
public class TamanhoModelo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "modelo_id", nullable = false)
    private Modelo modelo;

    @Column(nullable = false, length = 10)
    private String tamanho;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(nullable = false)
    private Genero genero;

    @ManyToOne
    @JoinColumn(name = "tamanho_equivalente_id")
    private TamanhoModelo tamanhoEquivalente;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Modelo getModelo() {
        return modelo;
    }

    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
    }

    public String getTamanho() {
        return tamanho;
    }

    public void setTamanho(String tamanho) {
        this.tamanho = tamanho;
    }

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    public TamanhoModelo getTamanhoEquivalente() {
        return tamanhoEquivalente;
    }

    public void setTamanhoEquivalente(TamanhoModelo tamanhoEquivalente) {
        this.tamanhoEquivalente = tamanhoEquivalente;
    }

}