package br.ufsm.spi.ProSaude.model.dadosAluno;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DadosAlunoRepository extends JpaRepository<DadosAluno, Long> {


    DadosAluno findAlunoById(Long id);

    DadosAluno findByCPF(String cpf);
}
