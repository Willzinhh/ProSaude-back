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
import java.util.Optional;

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

        // 🎯 AJUSTE 1: Bloqueia APENAS se o aluno já possui VAGA GARANTIDA em qualquer turma no mesmo semestre
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

        // 🎯 AJUSTE 2: Verifica quantas vagas garantidas já foram ocupadas nessa turma
        long matriculadosComVaga = inscricaoRepository
                .countByTurmaIdAndSemestreAndStatus(
                        turma.getId(),
                        dto.getSemestre(),
                        StatusInscricao.VAGA_GARANTIDA
                );

        Inscricao inscricao = new Inscricao();
        inscricao.setAluno(usuario);
        inscricao.setTurma(turma);
        inscricao.setSemestre(dto.getSemestre());
        inscricao.setDataInscricao(LocalDate.now());

// 🎯 3. Atribui o Enum no status
        if (matriculadosComVaga < turma.getVagas()) {
            inscricao.setStatus(StatusInscricao.VAGA_GARANTIDA);
        } else {
            inscricao.setStatus(StatusInscricao.FILA_DE_ESPERA);
        }

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

        String statusAnterior = String.valueOf(inscricaoRemovida.getStatus());
        String semestre = inscricaoRemovida.getSemestre();

        // Deleta a inscrição solicitada
        this.inscricaoRepository.deleteById(inscricaoRemovida.getId());

        // 🎯 AJUSTE 3: Auto-promoção da fila de espera
        // Se a vaga libertada era GARANTIDA, busca o primeiro da Fila de Espera por ordem de data de inscrição e promove
        if ("VAGA_GARANTIDA".equals(statusAnterior)) {
            Optional<Inscricao> proximoDaFila = inscricaoRepository
                    .findFirstByTurmaIdAndSemestreAndStatusOrderByDataInscricaoAsc(turmaId, semestre, StatusInscricao.valueOf("FILA_DE_ESPERA"));

            if (proximoDaFila.isPresent()) {
                Inscricao promovido = proximoDaFila.get();
                promovido.setStatus(StatusInscricao.valueOf("VAGA_GARANTIDA"));
                inscricaoRepository.save(promovido);
            }
        }
    }

    public List<InscritoDTO> listarAlunosPorTurma(Long turmaId) {
        List<Inscricao> inscricoes = inscricaoRepository.findInscricoesByTurma(turmaId);

        return inscricoes.stream()
                .map(inscricao -> new InscritoDTO(
                        inscricao.getAluno().getId(),
                        inscricao.getAluno().getNome(),
                        inscricao.getAluno().getTelefone(),
                        inscricao.getAluno().getTelefoneEmergencia(),
                        inscricao.getDataInscricao()
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
                    // 🎯 Converte o Enum para String chamando .name()
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

    @Transactional
    public void reavaliarFilaDeEspera(Long turmaId) {
        Turma turma = turmaRepository.findById(turmaId)
                .orElseThrow(() -> new EntityNotFoundException("Turma não encontrada"));

        // 1. Busca todos os alunos garantidos ordenados por ordem de chegada (ID mais antigo primeiro)
        List<Inscricao> garantidos = inscricaoRepository.findByTurmaIdAndStatusOrderByIdAsc(
                turmaId, StatusInscricao.VAGA_GARANTIDA);

        int limiteVagas = Math.toIntExact(turma.getVagas());

        // CENÁRIO A: Diminuição de vagas (excedentes vão para a fila)
        if (garantidos.size() > limiteVagas) {
            // Pega os excedentes a partir do índice do limite até o final
            for (int i = limiteVagas; i < garantidos.size(); i++) {
                Inscricao inscricao = garantidos.get(i);
                inscricao.setStatus(StatusInscricao.FILA_DE_ESPERA);
                inscricaoRepository.save(inscricao);
            }
        }
        // CENÁRIO B: Aumento de vagas (promove quem está na fila)
        else if (garantidos.size() < limiteVagas) {
            long vagasDisponiveis = limiteVagas - garantidos.size();

            List<Inscricao> filaEspera = inscricaoRepository.findByTurmaIdAndStatusOrderByIdAsc(
                    turmaId, StatusInscricao.FILA_DE_ESPERA);

            for (int i = 0; i < Math.min(vagasDisponiveis, filaEspera.size()); i++) {
                Inscricao inscricao = filaEspera.get(i);
                inscricao.setStatus(StatusInscricao.VAGA_GARANTIDA);
                inscricaoRepository.save(inscricao);
            }
        }
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
                        inscricao.getDataInscricao()
                ))
                .toList();
    }

}