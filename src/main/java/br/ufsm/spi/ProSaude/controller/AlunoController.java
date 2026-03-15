package br.ufsm.spi.ProSaude.controller;

import br.ufsm.spi.ProSaude.model.aluno.Aluno;
import br.ufsm.spi.ProSaude.model.aluno.AlunoRepository;
import br.ufsm.spi.ProSaude.model.turma.Turma;
import br.ufsm.spi.ProSaude.service.AlunoService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/aluno")
public class AlunoController {

    private final AlunoService alunoService;


    @PostMapping
    public ResponseEntity<Aluno> cadastrar(@RequestBody Aluno aluno) {
        // O service já cuida de colocar o código em maiúsculo (T1, T2...)
        return ResponseEntity.ok(alunoService.salvar(aluno));
    }

    @GetMapping
    public ResponseEntity<List<Aluno>> listar() {
        return ResponseEntity.ok(alunoService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Aluno> buscarPorId(@PathVariable Long id) {
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
