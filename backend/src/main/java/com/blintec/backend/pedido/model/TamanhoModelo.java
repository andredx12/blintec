package com.blintec.backend.pedido.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;

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

    @Column(name = "consumo_tecido_por_peca", nullable = false, precision = 6, scale = 2)
    @NotNull(message = "Consumo de tecido por peca e obrigatorio")
    @Positive(message = "Consumo deve ser maior que zero")
    private BigDecimal consumoTecidoPorPeca;

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

    public BigDecimal getConsumoTecidoPorPeca() {
        return consumoTecidoPorPeca;
    }

    public void setConsumoTecidoPorPeca(BigDecimal consumoTecidoPorPeca) {
        this.consumoTecidoPorPeca = consumoTecidoPorPeca;
    }

    public BigDecimal getConsumoTecidoEfetivo() {
        return tamanhoEquivalente != null
                ? tamanhoEquivalente.getConsumoTecidoPorPeca()
                : consumoTecidoPorPeca;
    }

}