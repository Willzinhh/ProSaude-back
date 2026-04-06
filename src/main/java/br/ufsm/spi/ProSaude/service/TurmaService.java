package br.ufsm.spi.ProSaude.service;

import br.ufsm.spi.ProSaude.dto.turma.TurmaRequestDTO;
import br.ufsm.spi.ProSaude.model.turma.Turma;
import br.ufsm.spi.ProSaude.model.turma.TurmaRepository;
import br.ufsm.spi.ProSaude.model.usuario.Usuario;
import br.ufsm.spi.ProSaude.model.usuario.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TurmaService {

    private TurmaRepository repository;
    private UsuarioRepository usuarioRepository;

    public Turma salvar(TurmaRequestDTO dados) {
        Turma novaTurma = new Turma();
        novaTurma.setNome(dados.nome());
        novaTurma.setDescricao(dados.descricao());
        novaTurma.setHoraInicio(dados.horaInicio());
        novaTurma.setHoraFim(dados.horaFim());
        novaTurma.setDiasSemana(dados.diasSemana());

        if (dados.bolsistaResponsavel().getId() != null) {
            Usuario bolsista = usuarioRepository.findById(dados.bolsistaResponsavel().getId())
                    .orElseThrow(() -> new RuntimeException("Bolsista não encontrado"));
            novaTurma.setBolsistaResponsavel(bolsista); // Agora você seta o OBJETO, não o ID
        }
        Turma turma1 = repository.getTurmaById(dados.id());
        if ( turma1 == null ) {
            return repository.save(novaTurma);
        }
        else {
            turma1.setDescricao(novaTurma.getDescricao());
            turma1.setNome(novaTurma.getNome());
            turma1.setBolsistaResponsavel(novaTurma.getBolsistaResponsavel());
            turma1.setHoraInicio(novaTurma.getHoraInicio());
            turma1.setHoraFim(novaTurma.getHoraFim());
            turma1.setDiasSemana(novaTurma.getDiasSemana());
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

    public List<Turma> buscarPorUsuario(int id) {
        return repository.buscarMinhasTurmas(id);
    }
}
