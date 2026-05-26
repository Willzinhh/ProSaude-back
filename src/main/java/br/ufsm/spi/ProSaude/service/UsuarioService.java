package br.ufsm.spi.ProSaude.service;

import br.ufsm.spi.ProSaude.dto.usuario.UsuarioRequestDTO;
import br.ufsm.spi.ProSaude.dto.usuario.UsuarioResponseDTO;
import br.ufsm.spi.ProSaude.model.usuario.Perfil;
import br.ufsm.spi.ProSaude.model.usuario.Usuario;
import br.ufsm.spi.ProSaude.model.usuario.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class UsuarioService {


    private final UsuarioRepository repository;

    public Usuario salvar(UsuarioRequestDTO dto) {
        // 1. Instancia o Usuário
        Usuario usuario = new Usuario();
        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setSenha(new BCryptPasswordEncoder().encode(dto.senha())); // Lembre-se do BCrypt aqui se tiver segurança
        usuario.setPerfil(Perfil.valueOf(dto.perfil()));

        // 2. Se for ALUNO e tiver dados, instancia o DadosAluno
        if ("ALUNO".equals(dto.perfil()) && dto.perfil() != null) {
            usuario.setTelefone(dto.telefone());
            usuario.setTelefoneEmergencia(dto.telefoneEmergencia());
            usuario.setCPF(dto.cpf());
            usuario.setDataNascimento(dto.dataNascimento());
            usuario.setObservacaoMedica(dto.observacaoMedic());


        }

        // 3. Salva o usuário. O "cascade = ALL" salvará os dados automaticamente na outra tabela.

        this.repository.save(usuario);
        return usuario;
    }

    public List listar() {
        List<Usuario> users = repository.findAll();
        if (users.isEmpty()) {
            throw new NoSuchElementException("Usuário não encontrado");
        }
        return users;
    }

    public List<Usuario> listarEquipe() {
        List<Perfil> perfisEquipe = Arrays.asList(Perfil.COORDENADOR, Perfil.BOLSISTA);
        return repository.findByPerfilIn(perfisEquipe);
    }

    public List<UsuarioResponseDTO> listarAlunos() {
        return repository.findAlunoByPerfil("ALUNO");
    }

    public UsuarioResponseDTO buscar(long id) {
        return repository.findUsuarioDTOById(id);

    }


    public void excluir(long id) {
        Usuario user = repository.findUsuarioById(id);
        if (user == null) {
            throw new NoSuchElementException("Usuário não encontrado");
        }

        this.repository.deleteById(id);
    }

    public void salvarSenha(Long id, String senhaNoJson) {
        Usuario u = repository.findUsuarioById(id);
        u.setSenha(new BCryptPasswordEncoder().encode(senhaNoJson));
        u.setPrimeiroAcesso(false);
        repository.save(u);
    }

    public Usuario editar(UsuarioRequestDTO dto) {
        Usuario usuario = repository.findById(dto.id())
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado com o ID: " + dto.id()));

        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setPerfil(Perfil.valueOf(dto.perfil()));
        usuario.setPrimeiroAcesso(dto.primeiroAcesso());

        if (dto.primeiroAcesso() != null && dto.primeiroAcesso()) {
            usuario.setPrimeiroAcesso(true);
            usuario.setSenha(new BCryptPasswordEncoder().encode("bolsista123"));
        }
        if (dto.senha() != null && !dto.senha().isBlank() && !dto.senha().equals("bolsista123")) {
            usuario.setSenha(new BCryptPasswordEncoder().encode(dto.senha()));
        }

        if ("ALUNO".equals(dto.perfil())) {
            usuario.setTelefone(dto.telefone());
            usuario.setTelefoneEmergencia(dto.telefoneEmergencia());
            usuario.setCPF(dto.cpf());
            usuario.setDataNascimento(dto.dataNascimento());
            usuario.setObservacaoMedica(dto.observacaoMedic());
        } else {
            usuario.setTelefone(null);
            usuario.setTelefoneEmergencia(null);
            usuario.setCPF(null);
            usuario.setDataNascimento(null);
            usuario.setObservacaoMedica(null);
        }

        return this.repository.save(usuario);
    }

}
