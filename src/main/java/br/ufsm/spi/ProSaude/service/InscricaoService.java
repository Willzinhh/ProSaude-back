package br.ufsm.spi.ProSaude.service;

import br.ufsm.spi.ProSaude.model.dadosAluno.DadosAluno;
import br.ufsm.spi.ProSaude.model.inscricao.Inscricao;
import br.ufsm.spi.ProSaude.model.inscricao.InscricaoRepository;
import br.ufsm.spi.ProSaude.model.turma.Turma;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class InscricaoService {
    private InscricaoRepository inscricaoRepository;

    public void inscreverAlunoNaTurma(DadosAluno dadosAluno, Turma turma) {
        Inscricao novaInscricao = new Inscricao();
        novaInscricao.setAluno_id(dadosAluno.getId());
        novaInscricao.setTurma_id(turma.getId());
        novaInscricao.setData_inscricao(LocalDate.now());

        inscricaoRepository.save(novaInscricao);
    }
}
