package br.ufsm.spi.ProSaude.model.presenca;

import br.ufsm.spi.ProSaude.model.aluno.Aluno;
import br.ufsm.spi.ProSaude.model.turma.Turma;
import br.ufsm.spi.ProSaude.model.usuario.Usuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "presenca")
public class Presenca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private Long aluno_id; // Quem é o aluno


    private Long turma_id; // Em qual atividade/turma ele está [cite: 12, 16]

    private LocalDateTime dataHoraRegistro; // Registro em tempo real

    @ManyToOne
    @JoinColumn(name = "bolsista_id")
    private Usuario bolsista; // Qual bolsista validou a presença

    private boolean presente;
}
