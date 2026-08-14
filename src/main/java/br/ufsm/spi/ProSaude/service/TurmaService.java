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

    private final InscricaoService inscricaoService;
    private TurmaRepository repository;
    private UsuarioRepository usuarioRepository;

    public Turma salvar(TurmaRequestDTO dados) {
        Turma novaTurma = new Turma();
        novaTurma.setNome(dados.nome());
        novaTurma.setDescricao(dados.descricao());
        novaTurma.setVagas((long) dados.vagas());
        novaTurma.setSemestre(dados.semestre());
        novaTurma.setBolsista_responsavel(dados.bolsista_responsavel());
        novaTurma.setHoraInicio(dados.horaInicio());
        novaTurma.setHoraFim(dados.horaFim());
        novaTurma.setSEGUNDA(dados.SEGUNDA());
        novaTurma.setTERCA(dados.TERCA());
        novaTurma.setQUARTA(dados.QUARTA());
        novaTurma.setQUINTA(dados.QUINTA());
        novaTurma.setSEXTA(dados.SEXTA());
        novaTurma.setSABADO(dados.SABADO());
        novaTurma.setDOMINGO(dados.DOMINGO());


        if (dados.bolsista_responsavel().getId() != null) {
            Usuario bolsista = usuarioRepository.findById(dados.bolsista_responsavel().getId())
                    .orElseThrow(() -> new RuntimeException("Bolsista não encontrado"));
            novaTurma.setBolsista_responsavel(bolsista);
        }
        Turma turma1 = repository.getTurmaById(dados.id());
        if (turma1 == null) {
            return repository.save(novaTurma);
        } else {
            turma1.setDescricao(novaTurma.getDescricao());
            turma1.setNome(novaTurma.getNome());
            turma1.setVagas(novaTurma.getVagas());
            turma1.setSemestre(novaTurma.getSemestre());
            turma1.setBolsista_responsavel(novaTurma.getBolsista_responsavel());
            turma1.setHoraInicio(novaTurma.getHoraInicio());
            turma1.setHoraFim(novaTurma.getHoraFim());
            turma1.setSEGUNDA(novaTurma.isSEGUNDA());
            turma1.setTERCA(novaTurma.isTERCA());
            turma1.setQUARTA(novaTurma.isQUARTA());
            turma1.setQUINTA(novaTurma.isQUINTA());
            turma1.setSEXTA(novaTurma.isSEXTA());
            turma1.setSABADO(novaTurma.isSABADO());
            turma1.setDOMINGO(novaTurma.isDOMINGO());
        }
        repository.save(turma1);
        inscricaoService.reavaliarFilaDeEspera(turma1.getId());

        return turma1;
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
            throw new NoSuchElementException("Turma não encontrada");
        }
        turma.setBolsista_responsavel(null);

        this.repository.deleteById(id);
    }

    public List<Turma> buscarPorUsuario(int id, String semestreFormatado) {
        return repository.buscarMinhasTurmasbySemestre(id, semestreFormatado);
    }

    public List<Turma> listarTurmasPorSemestre(String semestreFormatado) {
        return repository.findBySemestre(semestreFormatado);
    }

    public List<Turma> buscarPorBolsista(int id) {
        return repository.buscarMinhasTurmas(id);
    }
}
