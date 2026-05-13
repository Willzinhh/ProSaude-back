package br.ufsm.spi.ProSaude.controller;

import br.ufsm.spi.ProSaude.dto.presenca.RegistroPresencaDTO;
import br.ufsm.spi.ProSaude.service.InscricaoService;
import br.ufsm.spi.ProSaude.service.PresencaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/presenca")
public class PresencaController {

    @Autowired
    private PresencaService presencaService;
    @Autowired
    private InscricaoService inscricaoService;

    // Para registrar a presença de vários alunos de uma vez
    @PostMapping("/registrar-chamada")
    public ResponseEntity<?> registrarChamada(@RequestBody List<RegistroPresencaDTO> chamada) {
        presencaService.processarChamada(chamada);
        return ResponseEntity.ok().build();
    }
    @PatchMapping("/{id}/registrar-falta")
    public ResponseEntity<Void> registrarFalta(@PathVariable Long id) {
//        inscricaoService.registrarFalta(id);
        return ResponseEntity.noContent().build();
    }
}
