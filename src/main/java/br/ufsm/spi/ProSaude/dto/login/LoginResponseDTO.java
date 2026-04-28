package br.ufsm.spi.ProSaude.dto.login;

public class LoginResponseDTO {
    private String token;
    private String nome;
    private String perfil; // Ex: "ADMIN", "MONITOR", "BOLSISTA"
    private long id;
    private boolean primeiroAcesso;

    public LoginResponseDTO(String token, String nome, String perfil, Long id, boolean primeiroAcesso) {
        this.token = token;
        this.nome = nome;
        this.perfil = perfil;
        this.id = id;
        this.primeiroAcesso = primeiroAcesso;
    }

    // Getters e Setters (ou use @Data se tiver o Lombok)
    public String getToken() { return token; }
    public String getNome() { return nome; }
    public String getPerfil() { return perfil; }
    public long getId() { return id; }
    public boolean getPrimeiroAcesso() {return primeiroAcesso; }
}