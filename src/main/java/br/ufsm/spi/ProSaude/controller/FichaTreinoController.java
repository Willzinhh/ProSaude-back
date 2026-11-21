package br.ufsm.spi.ProSaude.controller;

import br.ufsm.spi.ProSaude.dto.fichaTreino.FichaTreinoDTO;
import br.ufsm.spi.ProSaude.service.FichaTreinoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/fichas-treino")
public class FichaTreinoController {

    @Autowired
    private FichaTreinoService service;

    @GetMapping("/turma/{turmaId}/aluno/{alunoId}")
    public ResponseEntity<?> buscarFicha(@PathVariable Long turmaId, @PathVariable Long alunoId) {
        try {
            return ResponseEntity.ok(service.buscarPorTurmaEAluno(turmaId, alunoId));
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("mensagem", e.getMessage()));
        }
    }

    @PutMapping("/turma/{turmaId}/aluno/{alunoId}")
    public ResponseEntity<?> salvarFicha(
            @PathVariable Long turmaId,
            @PathVariable Long alunoId,
            @RequestBody FichaTreinoDTO dto) {
        try {
            return ResponseEntity.ok(service.salvarOuAtualizar(turmaId, alunoId, dto));
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("mensagem", e.getMessage()));
        }
    }
}