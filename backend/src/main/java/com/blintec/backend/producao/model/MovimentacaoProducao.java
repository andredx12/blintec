package com.blintec.backend.producao.model;

import com.blintec.backend.auth.model.Usuario;
import com.blintec.backend.pedido.model.Pedido;
import com.blintec.backend.pedido.model.StatusPedido;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Entity
@Table(name = "movimentacao_producao")
public class MovimentacaoProducao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    @NotNull(message = "Pedido é obrigatório")
    private Pedido pedido;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "etapa_anterior", nullable = false)
    private StatusPedido etapaAnterior;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "etapa_nova", nullable = false)
    private StatusPedido etapaNova;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    @NotNull(message = "Usuário é obrigatório")
    private Usuario usuario;

    @Column(name = "data_hora", nullable = false, updatable = false, insertable = false)
    private LocalDateTime dataHora;

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

    public StatusPedido getEtapaAnterior() {
        return etapaAnterior;
    }

    public void setEtapaAnterior(StatusPedido etapaAnterior) {
        this.etapaAnterior = etapaAnterior;
    }

    public StatusPedido getEtapaNova() {
        return etapaNova;
    }

    public void setEtapaNova(StatusPedido etapaNova) {
        this.etapaNova = etapaNova;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

}