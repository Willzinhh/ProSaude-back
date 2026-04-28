package br.ufsm.spi.ProSaude.model.inscricao;

import br.ufsm.spi.ProSaude.dto.inscricao.InscritoDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface InscricaoRepository extends JpaRepository<Inscricao, Long> {

    // O Spring agora vai mapear corretamente: By + UsuarioId + And + Semestre
    boolean existsByAlunoIdAndSemestre(Long alunoId, String semestre);

    List<Inscricao> findByTurmaIdOrderByDataInscricaoAsc(Long turmaId);



    @Query("SELECT new br.ufsm.spi.ProSaude.dto.inscricao.InscritoDTO(" +
            "u.nome, " +
            "d.CPF, " +
            "d.telefone, " +
            "i.dataInscricao, " +
            "CAST((SELECT COUNT(p) FROM Presenca p WHERE p.inscricao = i AND p.presente = false) AS Long)) " +
            "FROM Inscricao i " +
            "JOIN i.aluno u " +
            "JOIN DadosAluno d ON d.usuario = u " +
            "WHERE i.turma.id = :turmaId " +
            "ORDER BY i.dataInscricao ASC") // Ordenação simples por data para validar
    List<InscritoDTO> findInscritosByTurma(@Param("turmaId") Long turmaId);


    Optional<Inscricao> findById(Long id);
}
