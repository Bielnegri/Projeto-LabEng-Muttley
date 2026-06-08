package com.fatec.muttley.security;

import com.fatec.muttley.pessoa.Pessoa;
import java.time.Duration;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private final JwtEncoder jwtEncoder;
    private final Duration expiration;

    public JwtService(JwtEncoder jwtEncoder,
                      @Value("${muttley.jwt.expiration:2h}") Duration expiration) {
        this.jwtEncoder = jwtEncoder;
        this.expiration = expiration;
    }

    public String gerarToken(Pessoa pessoa) {
        Instant agora = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(pessoa.getEmail())
                .issuedAt(agora)
                .expiresAt(agora.plus(expiration))
                .claim("userId", pessoa.getId())
                .claim("role", pessoa.getRole().name())
                .build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }

    public String gerarTokenCadastro(Pessoa pessoa) {
        Instant agora = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(pessoa.getEmail())
                .issuedAt(agora)
                .expiresAt(agora.plus(Duration.ofHours(24)))
                .claim("userId", pessoa.getId())
                .claim("role", pessoa.getRole().name())
                .claim("type", "REGISTRATION")
                .claim("nome", pessoa.getNome())
                .claim("email", pessoa.getEmail())
                .claim("cpf", pessoa.getCpf())
                .build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();

        return jwtEncoder.encode(
                JwtEncoderParameters.from(header, claims)
        ).getTokenValue();
    }

    public long getExpirationSeconds() {
        return expiration.toSeconds();
    }
}
