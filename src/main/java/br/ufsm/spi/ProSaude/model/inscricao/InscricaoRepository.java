package br.ufsm.spi.ProSaude.model.inscricao;

import br.ufsm.spi.ProSaude.model.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InscricaoRepository extends JpaRepository<Inscricao, Long> {

    boolean existsByAlunoIdAndSemestre(Long alunoId, String semestre);

    Inscricao findInscricaoByIdAndSemestre(Long id, String semestre);

    List<Inscricao> findByTurmaIdOrderByDataInscricaoAsc(Long turmaId);


    @Query("SELECT i FROM Inscricao i JOIN FETCH i.aluno WHERE i.turma.id = :turmaId")
    List<Inscricao> findInscricoesByTurma(@Param("turmaId") Long turmaId);

    Inscricao findInscricaoByAlunoAndSemestre(Usuario aluno, String semestre);
}
