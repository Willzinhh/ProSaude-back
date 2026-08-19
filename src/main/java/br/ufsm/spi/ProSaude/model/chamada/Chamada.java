package br.ufsm.spi.ProSaude.model.chamada;


import br.ufsm.spi.ProSaude.model.turma.Turma;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import br.ufsm.spi.ProSaude.model.presenca.Presenca;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class Chamada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "turma_id", nullable = false)
    private Turma turma;

    @Column(nullable = false)
    private LocalDate data;

    @OneToMany(mappedBy = "chamada", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Presenca> presencas = new ArrayList<>();

    public Chamada() {
    }

    public Chamada(Long id, Turma turma, LocalDate data) {
        this.id = id;
        this.turma = turma;
        this.data = data;
    }


}
