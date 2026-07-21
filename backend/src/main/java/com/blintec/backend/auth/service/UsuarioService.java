package com.blintec.backend.auth.service;

import com.blintec.backend.auth.model.Perfil;
import com.blintec.backend.auth.model.Usuario;
import com.blintec.backend.auth.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario criar(String nome, String email, String senha, Perfil perfil) {
        if (usuarioRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Já existe um usuário com esse e-mail");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setSenhaHash(passwordEncoder.encode(senha));
        usuario.setPerfil(perfil);

        return usuarioRepository.save(usuario);
    }

}