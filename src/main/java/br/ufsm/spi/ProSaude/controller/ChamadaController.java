package br.ufsm.spi.ProSaude.controller;

import br.ufsm.spi.ProSaude.model.presenca.ChamadaDTO;
import br.ufsm.spi.ProSaude.model.usuario.Usuario;
import br.ufsm.spi.ProSaude.service.ChamadaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chamada")
public class ChamadaController {

    @Autowired
    private ChamadaService chamadaService;


}
