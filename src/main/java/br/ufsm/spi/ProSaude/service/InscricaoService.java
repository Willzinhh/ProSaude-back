package br.ufsm.spi.ProSaude.service;

import br.ufsm.spi.ProSaude.dto.inscricao.CadastroInscricaoDTO;
import br.ufsm.spi.ProSaude.dto.inscricao.InscritoDTO;
import br.ufsm.spi.ProSaude.dto.turma.HistoricoTurmaDTO;
import br.ufsm.spi.ProSaude.model.inscricao.Inscricao;
import br.ufsm.spi.ProSaude.model.inscricao.InscricaoRepository;
import br.ufsm.spi.ProSaude.model.inscricao.StatusInscricao;
import br.ufsm.spi.ProSaude.model.turma.Turma;
import br.ufsm.spi.ProSaude.model.turma.TurmaRepository;
import br.ufsm.spi.ProSaude.model.usuario.Perfil;
import br.ufsm.spi.ProSaude.model.usuario.Usuario;
import br.ufsm.spi.ProSaude.model.usuario.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class InscricaoService {

    @Autowired
    private InscricaoRepository inscricaoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TurmaRepository turmaRepository;

    @Transactional
    public void processarInscricaoEAutoCadastro(CadastroInscricaoDTO dto) {
        Usuario usuario = null;

        if (dto.getAlunoId() != null) {
            usuario = usuarioRepository.findById(dto.getAlunoId()).orElse(null);
        } else if (dto.getCpf() != null && !dto.getCpf().isEmpty()) {
            usuario = usuarioRepository.findByCPF(dto.getCpf());
        }

        if (usuario == null) {
            usuario = new Usuario();
            usuario.setNome(dto.getNome());
            usuario.setEmail(dto.getEmail());
            usuario.setPerfil(Perfil.ALUNO);
            usuario.setSenha(passwordEncoder.encode(dto.getCpf().replaceAll("\\D", "")));
            usuario.setCPF(dto.getCpf());
            usuario.setTelefone(dto.getTelefone());
            usuario.setTelefoneEmergencia(dto.getContatoEmergencia());
            usuario.setObservacaoMedica(dto.getDoencasCronicas());
            usuario.setDataNascimento(LocalDate.parse(dto.getDataNascimento()));

            usuario = usuarioRepository.save(usuario);
        }

        // Bloqueia se o aluno já possui VAGA GARANTIDA em qualquer turma no mesmo semestre
        boolean jaPossuiVagaGarantida = inscricaoRepository
                .existsByAlunoIdAndSemestreAndStatus(
                        usuario.getId(),
                        dto.getSemestre(),
                        StatusInscricao.VAGA_GARANTIDA
                );

        if (jaPossuiVagaGarantida) {
            throw new IllegalArgumentException("Inscrição Negada: Você já possui uma vaga garantida em uma turma para o semestre " + dto.getSemestre());
        }

        Turma turma = turmaRepository.findById(dto.getTurmaId())
                .orElseThrow(() -> new RuntimeException("Turma não encontrada"));

        // Evita inscrição duplicada na mesma turma e semestre
        Inscricao inscricaoExistente = inscricaoRepository.findInscricoesByAlunoAndTurma(usuario, turma);
        if (inscricaoExistente != null && inscricaoExistente.getSemestre().equals(dto.getSemestre())) {
            throw new IllegalArgumentException("Você já está inscrito ou na fila de espera desta turma.");
        }

        Inscricao inscricao = new Inscricao();
        inscricao.setAluno(usuario);
        inscricao.setTurma(turma);
        inscricao.setSemestre(dto.getSemestre());
        inscricao.setDataInscricao(LocalDate.now());

        // TODOS os alunos entram em FILA_DE_ESPERA aguardando confirmação do bolsista
        inscricao.setStatus(StatusInscricao.FILA_DE_ESPERA);

        inscricaoRepository.save(inscricao);
    }

    // NOVO MÉTODO: Bolsista confirma a vaga do aluno que está na fila de espera
    @Transactional
    public void confirmarVaga(Long turmaId, Long alunoId) {
        Usuario aluno = usuarioRepository.findById(alunoId)
                .orElseThrow(() -> new NoSuchElementException("Aluno não encontrado com o ID: " + alunoId));

        Turma turma = turmaRepository.findById(turmaId)
                .orElseThrow(() -> new NoSuchElementException("Turma não encontrada com o ID: " + turmaId));

        Inscricao inscricao = inscricaoRepository.findInscricoesByAlunoAndTurma(aluno, turma);
        if (inscricao == null) {
            throw new NoSuchElementException("Inscrição não encontrada para este aluno nesta turma.");
        }

        if (inscricao.getStatus() == StatusInscricao.VAGA_GARANTIDA) {
            throw new IllegalArgumentException("Este aluno já possui vaga confirmada nesta turma.");
        }

        // Verifica se o aluno já não foi confirmado em OUTRA turma neste mesmo semestre
        boolean jaPossuiVagaNoSemestre = inscricaoRepository.existsByAlunoIdAndSemestreAndStatus(
                alunoId, inscricao.getSemestre(), StatusInscricao.VAGA_GARANTIDA);
        if (jaPossuiVagaNoSemestre) {
            throw new IllegalArgumentException("Este aluno já possui vaga confirmada em outra turma neste semestre.");
        }

        // Verifica se ainda há vagas disponíveis na turma
        long matriculadosComVaga = inscricaoRepository.countByTurmaIdAndSemestreAndStatus(
                turma.getId(), inscricao.getSemestre(), StatusInscricao.VAGA_GARANTIDA);

        if (matriculadosComVaga >= turma.getVagas()) {
            throw new IllegalStateException("Não há mais vagas disponíveis nesta turma.");
        }

        inscricao.setStatus(StatusInscricao.VAGA_GARANTIDA);
        inscricaoRepository.save(inscricao);
    }

    @Transactional
    public void deletar(Long turmaId, Long alunoId) {
        Usuario usuario = usuarioRepository.findById(alunoId)
                .orElseThrow(() -> new NoSuchElementException("Aluno não encontrado com o ID: " + alunoId));

        Turma turma = turmaRepository.findById(turmaId)
                .orElseThrow(() -> new NoSuchElementException("Turma não encontrada com o ID: " + turmaId));

        Inscricao inscricaoRemovida = inscricaoRepository.findInscricoesByAlunoAndTurma(usuario, turma);

        if (inscricaoRemovida == null) {
            throw new NoSuchElementException("Inscrição não encontrada para este aluno nesta turma");
        }

        // Apenas remove a inscrição. A vaga liberada só será preenchida quando o bolsista confirmar outro aluno.
        this.inscricaoRepository.deleteById(inscricaoRemovida.getId());
    }

    public List<InscritoDTO> listarAlunosPorTurma(Long turmaId) {
        List<Inscricao> inscricoes = inscricaoRepository.findByTurmaIdOrderByDataInscricaoAscIdAsc(turmaId);

        return inscricoes.stream()
                .map(inscricao -> new InscritoDTO(
                        inscricao.getAluno().getId(),
                        inscricao.getAluno().getNome(),
                        inscricao.getAluno().getTelefone(),
                        inscricao.getAluno().getTelefoneEmergencia(),
                        inscricao.getDataInscricao(),
                        inscricao.getStatus()
                ))
                .toList();
    }

    public Turma listarTurmaPorAluno(Long id, String semestre) {
        Usuario u = usuarioRepository.findUsuarioById(id);
        List<Inscricao> inscricoes = inscricaoRepository.findInscricoesByAlunoAndSemestre(u, semestre);

        if (inscricoes.isEmpty()) {
            return null;
        }

        Inscricao primeiraInscricao = inscricoes.get(0);
        return turmaRepository.findTurmaById(primeiraInscricao.getTurma().getId());
    }

    public List<HistoricoTurmaDTO> listarHistoricoDoAluno(Long alunoId) {
        Usuario u = usuarioRepository.findUsuarioById(alunoId);
        List<Inscricao> historico = inscricaoRepository.findByAluno(u);

        LocalDate agora = LocalDate.now();
        int anoAtual = agora.getYear();
        String semestreAtual = anoAtual + "/" + (agora.getMonthValue() <= 6 ? "1" : "2");

        return historico.stream()
                .map(ins -> {
                    String statusFinal = ins.getSemestre().equals(semestreAtual)
                            ? ins.getStatus().name()
                            : "INATIVO";

                    return new HistoricoTurmaDTO(
                            ins.getTurma().getId(),
                            ins.getTurma().getNome(),
                            ins.getSemestre(),
                            statusFinal
                    );
                })
                .toList();
    }



    public List<InscritoDTO> listarAlunosPorTurmaNoSemestreAtual(Long turmaId) {
        LocalDate agora = LocalDate.now();
        int anoAtual = agora.getYear();
        String semestreAtual = anoAtual + "/" + (agora.getMonthValue() <= 6 ? "1" : "2");

        List<Inscricao> inscricoes = inscricaoRepository
                .findInscricoesByTurmaIdAndSemestre(turmaId, semestreAtual);

        return inscricoes.stream()
                .map(inscricao -> new InscritoDTO(
                        inscricao.getAluno().getId(),
                        inscricao.getAluno().getNome(),
                        inscricao.getAluno().getTelefone(),
                        inscricao.getAluno().getTelefoneEmergencia(),
                        inscricao.getDataInscricao(),
                        inscricao.getStatus()
                ))
                .toList();
    }
}