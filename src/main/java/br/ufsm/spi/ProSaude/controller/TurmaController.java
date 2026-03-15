package br.ufsm.spi.ProSaude.controller;

import br.ufsm.spi.ProSaude.model.turma.Turma;
import br.ufsm.spi.ProSaude.model.turma.TurmaRepository;
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
    public ResponseEntity<Turma> cadastrar(@RequestBody Turma turma) {
        // O service já cuida de colocar o código em maiúsculo (T1, T2...)
        return ResponseEntity.ok(service.salvar(turma));
    }

    @GetMapping
    public ResponseEntity<List<Turma>> listar() {
        return ResponseEntity.ok(service.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Turma> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping
    public ResponseEntity deletar( @PathVariable long id) {
        this.service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
