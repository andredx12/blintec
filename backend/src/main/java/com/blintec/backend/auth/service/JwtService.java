package com.blintec.backend.auth.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey chave = Keys.hmacShaKeyFor(
            "blintec-chave-secreta-desenvolvimento-troque-em-producao-123456".getBytes()
    );

    private static final long VALIDADE_MS = 8 * 60 * 60 * 1000; // 8 horas

    public String gerarToken(Long usuarioId, String perfil) {
        Date agora = new Date();
        Date expiracao = new Date(agora.getTime() + VALIDADE_MS);

        return Jwts.builder()
                .subject(usuarioId.toString())
                .claim("perfil", perfil)
                .issuedAt(agora)
                .expiration(expiracao)
                .signWith(chave)
                .compact();
    }

}