package com.blintec.backend.enfesto.model;

import com.blintec.backend.auth.model.Usuario;
import com.blintec.backend.pedido.model.Pedido;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "programacao_enfesto")
public class ProgramacaoEnfesto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "pedido_id", nullable = false, unique = true)
    @NotNull(message = "Pedido é obrigatório")
    private Pedido pedido;

    @Column(name = "data_programacao", nullable = false, updatable = false, insertable = false)
    private LocalDateTime dataProgramacao;

    @Column(name = "ajustado_manualmente", nullable = false)
    private boolean ajustadoManualmente = false;

    @ManyToOne
    @JoinColumn(name = "programado_por", nullable = false)
    @NotNull(message = "Usuário responsável é obrigatório")
    private Usuario programadoPor;

    @Column(name = "consumo_tecido_total", nullable = false, precision = 8, scale = 2)
    @NotNull(message = "Consumo total é obrigatório")
    @Positive(message = "Consumo total deve ser maior que zero")
    private BigDecimal consumoTecidoTotal;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public LocalDateTime getDataProgramacao() {
        return dataProgramacao;
    }

    public boolean isAjustadoManualmente() {
        return ajustadoManualmente;
    }

    public void setAjustadoManualmente(boolean ajustadoManualmente) {
        this.ajustadoManualmente = ajustadoManualmente;
    }

    public Usuario getProgramadoPor() {
        return programadoPor;
    }

    public void setProgramadoPor(Usuario programadoPor) {
        this.programadoPor = programadoPor;
    }

    public BigDecimal getConsumoTecidoTotal() {
        return consumoTecidoTotal;
    }

    public void setConsumoTecidoTotal(BigDecimal consumoTecidoTotal) {
        this.consumoTecidoTotal = consumoTecidoTotal;
    }

}