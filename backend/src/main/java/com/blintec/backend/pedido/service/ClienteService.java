package com.blintec.backend.pedido.service;

import com.blintec.backend.pedido.model.Cliente;
import com.blintec.backend.pedido.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }

    public Cliente criar(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

}