package br.ufsm.spi.ProSaude.model.turma;

import br.ufsm.spi.ProSaude.model.inscricao.Inscricao;
import br.ufsm.spi.ProSaude.model.usuario.Usuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "turma")
public class Turma {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String descricao;

    private Long vagas;

    @ManyToOne
    @JoinColumn(name = "bolsista_responsavel_id")
    private Usuario bolsista_responsavel;

    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;

    @Column(name = "hora_fim", nullable = false)
    private LocalTime horaFim;

    private boolean SEGUNDA;
    private boolean TERCA;
    private boolean QUARTA;
    private boolean QUINTA;
    private boolean SEXTA;
    private boolean SABADO;
    private boolean DOMINGO;

}


