package com.blintec.backend.auth.controller;

import com.blintec.backend.auth.model.Usuario;
import com.blintec.backend.auth.repository.UsuarioRepository;
import com.blintec.backend.auth.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> dados) {
        String email = dados.get("email");
        String senha = dados.get("senha");

        Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);

        if (usuario == null || !passwordEncoder.matches(senha, usuario.getSenhaHash())) {
            return ResponseEntity.status(401).body(Map.of("erro", "Email ou senha inválidos"));
        }

        String token = jwtService.gerarToken(usuario.getId(), usuario.getPerfil().name());

        return ResponseEntity.ok(Map.of(
                "token", token,
                "nome", usuario.getNome(),
                "perfil", usuario.getPerfil().name()
        ));
    }

@GetMapping("/me")
public ResponseEntity<?> me() {
    var auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();

    if (auth == null || !auth.isAuthenticated()) {
        return ResponseEntity.status(401).body(Map.of("erro", "Não autenticado"));
    }

    Long usuarioId = (Long) auth.getPrincipal();
    Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);

    if (usuario == null) {
        return ResponseEntity.status(404).body(Map.of("erro", "Usuário não encontrado"));
    }

    return ResponseEntity.ok(Map.of(
            "id", usuario.getId(),
            "nome", usuario.getNome(),
            "email", usuario.getEmail(),
            "perfil", usuario.getPerfil().name()
    ));
}

}