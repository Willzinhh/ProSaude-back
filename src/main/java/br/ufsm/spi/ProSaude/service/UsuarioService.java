package br.ufsm.spi.ProSaude.service;

import br.ufsm.spi.ProSaude.dto.usuario.UsuarioRequestDTO;
import br.ufsm.spi.ProSaude.dto.usuario.UsuarioResponseDTO;
import br.ufsm.spi.ProSaude.model.usuario.Perfil;
import br.ufsm.spi.ProSaude.model.usuario.Usuario;
import br.ufsm.spi.ProSaude.model.usuario.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UsuarioService {


    private final UsuarioRepository repository;

    public Usuario salvar(UsuarioRequestDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setSenha(new BCryptPasswordEncoder().encode(dto.senha()));
        usuario.setPerfil(Perfil.valueOf(dto.perfil()));

        if ("ALUNO".equals(dto.perfil()) && dto.perfil() != null) {
            usuario.setTelefone(dto.telefone());
            usuario.setTelefoneEmergencia(dto.telefoneEmergencia());
            usuario.setCPF(dto.cpf());
            usuario.setDataNascimento(dto.dataNascimento());
            usuario.setObservacaoMedica(dto.observacaoMedic());


        }

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

    public Usuario buscar(long id) {
        // Busca a entidade Usuario direto pelo findById nativo do Spring Data
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado com o ID: " + id));

        // Converte manualmente ou via construtor para o seu UsuarioResponseDTO 🎯
        // Ajuste os parâmetros abaixo de acordo com o construtor exato do seu UsuarioResponseDTO
        return usuario;
    }


    public void excluir(long id) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String emailLogado;

        if (principal instanceof UserDetails) {
            emailLogado = ((UserDetails) principal).getUsername();
        } else {
            emailLogado = principal.toString();
        }

        // 2. Busca o usuário logado para descobrir o ID dele
        Optional<Usuario> usuarioLogado = repository.findByEmail(emailLogado); // Certifique-se de que tem esse método no seu repository

        if (usuarioLogado == null) {
            throw new NoSuchElementException("Usuário autenticado não foi encontrado no sistema.");
        }

        // 3. 🎯 VALIDAÇÃO CRUCIAL: Se o ID para exclusão for igual ao ID do logado, joga uma exceção
        if (usuarioLogado.get().getId() == id) {
            throw new IllegalArgumentException("Operação inválida: Você não pode excluir o seu próprio usuário.");
        }

        // 4. Se passou pela validação, o fluxo antigo segue normalmente
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
