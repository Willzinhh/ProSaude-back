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

    boolean existsByAlunoIdAndSemestreAndStatus(Long aluno_id, String semestre, StatusInscricao status);

    long countByTurmaIdAndSemestreAndStatus(Long turma_id, String semestre, StatusInscricao status);

    Optional<Inscricao> findFirstByTurmaIdAndSemestreAndStatusOrderByDataInscricaoAsc(Long turma_id, String semestre, StatusInscricao status);

    // Conta o número de inscrições de uma turma filtrado pelo status
    long countByTurmaIdAndStatus(Long turmaId, StatusInscricao status);

    // Busca as inscrições de uma turma pelo status ordenando pelo ID (ordem de inserção/chegada)
    List<Inscricao> findByTurmaIdAndStatusOrderByIdAsc(Long turmaId, StatusInscricao status);
}
