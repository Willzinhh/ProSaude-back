package br.ufsm.spi.ProSaude.model.avaliacao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {

    // Busca o histórico de avaliações de um aluno específico ordenado pela data mais recente
    List<Avaliacao> findByAlunoIdOrderByDataAvaliacaoDesc(Long alunoId);
}