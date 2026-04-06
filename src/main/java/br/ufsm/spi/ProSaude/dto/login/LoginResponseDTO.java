package br.ufsm.spi.ProSaude.dto.login;

public class LoginResponseDTO {
    private String token;
    private String nome;
    private String perfil; // Ex: "ADMIN", "MONITOR", "BOLSISTA"
    private long id;

    public LoginResponseDTO(String token, String nome, String perfil, Long id) {
        this.token = token;
        this.nome = nome;
        this.perfil = perfil;
        this.id = id;
    }

    // Getters e Setters (ou use @Data se tiver o Lombok)
    public String getToken() { return token; }
    public String getNome() { return nome; }
    public String getPerfil() { return perfil; }
    public long getId() { return id; }
}