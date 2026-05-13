package br.ufsm.spi.ProSaude.model.usuario;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

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


    private String telefone;
    @Column(name = "telefone_emergencia")
    private String telefoneEmergencia;

    @JoinColumn(name = "cpf")
    private String CPF;

    private LocalDate dataNascimento;

    private String observacaoMedica;

}

