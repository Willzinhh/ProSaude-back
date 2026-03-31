package br.ufsm.spi.ProSaude.infra.security;

import br.ufsm.spi.ProSaude.model.usuario.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenServiceJWT {

    // Defina uma constante para não errar o nome em métodos diferentes


    public String gerarToken(Usuario user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256("ProjetoIntegrador");
            return JWT.create()
                    .withIssuer("API ProSaude")
                    .withSubject(user.getEmail())
                    .withClaim("ROLE", String.valueOf(user.getPerfil()))
                    .withExpiresAt(dataExpiracao())
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            throw new RuntimeException("Erro ao gerar token", e);
        }
    }

    public String getSubject(String token) {
        try {
            // Use a mesma SECRET_KEY aqui
            Algorithm algorithm = Algorithm.HMAC256("ProjetoIntegrador");
            return JWT.require(algorithm)
                    .withIssuer("API ProSaude")
                    .build()
                    .verify(token) // O método correto para validar
                    .getSubject();
        } catch (JWTVerificationException e) { // Catch correto é VerificationException
            throw new RuntimeException("Token inválido ou expirado");
        }
    }

    private Instant dataExpiracao() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}