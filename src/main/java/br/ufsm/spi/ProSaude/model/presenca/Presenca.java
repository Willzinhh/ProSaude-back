package br.ufsm.spi.ProSaude.model.presenca;

import br.ufsm.spi.ProSaude.model.turma.Turma;
import br.ufsm.spi.ProSaude.model.usuario.Usuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Presenca {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID incremental (1, 2, 3...)

    @ManyToOne
    @JoinColumn(name = "aluno_id")
    private Usuario aluno; // FK apontando para Usuario

    @ManyToOne
    @JoinColumn(name = "turma_id")
    private Turma turma;

    private LocalDateTime dataHoraRegistro = LocalDateTime.now();
    private boolean presente;
}
