package br.ufsm.spi.ProSaude.controller;

import br.ufsm.spi.ProSaude.dto.usuario.UsuarioRequestDTO;
import br.ufsm.spi.ProSaude.dto.usuario.UsuarioResponseDTO;
import br.ufsm.spi.ProSaude.model.usuario.Usuario;
import br.ufsm.spi.ProSaude.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<Usuario> cadastrar(@RequestBody UsuarioRequestDTO usuario, UriComponentsBuilder uriBuilder) {
        Usuario user = this.usuarioService.salvar(usuario);
        URI uri = uriBuilder.path("/usuario").buildAndExpand(usuario).toUri();
        return ResponseEntity.created(uri).body(user);
    }

    @PutMapping
    public ResponseEntity<Usuario> salvar(@RequestBody UsuarioRequestDTO dados) {
        return ResponseEntity.ok(usuarioService.salvar(dados));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String senhaNoJson = body.get("senha"); // O nome aqui deve ser igual ao do Flutter
        // ... lógica para salvar e mudar primeiroAcesso para false
        usuarioService.salvarSenha(id, senhaNoJson);
        return ResponseEntity.ok().build();
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

    @PostMapping("/autocadastro")
    public ResponseEntity<Usuario> autocadastro(@RequestBody UsuarioRequestDTO dados) {
        // O service já cuida de colocar o código em maiúsculo (T1, T2...)
        return ResponseEntity.ok(usuarioService.salvar(dados));
    }

    @GetMapping("/alunos")
    public ResponseEntity<List<UsuarioResponseDTO>> listara() {
        return ResponseEntity.ok(usuarioService.listarAlunos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(@PathVariable Long id) {
        UsuarioResponseDTO dto = usuarioService.buscar(id);
        return ResponseEntity.ok(dto);
    }


}
