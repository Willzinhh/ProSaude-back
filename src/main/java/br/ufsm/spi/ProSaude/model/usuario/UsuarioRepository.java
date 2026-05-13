package br.ufsm.spi.ProSaude.model.usuario;

import br.ufsm.spi.ProSaude.dto.usuario.UsuarioResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);

    Usuario findUsuarioById(long id);
    List<Usuario> findByPerfilIn(List<Perfil> perfil);

    List<UsuarioResponseDTO> findAlunoByPerfil(String aluno);

    UsuarioResponseDTO findUsuarioDTOById(long id);

    Usuario findByCPF(String cpf);
}