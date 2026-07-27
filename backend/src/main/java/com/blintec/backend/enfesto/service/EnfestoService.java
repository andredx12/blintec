package com.blintec.backend.enfesto.service;

import com.blintec.backend.auth.model.Usuario;
import com.blintec.backend.enfesto.model.ProgramacaoEnfesto;
import com.blintec.backend.enfesto.repository.ProgramacaoEnfestoRepository;
import com.blintec.backend.estoque.model.Rolo;
import com.blintec.backend.estoque.repository.RoloRepository;
import com.blintec.backend.estoque.service.RoloService;
import com.blintec.backend.pedido.model.Pedido;
import com.blintec.backend.pedido.model.StatusPedido;
import com.blintec.backend.pedido.repository.PedidoRepository;
import com.blintec.backend.pedido.service.ComponentesCapa;
import com.blintec.backend.pedido.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EnfestoService {

    @Autowired
    private ProgramacaoEnfestoRepository programacaoEnfestoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private RoloRepository roloRepository;

    @Autowired
    private RoloService roloService;

    @Autowired
    private PedidoService pedidoService;

    public BigDecimal calcularConsumoTotal(Pedido pedido) {
        ComponentesCapa componentes = pedidoService.calcularComponentes(pedido);
        BigDecimal consumoPorPeca = pedido.getModelo().getConsumoTecidoPorPeca();
        return consumoPorPeca.multiply(BigDecimal.valueOf(componentes.frentes()));
    }

    public List<SugestaoRolo> sugerirRolos(Long tipoTecidoId) {
        return roloRepository.findAll().stream()
                .filter(rolo -> rolo.getTipoTecido().getId().equals(tipoTecidoId))
                .filter(rolo -> rolo.getSaldoAtual().compareTo(BigDecimal.ZERO) > 0)
                .map(rolo -> new SugestaoRolo(rolo.getId(), rolo.getCodigo(), rolo.getSaldoAtual()))
                .collect(Collectors.toList());
    }

    @Transactional
    public ProgramacaoEnfesto confirmar(Long pedidoId, Map<Long, BigDecimal> rolosSelecionados, Usuario usuario) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));

        if (pedido.getStatus() != StatusPedido.AGUARDANDO_PROGRAMACAO) {
            throw new IllegalStateException("Pedido não está aguardando programação");
        }

        BigDecimal consumoTotal = calcularConsumoTotal(pedido);

        BigDecimal totalSelecionado = rolosSelecionados.values().stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalSelecionado.compareTo(consumoTotal) < 0) {
            throw new IllegalStateException(
                    "Quantidade selecionada (" + totalSelecionado + "m) é menor que o consumo necessário (" + consumoTotal + "m)"
            );
        }

        for (Map.Entry<Long, BigDecimal> entrada : rolosSelecionados.entrySet()) {
            roloService.registrarSaida(entrada.getKey(), entrada.getValue(), usuario, pedido);
        }

        ProgramacaoEnfesto programacao = new ProgramacaoEnfesto();
        programacao.setPedido(pedido);
        programacao.setProgramadoPor(usuario);
        programacao.setConsumoTecidoTotal(consumoTotal);
        programacao.setAjustadoManualmente(true);

        pedido.setStatus(StatusPedido.PROGRAMADO);
        pedidoRepository.save(pedido);

        return programacaoEnfestoRepository.save(programacao);
    }

}