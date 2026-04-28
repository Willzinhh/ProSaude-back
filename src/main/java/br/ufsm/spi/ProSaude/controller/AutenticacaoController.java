package br.ufsm.spi.ProSaude.controller;


import br.ufsm.spi.ProSaude.dto.login.LoginResponseDTO;
import br.ufsm.spi.ProSaude.infra.security.TokenServiceJWT;
import br.ufsm.spi.ProSaude.model.usuario.Usuario;
import br.ufsm.spi.ProSaude.model.usuario.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


@RestController
@RequestMapping("/login")
public class AutenticacaoController {


    private final AuthenticationManager manager;
    private final TokenServiceJWT tokenServiceJWT;
    private final UsuarioRepository userRepository;

    public AutenticacaoController(AuthenticationManager manager, TokenServiceJWT tokenServiceJWT, UsuarioRepository userRepository) {
        this.manager = manager;
        this.tokenServiceJWT = tokenServiceJWT;
        this.userRepository = userRepository;
    }

    @PostMapping

    public ResponseEntity<?> efetuarLogin(@RequestBody @Valid DadosAutenticacao dados) {
        try {
            Authentication autenticado = new UsernamePasswordAuthenticationToken(dados.email(), dados.senha());
            Authentication at = manager.authenticate(autenticado);

            Optional<Usuario> userr = userRepository.findByEmail(dados.email());

            User user = (User) at.getPrincipal();
            assert userr.orElse(null) != null;
            String token = this.tokenServiceJWT.gerarToken(userr.orElse(null));
            String nome = userr.get().getNome();
            String perfil = userr.get().getPerfil().toString();
            Long id = userr.get().getId();
            boolean pa = userr.get().getPrimeiroAcesso();

            return ResponseEntity.ok(new LoginResponseDTO(token, nome, perfil, id, pa));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Falha na autenticação: Credenciais inválidas.");
        }
    }

    private record DadosAutenticacao(String email, String senha) {

    }

    private record DadosTokenJWT(String token) {
    }
}

