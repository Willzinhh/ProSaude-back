package br.ufsm.spi.ProSaude.controller;

import br.ufsm.spi.ProSaude.dto.inscricao.CadastroInscricaoDTO;
import br.ufsm.spi.ProSaude.dto.inscricao.InscritoDTO;
import br.ufsm.spi.ProSaude.dto.turma.HistoricoTurmaDTO;
import br.ufsm.spi.ProSaude.model.turma.Turma;
import br.ufsm.spi.ProSaude.service.InscricaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/inscricao")
public class InscricaoController {

    @Autowired
    private InscricaoService service;

    @GetMapping("/turma/{turmaId}")
    public ResponseEntity<List<InscritoDTO>> listarPorTurma(@PathVariable Long turmaId) {
        return ResponseEntity.ok(service.listarAlunosPorTurma(turmaId));
    }

    @GetMapping("/{id}/{ano}/{semestre}")
    public ResponseEntity<?> listarPorInscrito(@PathVariable Long id, @PathVariable String ano, @PathVariable String semestre) {
        String periodo = ano + "/" + semestre;
        Turma turma = service.listarTurmaPorAluno(id, periodo);
        if (turma == null) {
            return ResponseEntity.ok().body(List.of()); // Retorna uma lista vazia pro Flutter tratar no isEmpty()
        }

        return ResponseEntity.ok(List.of(turma));


    }

    @PostMapping("/autocadastro")
    public ResponseEntity<?> realizarInscricao(@RequestBody CadastroInscricaoDTO dto) {
        try {
            service.processarInscricaoEAutoCadastro(dto);
            return ResponseEntity.ok(Map.of("mensagem", "Inscrição realizada com sucesso!"));
        } catch (IllegalArgumentException e) {
            // Retorna HTTP 400 com a mensagem de bloqueio por semestre 🛑
            return ResponseEntity.badRequest().body(Map.of("mensagem", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("mensagem", "Erro interno ao processar a inscrição."));
        }
    }

    @GetMapping("/historico/{alunoId}")
    public ResponseEntity<List<HistoricoTurmaDTO>> obterHistorico(@PathVariable Long alunoId) {
        System.out.println("👉 Chamando o método de histórico real para o aluno ID: " + alunoId);

        // 🎯 GARANTA QUE ESTÁ CHAMANDO 'listarHistoricoDoAluno' e NÃO 'listarTurmaPorAluno'
        List<HistoricoTurmaDTO> historico = service.listarHistoricoDoAluno(alunoId);

        return ResponseEntity.ok(historico);
    }

    @DeleteMapping("/{turmaId}/{alunoId}")
    public ResponseEntity<?> removerAlunodaTurma(@PathVariable Long turmaId, @PathVariable Long alunoId) {
        this.service.deletar(turmaId, alunoId);
        return ResponseEntity.noContent().build();
    }
}
