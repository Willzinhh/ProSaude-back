package br.ufsm.spi.ProSaude.service;

import br.ufsm.spi.ProSaude.model.dadosAluno.DadosAluno;
import br.ufsm.spi.ProSaude.model.dadosAluno.DadosAlunoRepository;
import br.ufsm.spi.ProSaude.model.turma.TurmaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AlunoService {

    private DadosAlunoRepository repository;

    private TurmaRepository turmaRepository;

    private InscricaoService inscricaoService;

    public DadosAluno salvar(DadosAluno dadosAluno) {
        return repository.save(dadosAluno);
    }

    public List<DadosAluno> listarTodas() {
        return repository.findAll();
    }


    public Optional<DadosAluno> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public void excluir(long id) {
        DadosAluno dadosAluno = repository.findAlunoById(id);
        if (dadosAluno == null) {
            throw new NoSuchElementException("Usuário não encontrado");
        }

        this.repository.deleteById(id);
    }

}
