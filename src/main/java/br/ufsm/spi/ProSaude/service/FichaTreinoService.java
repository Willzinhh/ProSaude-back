package br.ufsm.spi.ProSaude.service;

import br.ufsm.spi.ProSaude.dto.fichaTreino.FichaTreinoDTO;
import br.ufsm.spi.ProSaude.model.inscricao.InscricaoRepository;
import br.ufsm.spi.ProSaude.model.inscricao.StatusInscricao;
import br.ufsm.spi.ProSaude.model.fichaTreino.FichaTreino;
import br.ufsm.spi.ProSaude.model.fichaTreino.FichaTreinoRepository;
import br.ufsm.spi.ProSaude.model.turma.Turma;
import br.ufsm.spi.ProSaude.model.turma.TurmaRepository;
import br.ufsm.spi.ProSaude.model.usuario.Usuario;
import br.ufsm.spi.ProSaude.model.usuario.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class FichaTreinoService {

    @Autowired
    private FichaTreinoRepository fichaTreinoRepository;
    @Autowired
    private TurmaRepository turmaRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private InscricaoRepository inscricaoRepository;

    public FichaTreinoDTO buscarPorTurmaEAluno(Long turmaId, Long alunoId) {
        Turma turma = turmaRepository.findById(turmaId)
                .orElseThrow(() -> new EntityNotFoundException("Turma não encontrada"));

        if (!Boolean.TRUE.equals(turma.getPossuiFichaTreino())) {
            throw new IllegalStateException("Esta turma não possui ficha de treino habilitada.");
        }

        Usuario aluno = usuarioRepository.findById(alunoId)
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado"));

        return fichaTreinoRepository.findByTurmaIdAndAlunoId(turmaId, alunoId)
                .map(this::toDTO)
                .orElse(new FichaTreinoDTO(
                        null,
                        turmaId,
                        alunoId,
                        aluno.getNome(),
                        "",
                        "",
                        "",
                        null
                ));
    }

    @Transactional
    public FichaTreinoDTO salvarOuAtualizar(Long turmaId, Long alunoId, FichaTreinoDTO dto) {
        Turma turma = turmaRepository.findById(turmaId)
                .orElseThrow(() -> new EntityNotFoundException("Turma não encontrada"));

        if (!Boolean.TRUE.equals(turma.getPossuiFichaTreino())) {
            throw new IllegalStateException("Esta turma não possui ficha de treino habilitada.");
        }

        Usuario aluno = usuarioRepository.findById(alunoId)
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado"));

        // Garante que o aluno esteja matriculado com vaga garantida na turma
        var inscricao = inscricaoRepository.findInscricoesByAlunoAndTurma(aluno, turma);
        if (inscricao == null || inscricao.getStatus() != StatusInscricao.VAGA_GARANTIDA) {
            throw new IllegalArgumentException("O aluno precisa estar com vaga garantida na turma para ter uma ficha de treino.");
        }

        FichaTreino ficha = fichaTreinoRepository.findByTurmaIdAndAlunoId(turmaId, alunoId)
                .orElse(new FichaTreino());

        ficha.setTurma(turma);
        ficha.setAluno(aluno);
        ficha.setObjetivo(dto.getObjetivo());
        ficha.setExercicios(dto.getExercicios());
        ficha.setObservacoes(dto.getObservacoes());
        ficha.setDataAtualizacao(LocalDate.now());

        return toDTO(fichaTreinoRepository.save(ficha));
    }

    private FichaTreinoDTO toDTO(FichaTreino f) {
        return new FichaTreinoDTO(
                f.getId(),
                f.getTurma().getId(),
                f.getAluno().getId(),
                f.getAluno().getNome(),
                f.getObjetivo(),
                f.getExercicios(),
                f.getObservacoes(),
                f.getDataAtualizacao()
        );
    }
}