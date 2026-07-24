package com.blintec.backend.pedido.service;

import com.blintec.backend.pedido.model.Modelo;
import com.blintec.backend.pedido.model.TamanhoModelo;
import com.blintec.backend.pedido.repository.ModeloRepository;
import com.blintec.backend.pedido.repository.TamanhoModeloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ModeloService {

    @Autowired
    private ModeloRepository modeloRepository;

    @Autowired
    private TamanhoModeloRepository tamanhoModeloRepository;

    public List<Modelo> listarTodos() {
        return modeloRepository.findAll();
    }

    public Optional<Modelo> buscarPorId(Long id) {
        return modeloRepository.findById(id);
    }

    public Modelo criar(Modelo modelo) {
        return modeloRepository.save(modelo);
    }

    public List<TamanhoModelo> listarTamanhos(Long modeloId) {
        return tamanhoModeloRepository.findByModeloId(modeloId);
    }

}