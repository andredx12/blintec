package com.blintec.backend.pedido.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

@Entity
@Table(name = "modelo")
public class Modelo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    @NotBlank(message = "Nome do modelo é obrigatório")
    private String nome;

    @Column(name = "consumo_tecido_por_peca", nullable = false, precision = 6, scale = 2)
    @NotNull(message = "Consumo de tecido por peça é obrigatório")
    @Positive(message = "Consumo deve ser maior que zero")
    private BigDecimal consumoTecidoPorPeca;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getConsumoTecidoPorPeca() {
        return consumoTecidoPorPeca;
    }

    public void setConsumoTecidoPorPeca(BigDecimal consumoTecidoPorPeca) {
        this.consumoTecidoPorPeca = consumoTecidoPorPeca;
    }

}