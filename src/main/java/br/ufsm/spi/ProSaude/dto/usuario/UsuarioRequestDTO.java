package br.ufsm.spi.ProSaude.dto.usuario;

import java.time.LocalDate;

public record UsuarioRequestDTO(
        Long id,
        String nome,
        String email,
        String senha,
        String perfil,
        String telefone,
        String telefoneEmergencia,
        String cpf,
        LocalDate dataNascimento,
        String observacaoMedic,
        Boolean primeiroAcesso
) {


}

