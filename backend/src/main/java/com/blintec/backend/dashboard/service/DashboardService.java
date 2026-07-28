package com.blintec.backend.dashboard.service;
import com.blintec.backend.estoque.model.MovimentacaoEstoque;
import com.blintec.backend.estoque.model.TipoMovimentacaoEstoque;
import com.blintec.backend.estoque.repository.MovimentacaoEstoqueRepository;

import com.blintec.backend.estoque.model.Rolo;
import com.blintec.backend.estoque.model.TipoTecido;
import com.blintec.backend.estoque.repository.RoloRepository;
import com.blintec.backend.estoque.repository.TipoTecidoRepository;
import com.blintec.backend.pedido.model.Pedido;
import com.blintec.backend.pedido.model.StatusPedido;
import com.blintec.backend.pedido.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    @Autowired
    private MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;
    
    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private RoloRepository roloRepository;

    @Autowired
    private TipoTecidoRepository tipoTecidoRepository;

    public ResumoPedidos resumoPedidos() {
        List<Pedido> todos = pedidoRepository.findAll();

        Map<StatusPedido, Long> contagem = todos.stream()
                .collect(Collectors.groupingBy(Pedido::getStatus, Collectors.counting()));

        return new ResumoPedidos(contagem, todos.size());
    }

    public List<PedidoAtrasado> pedidosAtrasados() {
        LocalDate hoje = LocalDate.now();

        return pedidoRepository.findAll().stream()
                .filter(pedido -> pedido.getStatus() != StatusPedido.ENTREGUE)
                .map(pedido -> {
                    boolean atrasado = pedido.getDataEntrega().isBefore(hoje);
                    boolean proximoVencimento = !atrasado
                            && pedido.getDataEntrega().isBefore(hoje.plusDays(3));

                    return new PedidoAtrasado(
                            pedido.getId(),
                            pedido.getNumeroPedido(),
                            pedido.getDataEntrega(),
                            atrasado,
                            proximoVencimento
                    );
                })
                .filter(pa -> pa.atrasado() || pa.proximoVencimento())
                .collect(Collectors.toList());
    }

    public List<IndicadorEstoque> indicadoresEstoque() {
        List<TipoTecido> tipos = tipoTecidoRepository.findAll();

        return tipos.stream()
                .map(tipo -> {
                    BigDecimal saldoTotal = roloRepository.findAll().stream()
                            .filter(rolo -> rolo.getTipoTecido().getId().equals(tipo.getId()))
                            .map(Rolo::getSaldoAtual)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    boolean abaixoDoMinimo = saldoTotal.compareTo(tipo.getEstoqueMinimo()) < 0;

                    return new IndicadorEstoque(
                            tipo.getId(),
                            tipo.getNome(),
                            saldoTotal,
                            tipo.getEstoqueMinimo(),
                            abaixoDoMinimo
                    );
                })
                .collect(Collectors.toList());
    }
    public ConsumoPeriodo consumoPeriodo(LocalDate inicio, LocalDate fim) {
        List<MovimentacaoEstoque> saidas = movimentacaoEstoqueRepository.findAll().stream()
                .filter(mov -> mov.getTipo() == TipoMovimentacaoEstoque.SAIDA)
                .filter(mov -> {
                    LocalDate dataMovimentacao = mov.getData().toLocalDate();
                    return !dataMovimentacao.isBefore(inicio) && !dataMovimentacao.isAfter(fim);
                })
                .collect(Collectors.toList());

        BigDecimal total = saidas.stream()
                .map(MovimentacaoEstoque::getQuantidade)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new ConsumoPeriodo(inicio, fim, total);
    }

}