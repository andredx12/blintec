package com.blintec.backend.pedido.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "item_pedido")
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    @JsonIgnore
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "tamanho_modelo_id", nullable = false)
    private TamanhoModelo tamanhoModelo;

    @Column(nullable = false)
    private Integer quantidade;

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

    public TamanhoModelo getTamanhoModelo() {
        return tamanhoModelo;
    }

    public void setTamanhoModelo(TamanhoModelo tamanhoModelo) {
        this.tamanhoModelo = tamanhoModelo;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

}