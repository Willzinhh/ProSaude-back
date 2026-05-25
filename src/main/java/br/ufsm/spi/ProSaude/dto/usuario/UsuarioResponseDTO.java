package br.ufsm.spi.ProSaude.dto.usuario;

import br.ufsm.spi.ProSaude.model.usuario.Perfil;
import br.ufsm.spi.ProSaude.model.usuario.Usuario;

import java.time.LocalDate;

public record UsuarioResponseDTO(String nome, String email, Perfil perfil, String telefone, String telefoneEmergencia,
                                 String CPF, LocalDate dataNacimento, String observacaoMedic) {
    public UsuarioResponseDTO(Usuario usuario) {
        this(usuario.getNome(), usuario.getEmail(), usuario.getPerfil(), usuario.getTelefone(), usuario.getTelefoneEmergencia(), usuario.getCPF(), usuario.getDataNascimento(), usuario.getObservacaoMedica());

    }
}
