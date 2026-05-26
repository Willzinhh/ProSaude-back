package br.ufsm.spi.ProSaude.controller;

import br.ufsm.spi.ProSaude.dto.inscricao.CadastroInscricaoDTO;
import br.ufsm.spi.ProSaude.dto.inscricao.InscritoDTO;
import br.ufsm.spi.ProSaude.model.turma.Turma;
import br.ufsm.spi.ProSaude.service.InscricaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

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
    public ResponseEntity<List<Turma>> listarPorInscrito(@PathVariable Long id, @PathVariable String ano, @PathVariable String semestre) {
        String periodo = ano + "/" + semestre;
        List<Turma> turmas = Collections.singletonList(service.listarTurmaPorAluno(id, periodo));
        return ResponseEntity.ok(turmas);
    }

    @PostMapping("/autocadastro")
    public ResponseEntity<?> autoCadastro(@RequestBody CadastroInscricaoDTO dto) {
        try {
            service.processarInscricaoEAutoCadastro(dto);
            System.out.print("OQ TA DANDOOOOOO");

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Cadastro e inscrição realizados com sucesso!");

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno ao processar a inscrição.");
        }
    }
}
