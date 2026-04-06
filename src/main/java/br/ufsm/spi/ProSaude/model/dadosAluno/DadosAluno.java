package br.ufsm.spi.ProSaude.model.dadosAluno;

import br.ufsm.spi.ProSaude.model.usuario.Usuario;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data // Se estiver usando Lombok (gera getters e setters)
@Entity
public class DadosAluno {
    @Id
    private Long id; // Este ID será o mesmo do Usuario

    @OneToOne
    @MapsId // Diz ao JPA para usar a PK do Usuario como PK daqui
    @JoinColumn(name = "usuario_id")
    @JsonBackReference
    private Usuario usuario;

    private String telefone;
    private String CPF;
    private String observacaoMedica;
    private LocalDate dataNascimento;

    // Getters e Setters
}
