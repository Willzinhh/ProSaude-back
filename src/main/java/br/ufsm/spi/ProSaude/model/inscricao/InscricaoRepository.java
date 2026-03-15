package br.ufsm.spi.ProSaude.model.inscricao;

import br.ufsm.spi.ProSaude.model.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InscricaoRepository extends JpaRepository<Inscricao, Long> {



}
