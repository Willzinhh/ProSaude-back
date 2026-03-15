package br.ufsm.spi.ProSaude.model.turma;

import br.ufsm.spi.ProSaude.model.usuario.Usuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
@Entity
@Getter
@Setter
@Table(name = "turma")
public class Turma {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codigo;

    private String nome;

    private String descricao;

    @ManyToOne
    @JoinColumn(name = "bolsista_responsavel_id")
    private Usuario bolsistaResponsavel;

    @ManyToOne
    @JoinColumn(name = "monitor_id")
    private Usuario monitor;
}


