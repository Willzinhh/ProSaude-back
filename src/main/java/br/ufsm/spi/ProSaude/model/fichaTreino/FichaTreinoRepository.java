package br.ufsm.spi.ProSaude.model.fichaTreino;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FichaTreinoRepository extends JpaRepository<FichaTreino, Long> {
    Optional<FichaTreino> findByTurmaIdAndAlunoId(Long turmaId, Long alunoId);
}