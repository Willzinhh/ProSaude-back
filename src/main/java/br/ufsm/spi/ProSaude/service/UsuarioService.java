package br.ufsm.spi.ProSaude.service;

import br.ufsm.spi.ProSaude.dto.usuario.UsuarioRequestDTO;
import br.ufsm.spi.ProSaude.model.dadosAluno.DadosAluno;
import br.ufsm.spi.ProSaude.model.usuario.Perfil;
import br.ufsm.spi.ProSaude.model.usuario.Usuario;
import br.ufsm.spi.ProSaude.model.usuario.UsuarioRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
        if ("ALUNO".equals(dto.perfil()) && dto.dados() != null) {
            DadosAluno dados = new DadosAluno();
            dados.setTelefone(dto.dados().telefone());
            dados.setCPF(dto.dados().cpf());
            dados.setObservacaoMedica(dto.dados().observacaoMedica());
            dados.setDataNascimento(dto.dados().dataNascimento());

            // VÍNCULO IMPORTANTE:
            dados.setUsuario(usuario);
            usuario.setDados(dados);
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
        // Use o Enum diretamente
        List<Perfil> perfisEquipe = Arrays.asList(Perfil.BOLSISTA, Perfil.MONITOR);
        return repository.findByPerfilIn(perfisEquipe);
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
//
//    public DadosUserOutput editar(@Valid DadosUserInput userInput, long owner_id) {
//        User u = this.repository.findUserByOwnerAndId(owner_id, userInput.id());
//
//        if (u == null) {
//            System.out.println("id = " + userInput.id());
//            throw new NoSuchElementException("Usuário não encontrado");
//        }
//
//        u.setNome(userInput.nome());
//        u.setEmail(userInput.email());
//        u.setSenha(new BCryptPasswordEncoder().encode(userInput.senha()));
//        u.setPermissao(userInput.permissao());
//
//
//
//
//
//        return new DadosUserOutput(this.repository.save(u));
}
