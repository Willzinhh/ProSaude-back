package br.ufsm.spi.ProSaude.model.turma;

import br.ufsm.spi.ProSaude.model.usuario.Usuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

    @ManyToOne
    @JoinColumn(name = "bolsista_responsavel_id")
    private Usuario bolsistaResponsavel;

    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;

    @Column(name = "hora_fim", nullable = false)
    private LocalTime horaFim;


    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "turma_dias", joinColumns = @JoinColumn(name = "turma_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "dia")
    private List<DiaSemana> diasSemana;



}


