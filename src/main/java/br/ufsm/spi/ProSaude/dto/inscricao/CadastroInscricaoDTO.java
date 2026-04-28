package br.ufsm.spi.ProSaude.dto.inscricao;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CadastroInscricaoDTO {
    // Dados do Usuário
    private String nome;
    private String email;
    private String cpf;
    private String telefone;
    private String contatoEmergencia;
    private String doencasCronicas;
    private String dataNascimento; // Receba como String do Flutter

    // Dados da Matrícula
    private Long turmaId;
    private String semestre;
}
