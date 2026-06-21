package br.ufsm.spi.ProSaude.service;

import br.ufsm.spi.ProSaude.dto.inscricao.CadastroInscricaoDTO;
import br.ufsm.spi.ProSaude.dto.inscricao.InscritoDTO;
import br.ufsm.spi.ProSaude.dto.turma.HistoricoTurmaDTO;
import br.ufsm.spi.ProSaude.model.inscricao.Inscricao;
import br.ufsm.spi.ProSaude.model.inscricao.InscricaoRepository;
import br.ufsm.spi.ProSaude.model.turma.Turma;
import br.ufsm.spi.ProSaude.model.turma.TurmaRepository;
import br.ufsm.spi.ProSaude.model.usuario.Perfil;
import br.ufsm.spi.ProSaude.model.usuario.Usuario;
import br.ufsm.spi.ProSaude.model.usuario.UsuarioRepository;
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
        System.out.println("Service Inscrição - Semestre: " + dto.getSemestre());
        Usuario usuario = null;

        if (dto.getAlunoId() != null) {
            usuario = usuarioRepository.findById(dto.getAlunoId()).orElse(null);
        }
        else if (dto.getCpf() != null && !dto.getCpf().isEmpty()) {
            usuario = usuarioRepository.findByCPF(dto.getCpf());
        }

        if (usuario == null) {
            System.out.println("Cadastrando Novo Usuário Aluno...");
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
        } else {
            System.out.println("Usuário existente encontrado: " + usuario.getNome() + ". Reaproveitando dados históricos.");
        }

        boolean jaInscrito = inscricaoRepository.existsByAlunoIdAndSemestre(usuario.getId(), dto.getSemestre());

        if (jaInscrito) {
            throw new IllegalArgumentException("Inscrição Negada: Você já possui uma inscrição ativa nesta ou em outra modalidade para o semestre " + dto.getSemestre());
        }

        Inscricao inscricao = new Inscricao();
        inscricao.setAluno(usuario);
        inscricao.setTurma(turmaRepository.findById(dto.getTurmaId())
                .orElseThrow(() -> new RuntimeException("Turma não encontrada")));
        inscricao.setSemestre(dto.getSemestre());
        inscricao.setDataInscricao(LocalDate.now());
        inscricao.setStatus("ATIVO");

        inscricaoRepository.save(inscricao);
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

        // 🎯 Em vez de lançar RuntimeException e quebrar a tela, retorne null!
        if (inscricoes.isEmpty()) {
            System.out.println("Aviso: Nenhuma inscrição ativa para o aluno " + id + " no semestre " + semestre);
            return null;
        }

        Inscricao primeiraInscricao = inscricoes.get(0);
        return turmaRepository.findTurmaById(primeiraInscricao.getTurma().getId());
    }

    public List<HistoricoTurmaDTO> listarHistoricoDoAluno(Long alunoId) {
        Usuario u = usuarioRepository.findUsuarioById(alunoId);

        // 1. Busca todas as inscrições dele no banco
        List<Inscricao> historico = inscricaoRepository.findByAluno(u);

        // 2. Calcula dinamicamente o semestre atual do sistema (Ex: "2026/1")
        LocalDate agora = LocalDate.now();
        int anoAtual = agora.getYear();
        String semestreAtual = anoAtual + "/" + (agora.getMonthValue() <= 6 ? "1" : "2");

        // 3. Monta a lista definindo o status baseado no semestre
        return historico.stream()
                .map(ins -> {
                    // Se o semestre da inscrição for igual ao atual do sistema, mantém o status original.
                    // Caso contrário, força a exibição como "INATIVO" ou "CONCLUÍDO".
                    String statusFinal = ins.getSemestre().equals(semestreAtual)
                            ? ins.getStatus()
                            : "INATIVO"; // 🎯 Aqui a mágica acontece

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
        // 1. Calcula dinamicamente o semestre atual do sistema (Ex: "2026/1")
        LocalDate agora = LocalDate.now();
        int anoAtual = agora.getYear();
        String semestreAtual = anoAtual + "/" + (agora.getMonthValue() <= 6 ? "1" : "2");

        System.out.println("Bolsista listando alunos da Turma " + turmaId + " no semestre " + semestreAtual);

        // 2. Busca apenas as inscrições ATIVAS daquela turma NO SEMESTRE ATUAL
        List<Inscricao> inscricoes = inscricaoRepository
                .findInscricoesByTurmaIdAndSemestre(turmaId, semestreAtual);

        // 3. Converte para DTO
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

    public void deletar(Long turmaId, Long alunoId) {
        // 1. Busca os objetos e lança erro caso não existam no banco
        Usuario usuario = usuarioRepository.findById(alunoId)
                .orElseThrow(() -> new NoSuchElementException("Aluno não encontrado com o ID: " + alunoId));

        Turma turma = turmaRepository.findById(turmaId)
                .orElseThrow(() -> new NoSuchElementException("Turma não encontrada com o ID: " + turmaId));

        // 2. Agora sim, passa as entidades puras (usuario, turma) extraídas do Optional
        Inscricao i = inscricaoRepository.findInscricoesByAlunoAndTurma(usuario, turma);

        if (i == null) {
            throw new NoSuchElementException("Inscrição não encontrada para este aluno nesta turma");
        }

        // 3. Deleta com sucesso
        this.inscricaoRepository.deleteById(i.getId());
    }
}