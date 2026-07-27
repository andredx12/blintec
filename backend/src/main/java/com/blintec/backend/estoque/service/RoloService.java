package com.blintec.backend.estoque.service;

import com.blintec.backend.auth.model.Usuario;
import com.blintec.backend.estoque.model.MovimentacaoEstoque;
import com.blintec.backend.estoque.model.Rolo;
import com.blintec.backend.estoque.model.TipoMovimentacaoEstoque;
import com.blintec.backend.estoque.repository.MovimentacaoEstoqueRepository;
import com.blintec.backend.estoque.repository.RoloRepository;
import com.blintec.backend.pedido.model.Pedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class RoloService {

    @Autowired
    private RoloRepository roloRepository;

    @Autowired
    private MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;

    public List<Rolo> listarTodos() {
        return roloRepository.findAll();
    }

    public Optional<Rolo> buscarPorId(Long id) {
        return roloRepository.findById(id);
    }

    @Transactional
    public Rolo registrarEntrada(Rolo rolo, Usuario usuario) {
        rolo.setSaldoAtual(rolo.getMetragemInicial());
        Rolo roloSalvo = roloRepository.save(rolo);

        MovimentacaoEstoque movimentacao = new MovimentacaoEstoque();
        movimentacao.setRolo(roloSalvo);
        movimentacao.setTipo(TipoMovimentacaoEstoque.ENTRADA);
        movimentacao.setQuantidade(roloSalvo.getMetragemInicial());
        movimentacao.setUsuario(usuario);
        movimentacaoEstoqueRepository.save(movimentacao);

        return roloSalvo;
    }

    @Transactional
    public void registrarSaida(Long roloId, BigDecimal quantidade, Usuario usuario, Pedido pedido) {
        Rolo rolo = roloRepository.findById(roloId)
                .orElseThrow(() -> new IllegalArgumentException("Rolo não encontrado"));

        if (rolo.getSaldoAtual().compareTo(quantidade) < 0) {
            throw new IllegalStateException("Saldo insuficiente no rolo " + rolo.getCodigo());
        }

        rolo.setSaldoAtual(rolo.getSaldoAtual().subtract(quantidade));
        roloRepository.save(rolo);

        MovimentacaoEstoque movimentacao = new MovimentacaoEstoque();
        movimentacao.setRolo(rolo);
        movimentacao.setTipo(TipoMovimentacaoEstoque.SAIDA);
        movimentacao.setQuantidade(quantidade);
        movimentacao.setUsuario(usuario);
        movimentacao.setPedido(pedido);
        movimentacaoEstoqueRepository.save(movimentacao);
    }

}