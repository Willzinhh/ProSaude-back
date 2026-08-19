package br.ufsm.spi.ProSaude.model.chamada;

import java.time.LocalDate;
import java.util.List;

public class ChamadaRequest {

    private Long turmaId;
    private LocalDate data;
    private List<PresencaRequest> presencas;

    // Getters e Setters
    public Long getTurmaId() {
        return turmaId;
    }

    public void setTurmaId(Long turmaId) {
        this.turmaId = turmaId;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public List<PresencaRequest> getPresencas() {
        return presencas;
    }

    public void setPresencas(List<PresencaRequest> presencas) {
        this.presencas = presencas;
    }

    public static class PresencaRequest {
        private Long alunoId;
        private Boolean presente;

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
}
