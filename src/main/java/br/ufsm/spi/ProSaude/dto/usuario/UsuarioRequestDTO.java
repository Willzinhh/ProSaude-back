package br.ufsm.spi.ProSaude.dto.usuario;

public record UsuarioRequestDTO(
        String nome,
        String email,
        String senha,
        String perfil,
        DadosAlunoRequestDTO dados // Objeto aninhado
) {}

