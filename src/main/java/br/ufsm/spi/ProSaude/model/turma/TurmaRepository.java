package br.ufsm.spi.ProSaude.model.turma;

import br.ufsm.spi.ProSaude.model.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TurmaRepository extends JpaRepository<Turma, Long> {

    Turma findTurmaById(Long id);

    Turma getTurmaById(Long id);

    @Query("SELECT a FROM Turma a WHERE a.bolsistaResponsavel.id = :id")
    List<Turma> buscarMinhasTurmas(@Param("id") int idUsuarioLogado);
}
