package com.blintec.backend.auth.service;

import com.blintec.backend.auth.model.Perfil;
import com.blintec.backend.auth.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class UsuarioSeeder implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Override
    public void run(String... args) {
        if (usuarioRepository.findByEmail("admin@blintec.com").isEmpty()) {
            usuarioService.criar("Administrador", "admin@blintec.com", "admin123", Perfil.ADMINISTRADOR);
            System.out.println(">>> Usuário admin criado: admin@blintec.com / admin123");
        }
    }

}