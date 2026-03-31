package br.ufsm.spi.ProSaude.service;

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

    public Usuario salvar(Usuario user) {
        user.setSenha(new BCryptPasswordEncoder().encode(user.getSenha()));
        this.repository.save(user);
        return user;
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
