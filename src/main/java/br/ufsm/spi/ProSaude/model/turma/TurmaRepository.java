package br.ufsm.spi.ProSaude.model.turma;

import br.ufsm.spi.ProSaude.model.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TurmaRepository extends JpaRepository<Turma, Long> {
    Optional<Turma> findByCodigo(String nomeTurmaNoCSV);

    Turma findTurmaById(Long id);

    Turma getTurmaById(Long id);
}
