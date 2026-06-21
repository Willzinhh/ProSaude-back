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

    @GetMapping("/disponiveis/{semestre}")
    public ResponseEntity<List<Turma>> listarTurmasDisponiveis(@PathVariable String semestre) {
        // 🎯 Transforma "2026-2" de volta para "2026/2" para bater com o banco!
        String semestreFormatado = semestre.replace("-", "/");

        System.out.println("🔎 Buscando turmas no banco para o semestre: " + semestreFormatado);

        List<Turma> turmas = service.listarTurmasPorSemestre(semestreFormatado);
        return ResponseEntity.ok(turmas);
    }

    @GetMapping
    public ResponseEntity<List<Turma>> listar() {
        return ResponseEntity.ok(service.listarTodas());
    }

    @GetMapping("/minhas-turmas/{id}/{semestre}")
    public ResponseEntity<List<Turma>> getMinhasAtividades(@PathVariable int id, @PathVariable String semestre) {
        String semestreFormatado = semestre.replace("-", "/");
        List<Turma> turmas = service.buscarPorUsuario(id, semestreFormatado);
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

    @GetMapping("/historico/{id}")
    public ResponseEntity<List<Turma>> getHistoricoBolsista(@PathVariable int id) {
        List<Turma> turmas = service.buscarPorBolsista(id);
        return ResponseEntity.ok(turmas);
    }
}