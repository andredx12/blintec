package com.blintec.backend.producao.service;

import com.blintec.backend.auth.model.Usuario;
import com.blintec.backend.pedido.model.Pedido;
import com.blintec.backend.pedido.model.StatusPedido;
import com.blintec.backend.pedido.repository.PedidoRepository;
import com.blintec.backend.producao.model.MovimentacaoProducao;
import com.blintec.backend.producao.repository.MovimentacaoProducaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
public class ProducaoService {

    private static final Map<StatusPedido, StatusPedido> PROXIMA_ETAPA = new EnumMap<>(StatusPedido.class);

    static {
        PROXIMA_ETAPA.put(StatusPedido.PROGRAMADO, StatusPedido.EM_CORTE);
        PROXIMA_ETAPA.put(StatusPedido.EM_CORTE, StatusPedido.EM_COSTURA);
        PROXIMA_ETAPA.put(StatusPedido.EM_COSTURA, StatusPedido.EM_ARREMATACAO);
        PROXIMA_ETAPA.put(StatusPedido.EM_ARREMATACAO, StatusPedido.EM_EXPEDICAO);
        PROXIMA_ETAPA.put(StatusPedido.EM_EXPEDICAO, StatusPedido.ENTREGUE);
    }

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private MovimentacaoProducaoRepository movimentacaoProducaoRepository;

    @Transactional
    public MovimentacaoProducao avancarEtapa(Long pedidoId, Usuario usuario) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));

        StatusPedido etapaAtual = pedido.getStatus();
        StatusPedido proximaEtapa = PROXIMA_ETAPA.get(etapaAtual);

        if (proximaEtapa == null) {
            throw new IllegalStateException("Pedido não pode avançar a partir do status " + etapaAtual);
        }

        pedido.setStatus(proximaEtapa);
        pedidoRepository.save(pedido);

        MovimentacaoProducao movimentacao = new MovimentacaoProducao();
        movimentacao.setPedido(pedido);
        movimentacao.setEtapaAnterior(etapaAtual);
        movimentacao.setEtapaNova(proximaEtapa);
        movimentacao.setUsuario(usuario);

        return movimentacaoProducaoRepository.save(movimentacao);
    }

    public List<MovimentacaoProducao> historico(Long pedidoId) {
        return movimentacaoProducaoRepository.findByPedidoIdOrderByDataHoraAsc(pedidoId);
    }

}