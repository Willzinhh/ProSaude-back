package br.ufsm.spi.ProSaude.service;

import br.ufsm.spi.ProSaude.model.aluno.Aluno;
import br.ufsm.spi.ProSaude.model.aluno.AlunoRepository;
import br.ufsm.spi.ProSaude.model.turma.Turma;
import br.ufsm.spi.ProSaude.model.turma.TurmaRepository;
import br.ufsm.spi.ProSaude.model.usuario.Usuario;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AlunoService {

    private AlunoRepository repository;

    private TurmaRepository turmaRepository;

    private InscricaoService inscricaoService;

    public Aluno salvar(Aluno aluno) {
        return repository.save(aluno);
    }

    public List<Aluno> listarTodas() {
        return repository.findAll();
    }


    public Optional<Aluno> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public void excluir(long id) {
        Aluno aluno = repository.findAlunoById(id);
        if (aluno == null) {
            throw new NoSuchElementException("Usuário não encontrado");
        }

        this.repository.deleteById(id);
    }

}
