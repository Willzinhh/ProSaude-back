package br.ufsm.spi.ProSaude.controller;

import br.ufsm.spi.ProSaude.model.usuario.Usuario;
import br.ufsm.spi.ProSaude.model.usuario.UsuarioRepository;
import br.ufsm.spi.ProSaude.service.UsuarioService;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<Usuario> cadastrar(@RequestBody Usuario usuario, UriComponentsBuilder uriBuilder) {
        Usuario user = this.usuarioService.salvar(usuario);
        URI uri = uriBuilder.path("/usuario").buildAndExpand(usuario.getId()).toUri();
        return ResponseEntity.created(uri).body(user);
    }

    @GetMapping
    public ResponseEntity listar() {
        return ResponseEntity.ok(this.usuarioService.listar());
    }

    @GetMapping("/equipe")
    public ResponseEntity<List<Usuario>> getEquipe() {
        List<Usuario> equipe = usuarioService.listarEquipe();
        return ResponseEntity.ok(equipe);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletar( @PathVariable long id) {
        this.usuarioService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
