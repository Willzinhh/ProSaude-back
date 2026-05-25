package br.ufsm.spi.ProSaude.dto.inscricao;

public record InscricaoRequestDTO(
        String nome,
        String email,
        String CPF,
        String telefone,
        String contatoEmergencia,
        String possuiDoencaQual,
        String dataNacimento

) {

}
