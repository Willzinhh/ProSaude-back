package br.ufsm.spi.ProSaude.service;

import br.ufsm.spi.ProSaude.model.avaliacao.Avaliacao;
import br.ufsm.spi.ProSaude.model.avaliacao.AvaliacaoRepository;
import br.ufsm.spi.ProSaude.model.usuario.Usuario;

import br.ufsm.spi.ProSaude.model.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class AvaliacaoService {

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public Avaliacao salvarAvaliacao(Avaliacao avaliacao, Long alunoId, Long avaliadorId) {
        Usuario aluno = usuarioRepository.findUsuarioById(alunoId);
        Usuario avaliador = usuarioRepository.findUsuarioById(avaliadorId);

        avaliacao.setAluno(aluno);
        avaliacao.setAvaliador(avaliador);

        if (avaliacao.getDataAvaliacao() == null) {
            avaliacao.setDataAvaliacao(LocalDate.now());
        }
        avaliacao.calcularParametros();
        return avaliacaoRepository.save(avaliacao);
    }

    public List<Avaliacao> listarPorAluno(Long alunoId) {
        return avaliacaoRepository.findByAlunoIdOrderByDataAvaliacaoDesc(alunoId);
    }
}