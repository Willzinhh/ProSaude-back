package br.ufsm.spi.ProSaude.model.chamada;
import br.ufsm.spi.ProSaude.model.presenca.PresencaItemDto;

import java.time.LocalDate;
import java.util.List;

public class ChamadaDto {

    private Long id;
    private Long turmaId;
    private LocalDate data;
    private List<PresencaItemDto> presencas;

    public ChamadaDto() {
    }

    public ChamadaDto(Long id, Long turmaId, LocalDate data, List<PresencaItemDto> presencas) {
        this.id = id;
        this.turmaId = turmaId;
        this.data = data;
        this.presencas = presencas;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public List<PresencaItemDto> getPresencas() {
        return presencas;
    }

    public void setPresencas(List<PresencaItemDto> presencas) {
        this.presencas = presencas;
    }
}