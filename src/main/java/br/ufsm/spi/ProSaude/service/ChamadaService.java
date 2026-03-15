package br.ufsm.spi.ProSaude.service;

import br.ufsm.spi.ProSaude.model.aluno.Aluno;
import br.ufsm.spi.ProSaude.model.aluno.AlunoRepository;
import br.ufsm.spi.ProSaude.model.inscricao.Inscricao;
import br.ufsm.spi.ProSaude.model.inscricao.InscricaoRepository;
import br.ufsm.spi.ProSaude.model.presenca.Presenca;
import br.ufsm.spi.ProSaude.model.presenca.PresencaRepository;
import br.ufsm.spi.ProSaude.model.turma.Turma;
import br.ufsm.spi.ProSaude.model.turma.TurmaRepository;
import br.ufsm.spi.ProSaude.model.usuario.Usuario;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class ChamadaService {

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private TurmaRepository turmaRepository;

    @Autowired
    private InscricaoRepository inscricaoRepository;

    @Autowired
    private PresencaRepository presencaRepository;


}