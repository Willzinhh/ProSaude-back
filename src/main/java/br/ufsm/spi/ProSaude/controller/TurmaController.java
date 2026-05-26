package br.ufsm.spi.ProSaude.controller;

import br.ufsm.spi.ProSaude.dto.turma.TurmaRequestDTO;
import br.ufsm.spi.ProSaude.model.turma.Turma;
import br.ufsm.spi.ProSaude.service.TurmaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/turma")
public class TurmaController {
    @Autowired
    private TurmaService service;

    @PostMapping
    public ResponseEntity<Turma> cadastrar(@RequestBody TurmaRequestDTO turma) {
        return ResponseEntity.ok(service.salvar(turma));
    }

    @PutMapping
    public ResponseEntity<Turma> salvar(@RequestBody TurmaRequestDTO turma) {
        return ResponseEntity.ok(service.salvar(turma));
    }

    @GetMapping
    public ResponseEntity<List<Turma>> listar() {
        return ResponseEntity.ok(service.listarTodas());
    }

    @GetMapping("/minhas-turmas/{id}")
    public ResponseEntity<List<Turma>> getMinhasAtividades(@PathVariable int id) {
        List<Turma> turmas = service.buscarPorUsuario(id);
        return ResponseEntity.ok(turmas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Turma> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletar(@PathVariable long id) {
        this.service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}