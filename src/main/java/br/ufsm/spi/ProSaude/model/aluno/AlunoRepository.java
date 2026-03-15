package br.ufsm.spi.ProSaude.model.aluno;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {


    Aluno findAlunoById(Long id);
}
