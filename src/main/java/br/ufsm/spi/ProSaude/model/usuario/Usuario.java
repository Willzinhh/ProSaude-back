package br.ufsm.spi.ProSaude.model.usuario;

import br.ufsm.spi.ProSaude.model.dadosAluno.DadosAluno;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    private Perfil perfil; // COORDENADOR, BOLSISTA ou MONITOR

    @Column(name = "primeiro_acesso", nullable = false)
    private Boolean primeiroAcesso = true;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    @JsonManagedReference
    private DadosAluno dados;


    // Getters e Setters
}

