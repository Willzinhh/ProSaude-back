package br.ufsm.spi.ProSaude.service;
import br.ufsm.spi.ProSaude.model.chamada.Chamada;
import br.ufsm.spi.ProSaude.model.chamada.ChamadaDto;
import br.ufsm.spi.ProSaude.model.chamada.ChamadaRepository;
import br.ufsm.spi.ProSaude.model.chamada.ChamadaRequest;
import br.ufsm.spi.ProSaude.model.presenca.Presenca;
import br.ufsm.spi.ProSaude.model.presenca.PresencaItemDto;
import br.ufsm.spi.ProSaude.model.turma.Turma;
import br.ufsm.spi.ProSaude.model.usuario.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.ufsm.spi.ProSaude.model.turma.TurmaRepository;
import br.ufsm.spi.ProSaude.model.usuario.UsuarioRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChamadaService {

    @Autowired
    private ChamadaRepository chamadaRepository;

    @Autowired
    private TurmaRepository turmaRepository;

    @Autowired
    private UsuarioRepository alunoRepository;

    @Transactional
    public Chamada salvar(ChamadaRequest request) {
        Turma turma = turmaRepository.findById(request.getTurmaId())
                .orElseThrow(() -> new IllegalArgumentException("Turma não encontrada. ID: " + request.getTurmaId()));

        // Verifica se já existe chamada registrada para a mesma turma na mesma data
        Optional<Chamada> chamadaExistente = chamadaRepository.findByTurmaIdAndData(turma.getId(), request.getData());

        Chamada chamada;
        if (chamadaExistente.isPresent()) {
            chamada = chamadaExistente.get();
            chamada.getPresencas().clear(); // Limpa presenças antigas para atualizar
        } else {
            chamada = new Chamada();
            chamada.setTurma(turma);
            chamada.setData(request.getData());
        }

        for (ChamadaRequest.PresencaRequest item : request.getPresencas()) {
            Usuario aluno = alunoRepository.findById(item.getAlunoId())
                    .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado. ID: " + item.getAlunoId()));

            Presenca presenca = new Presenca();
            presenca.setChamada(chamada);
            presenca.setAluno(aluno);
            presenca.setPresente(item.getPresente());

            chamada.getPresencas().add(presenca);
        }

        return chamadaRepository.save(chamada);
    }

    @Transactional
    public Chamada atualizar(Long id, ChamadaRequest request) {
        Chamada chamada = chamadaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Chamada não encontrada. ID: " + id));

        chamada.setData(request.getData());
        chamada.getPresencas().clear();

        for (ChamadaRequest.PresencaRequest item : request.getPresencas()) {
            Usuario aluno = alunoRepository.findById(item.getAlunoId())
                    .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado. ID: " + item.getAlunoId()));

            Presenca presenca = new Presenca();
            presenca.setChamada(chamada);
            presenca.setAluno(aluno);
            presenca.setPresente(item.getPresente());

            chamada.getPresencas().add(presenca);
        }

        return chamadaRepository.save(chamada);
    }

    @Transactional(readOnly = true)
    public List<Chamada> listarPorTurma(Long turmaId) {
        return chamadaRepository.findByTurmaIdOrderByDataDesc(turmaId);
    }

    @Transactional(readOnly = true)
    public Optional<Chamada> buscarPorTurmaEData(Long turmaId, LocalDate data) {
        return chamadaRepository.findByTurmaIdAndData(turmaId, data);
    }

    @Transactional(readOnly = true)
    public Chamada buscarPorId(Long id) {
        return chamadaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Chamada não encontrada. ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<ChamadaDto> buscarPorTurma(Long turmaId) {
        List<Chamada> chamadas = chamadaRepository.findByTurmaIdOrderByDataDesc(turmaId);

        return chamadas.stream().map(chamada -> {
            List<PresencaItemDto> presencasDto = chamada.getPresencas().stream()
                    .map(presenca -> new PresencaItemDto(
                            presenca.getAluno().getId(),
                            presenca.getPresente()
                    ))
                    .collect(Collectors.toList());

            return new ChamadaDto(
                    chamada.getId(),
                    chamada.getTurma().getId(),
                    chamada.getData(),
                    presencasDto
            );
        }).collect(Collectors.toList());
    }

    @Transactional
    public ChamadaDto registrarChamada(ChamadaDto dto) {
        Turma turma = turmaRepository.findById(dto.getTurmaId())
                .orElseThrow(() -> new IllegalArgumentException("Turma não encontrada. ID: " + dto.getTurmaId()));

        // Verifica se já existe uma chamada para a mesma turma nesta data
        Chamada chamada = chamadaRepository.findByTurmaIdAndData(dto.getTurmaId(), dto.getData())
                .orElseGet(() -> {
                    Chamada nova = new Chamada();
                    nova.setTurma(turma);
                    nova.setData(dto.getData());
                    return nova;
                });

        // Limpa a lista antiga caso seja um update para evitar duplicidades
        chamada.getPresencas().clear();

        // Mapeia os itens do DTO para entidades Presenca
        if (dto.getPresencas() != null) {
            for (PresencaItemDto item : dto.getPresencas()) {
                Usuario aluno = alunoRepository.findById(item.getAlunoId())
                        .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado. ID: " + item.getAlunoId()));

                Presenca presenca = new Presenca();
                presenca.setChamada(chamada);
                presenca.setAluno(aluno);
                presenca.setPresente(item.getPresente());

                chamada.getPresencas().add(presenca);
            }
        }

        // Persiste no banco de dados
        Chamada chamadaSalva = chamadaRepository.save(chamada);

        // Converte a entidade salva de volta para ChamadaDto
        List<PresencaItemDto> presencasDto = chamadaSalva.getPresencas().stream()
                .map(p -> new PresencaItemDto(p.getAluno().getId(), p.getPresente()))
                .collect(Collectors.toList());

        return new ChamadaDto(
                chamadaSalva.getId(),
                chamadaSalva.getTurma().getId(),
                chamadaSalva.getData(),
                presencasDto
        );
    }

    @Transactional
    public void atualizarChamada(Long id, ChamadaDto dto) {
        Chamada chamada = chamadaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Chamada não encontrada com o ID: " + id));

        // Atualiza a data caso tenha mudado
        chamada.setData(dto.getData());

        // Limpa a lista existente para dar lugar às presenças atualizadas (orphanRemoval cuida da deleção no banco)
        chamada.getPresencas().clear();

        // Recria a lista de presenças atualizada
        if (dto.getPresencas() != null) {
            for (PresencaItemDto item : dto.getPresencas()) {
                Usuario aluno = alunoRepository.findById(item.getAlunoId())
                        .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado com o ID: " + item.getAlunoId()));

                Presenca presenca = new Presenca();
                presenca.setChamada(chamada);
                presenca.setAluno(aluno);
                presenca.setPresente(item.getPresente());

                chamada.getPresencas().add(presenca);
            }
        }

        chamadaRepository.save(chamada);
    }
}
