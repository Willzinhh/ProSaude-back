package br.ufsm.spi.ProSaude.model.presenca;
public class PresencaItemDto {

    private Long alunoId;
    private Boolean presente;

    public PresencaItemDto() {
    }

    public PresencaItemDto(Long alunoId, Boolean presente) {
        this.alunoId = alunoId;
        this.presente = presente;
    }

    // Getters e Setters
    public Long getAlunoId() {
        return alunoId;
    }

    public void setAlunoId(Long alunoId) {
        this.alunoId = alunoId;
    }

    public Boolean getPresente() {
        return presente;
    }

    public void setPresente(Boolean presente) {
        this.presente = presente;
    }
}
