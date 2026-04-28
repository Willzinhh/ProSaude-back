package br.ufsm.spi.ProSaude.model.presenca;

import br.ufsm.spi.ProSaude.model.inscricao.Inscricao; // Importe sua classe de inscrição
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
    private Long id;

    // Em vez de Usuario e Turma separados, apontamos para a Inscricao
    // que já contém ambos os dados.
    @ManyToOne
    @JoinColumn(name = "inscricao_id", nullable = false)
    private Inscricao inscricao;

    private LocalDateTime dataHoraRegistro = LocalDateTime.now();

    @Column(nullable = false)
    private boolean presente; // true = presente, false = falta
}