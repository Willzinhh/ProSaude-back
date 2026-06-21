package br.ufsm.spi.ProSaude.model.inscricao;

import br.ufsm.spi.ProSaude.model.turma.Turma;
import br.ufsm.spi.ProSaude.model.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InscricaoRepository extends JpaRepository<Inscricao, Long> {

    List<Inscricao> findByAluno(Usuario u);

    boolean existsByAlunoIdAndSemestre(Long alunoId, String semestre);


    @Query("SELECT i FROM Inscricao i JOIN FETCH i.aluno WHERE i.turma.id = :turmaId")
    List<Inscricao> findInscricoesByTurma(@Param("turmaId") Long turmaId);

    Inscricao findInscricaoByAlunoAndSemestre(Usuario aluno, String semestre);

    List<Inscricao> findInscricoesByAlunoAndSemestre(Usuario u, String semestre);

    // 🎯 NOVA QUERY PROTEGIDA: Filtra por Turma E Semestre vigente

    List<Inscricao> findInscricoesByTurmaIdAndSemestre(Long turmaId, String semestreAtual);

    Inscricao findInscricoesByAlunoAndTurma(Usuario aluno, Turma turma);
}
