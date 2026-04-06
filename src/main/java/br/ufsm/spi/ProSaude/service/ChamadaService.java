package br.ufsm.spi.ProSaude.service;

import br.ufsm.spi.ProSaude.model.dadosAluno.DadosAlunoRepository;
import br.ufsm.spi.ProSaude.model.inscricao.InscricaoRepository;
import br.ufsm.spi.ProSaude.model.presenca.PresencaRepository;
import br.ufsm.spi.ProSaude.model.turma.TurmaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChamadaService {

    @Autowired
    private DadosAlunoRepository dadosAlunoRepository;

    @Autowired
    private TurmaRepository turmaRepository;

    @Autowired
    private InscricaoRepository inscricaoRepository;

    @Autowired
    private PresencaRepository presencaRepository;


}