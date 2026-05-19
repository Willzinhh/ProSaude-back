package br.ufsm.spi.ProSaude.controller;

import br.ufsm.spi.ProSaude.model.avaliacao.Avaliacao;
import br.ufsm.spi.ProSaude.service.AvaliacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/avaliacoes")
public class AvaliacaoController {

    @Autowired
    private AvaliacaoService avaliacaoService;

    // Salva uma nova avaliação passando os IDs via Query Parameters ou DTO
    @PostMapping("/aluno/{alunoId}/avaliador/{avaliadorId}")
    public ResponseEntity<Avaliacao> criar(
            @RequestBody Avaliacao avaliacao,
            @PathVariable Long alunoId,
            @PathVariable Long avaliadorId) {

        Avaliacao novaAvaliacao = avaliacaoService.salvarAvaliacao(avaliacao, alunoId, avaliadorId);
        return ResponseEntity.ok(novaAvaliacao);
    }

    // Busca o histórico de um aluno específico
    @GetMapping("/aluno/{alunoId}")
    public ResponseEntity<List<Avaliacao>> listarPorAluno(@PathVariable Long alunoId) {
        return ResponseEntity.ok(avaliacaoService.listarPorAluno(alunoId));
    }
}