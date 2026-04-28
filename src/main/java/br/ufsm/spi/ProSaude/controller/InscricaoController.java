package br.ufsm.spi.ProSaude.controller;

import br.ufsm.spi.ProSaude.dto.inscricao.CadastroInscricaoDTO;
import br.ufsm.spi.ProSaude.dto.inscricao.InscritoDTO;
import br.ufsm.spi.ProSaude.service.InscricaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/autocadastro")
    public ResponseEntity<?> autoCadastro(@RequestBody CadastroInscricaoDTO dto) {
        System.out.printf("******************************"+dto.getEmail());
        try {
            // Chama o Service que você já configurou para processar a lógica
            service.processarInscricaoEAutoCadastro(dto);
            System.out.printf("OQ TA DANDOOOOOO");

            // Retorna sucesso (201 Created ou 200 OK)
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Cadastro e inscrição realizados com sucesso!");

        } catch (RuntimeException e) {
            // Se o Service lançar erro (CPF duplicado, já inscrito, etc), retorna 400
            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (Exception e) {
            // Erro genérico de servidor (500)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno ao processar a inscrição.");
        }
    }
}
