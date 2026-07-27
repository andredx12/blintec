package com.blintec.backend.estoque.service;

import com.blintec.backend.estoque.model.TipoTecido;
import com.blintec.backend.estoque.repository.TipoTecidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class TipoTecidoService {

    @Autowired
    private TipoTecidoRepository tipoTecidoRepository;

    public List<TipoTecido> listarTodos() {
        return tipoTecidoRepository.findAll();
    }

    public Optional<TipoTecido> buscarPorId(Long id) {
        return tipoTecidoRepository.findById(id);
    }

    public TipoTecido criar(TipoTecido tipoTecido) {
        return tipoTecidoRepository.save(tipoTecido);
    }

    public Optional<TipoTecido> atualizarEstoqueMinimo(Long id, BigDecimal novoEstoqueMinimo) {
        return tipoTecidoRepository.findById(id)
                .map(tipoTecido -> {
                    tipoTecido.setEstoqueMinimo(novoEstoqueMinimo);
                    return tipoTecidoRepository.save(tipoTecido);
                });
    }

}
