package br.ufsm.spi.ProSaude.model.inscricao;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Inscricao {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    private  Long aluno_id;
    private  Long turma_id;
    private LocalDate data_inscricao;


}
