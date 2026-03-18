package br.ufsm.spi.ProSaude.dto;

public class LoginResponseDTO {
    private String token;
    private String nome;
    private String perfil; // Ex: "ADMIN", "MONITOR", "BOLSISTA"

    public LoginResponseDTO(String token, String nome, String perfil) {
        this.token = token;
        this.nome = nome;
        this.perfil = perfil;
    }

    // Getters e Setters (ou use @Data se tiver o Lombok)
    public String getToken() { return token; }
    public String getNome() { return nome; }
    public String getPerfil() { return perfil; }
}