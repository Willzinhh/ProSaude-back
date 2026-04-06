package br.ufsm.spi.ProSaude.controller;

import br.ufsm.spi.ProSaude.model.dadosAluno.DadosAluno;
import br.ufsm.spi.ProSaude.service.AlunoService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/aluno")
public class AlunoController {

    private final AlunoService alunoService;


    @PostMapping
    public ResponseEntity<DadosAluno> cadastrar(@RequestBody DadosAluno dadosAluno) {
        // O service já cuida de colocar o código em maiúsculo (T1, T2...)
        return ResponseEntity.ok(alunoService.salvar(dadosAluno));
    }

    @GetMapping
    public ResponseEntity<List<DadosAluno>> listar() {
        return ResponseEntity.ok(alunoService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosAluno> buscarPorId(@PathVariable Long id) {
        return alunoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping
    public ResponseEntity deletar( @PathVariable long id) {
        this.alunoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
