package br.ufsm.spi.ProSaude.service;

import br.ufsm.spi.ProSaude.model.turma.Turma;
import br.ufsm.spi.ProSaude.model.turma.TurmaRepository;
import br.ufsm.spi.ProSaude.model.usuario.Usuario;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TurmaService {

    private TurmaRepository repository;

    public Turma salvar(Turma turma) {
        System.out.println(turma.getDescricao()+"**********************************");
        Turma turma1 = repository.getTurmaById(turma.getId());
        if ( turma1 == null ) {
            return repository.save(turma);
        }
        else {
            turma1.setCodigo(turma.getCodigo());
            turma1.setDescricao(turma.getDescricao());
            turma1.setNome(turma.getNome());
        }
        return repository.save(turma1);
    }

    public List<Turma> listarTodas() {
        return repository.findAll();
    }


    public Optional<Turma> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public void excluir(long id) {
        Turma turma = repository.findTurmaById(id);
        if (turma == null) {
            throw new NoSuchElementException("Usuário não encontrado");
        }

        this.repository.deleteById(id);
    }
}
