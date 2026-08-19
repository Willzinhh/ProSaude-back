package br.ufsm.spi.ProSaude.model.chamada;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChamadaRepository extends JpaRepository<Chamada, Long> {

    List<Chamada> findByTurmaIdOrderByDataDesc(Long turmaId);

    Optional<Chamada> findByTurmaIdAndData(Long turmaId, LocalDate data);
}
