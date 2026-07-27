package com.blintec.backend.estoque.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "rolo")
public class Rolo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    @NotBlank(message = "Código do rolo é obrigatório")
    private String codigo;

    @ManyToOne
    @JoinColumn(name = "tipo_tecido_id", nullable = false)
    @NotNull(message = "Tipo de tecido é obrigatório")
    private TipoTecido tipoTecido;

    @Column(nullable = false, length = 50)
    @NotBlank(message = "Cor é obrigatória")
    private String cor;

    @Column(name = "metragem_inicial", nullable = false, precision = 8, scale = 2)
    @NotNull(message = "Metragem inicial é obrigatória")
    @Positive(message = "Metragem inicial deve ser maior que zero")
    private BigDecimal metragemInicial;

    @Column(name = "saldo_atual", nullable = false, precision = 8, scale = 2)
    @NotNull(message = "Saldo atual é obrigatório")
    @PositiveOrZero(message = "Saldo atual não pode ser negativo")
    private BigDecimal saldoAtual;

    @Column(name = "criado_em", nullable = false, updatable = false, insertable = false)
    private LocalDateTime criadoEm;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public TipoTecido getTipoTecido() {
        return tipoTecido;
    }

    public void setTipoTecido(TipoTecido tipoTecido) {
        this.tipoTecido = tipoTecido;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public BigDecimal getMetragemInicial() {
        return metragemInicial;
    }

    public void setMetragemInicial(BigDecimal metragemInicial) {
        this.metragemInicial = metragemInicial;
    }

    public BigDecimal getSaldoAtual() {
        return saldoAtual;
    }

    public void setSaldoAtual(BigDecimal saldoAtual) {
        this.saldoAtual = saldoAtual;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

}