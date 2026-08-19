package br.ufsm.spi.ProSaude.controller;

import br.ufsm.spi.ProSaude.service.ChamadaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.ufsm.spi.ProSaude.model.chamada.ChamadaDto;

import java.util.List;


@RestController
@RequestMapping("/chamadas")
public class ChamadaController {

    @Autowired
    private ChamadaService chamadaService;

    // GET /api/chamadas/turma/{turmaId}
    @GetMapping("/turma/{turmaId}")
    public ResponseEntity<List<ChamadaDto>> listarPorTurma(@PathVariable Long turmaId) {
        List<ChamadaDto> lista = chamadaService.buscarPorTurma(turmaId);
        return ResponseEntity.ok(lista);
    }

    // POST /api/chamadas
    @PostMapping
    public ResponseEntity<ChamadaDto> salvar(@RequestBody ChamadaDto dto) {
        ChamadaDto salva = chamadaService.registrarChamada(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(salva);
    }

    // PUT /api/chamadas/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizar(@PathVariable Long id, @RequestBody ChamadaDto dto) {
        chamadaService.atualizarChamada(id, dto);
        return ResponseEntity.ok().build();
    }
}
