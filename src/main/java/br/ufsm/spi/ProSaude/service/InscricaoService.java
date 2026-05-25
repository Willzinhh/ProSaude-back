package br.ufsm.spi.ProSaude.service;

import br.ufsm.spi.ProSaude.dto.inscricao.CadastroInscricaoDTO;
import br.ufsm.spi.ProSaude.dto.inscricao.InscritoDTO;
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
        System.out.print("service inscriço" + dto.getContatoEmergencia());
        Usuario dadosExistentes;

        dadosExistentes = usuarioRepository.findByCPF(dto.getCpf());

        Usuario usuario;

        if (dadosExistentes != null) {
            System.out.print("Dados Encontrados");
            // Se o CPF já existe, apenas pegamos o usuário vinculado a esses dados
            usuario = dadosExistentes;
        } else {
            System.out.print("Cadastrando Usuario");
            // 2. Se NÃO existe, criamos o Usuario novo
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


            // Salva o usuário (e os dados por cascata)
            usuario = usuarioRepository.save(usuario);
        }

        // 4. Validação de segurança: Aluno já inscrito neste semestre?
        boolean jaInscrito = inscricaoRepository.existsByAlunoIdAndSemestre(usuario.getId(), dto.getSemestre());

        if (jaInscrito) {
            throw new RuntimeException("Este CPF já possui uma inscrição para o semestre " + dto.getSemestre());
        }

        // 5. Criar a Inscrição
        Inscricao inscricao = new Inscricao();
        inscricao.setAluno(usuario);
        inscricao.setTurma(turmaRepository.findById(dto.getTurmaId()).orElseThrow(() -> new RuntimeException("Turma não encontrada")));
        inscricao.setSemestre(dto.getSemestre());
        inscricao.setDataInscricao(LocalDate.now());
        inscricao.setStatus("ATIVO");


        inscricaoRepository.save(inscricao);
    }

//
//    public List<Inscricao> listarInscritosPorTurma(Long turmaId) {
//        List<Inscricao> insc = inscricaoRepository.findByTurmaIdOrderByDataInscricaoAsc(turmaId);
//        if (insc.isEmpty()) {
//            throw new NoSuchElementException("Turma não encontrado");
//        }
//        return insc;
//    }

    public List<InscritoDTO> listarAlunosPorTurma(Long turmaId) {
        List<Inscricao> inscricoes = inscricaoRepository.findInscricoesByTurma(turmaId);


        return inscricoes.stream()
                .map(inscricao -> new InscritoDTO(
                        inscricao.getAluno().getNome(),
                        inscricao.getAluno().getTelefone(),
                        inscricao.getAluno().getTelefoneEmergencia(),
                        // E este dado pegamos da própria inscricao
                        inscricao.getDataInscricao()
                ))
                .toList();
    }

    public Turma listarTurmaPorAluno(Long id, String semestre) {
        Usuario u = usuarioRepository.findUsuarioById(id);
        Inscricao i = inscricaoRepository.findInscricaoByAlunoAndSemestre(u, semestre);
        return turmaRepository.findTurmaById(i.getTurma().getId());
    }
//
//    public void registrarFalta(Long inscricaoId) {
//        Inscricao i = inscricaoRepository.findById(inscricaoId).orElseThrow(() -> new RuntimeException("Inscrição não encontrada com ID: " + inscricaoId));
//        i.setFaltas(i.getFaltas() + 1);
//        inscricaoRepository.save(i);
//    }
}
