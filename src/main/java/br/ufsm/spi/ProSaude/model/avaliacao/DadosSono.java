package br.ufsm.spi.ProSaude.model.avaliacao;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Embeddable
@Getter
@Setter
public class DadosSono {

    private LocalTime IQS_hrDeitar;
    private LocalTime IQS_minPDormir;
    private LocalTime IQS_hrAcordar;
    private Double IQS_hsSono;

    private Integer q5aDemoraDormir;
    private Integer q5bAcordarNoite;
    private Integer q5cBanheiroNoite;
    private Integer q5dDificuldadeRespirar;
    private Integer q5eTossirRoncar;
    private Integer q5fSentirFrio;
    private Integer q5gSentirCalor;
    private Integer q5hPesadelos;
    private Integer q5iSentirDores;

    private String q5jOutraRazaoDescricao;
    private Integer q5jOutraRazaoFrequencia;

    private String q6ClassificacaoGeral;

    private Integer q7RemedioFrequencia;
    private String q7RemedioQuais;

    private Integer q8FicarAcordadoAtividades;

    private Integer q9FaltaEntusiasmo;
    private String q9Comentarios;

    private boolean q10Cochila;
    private String q10CochilaComentarios;

    private Boolean q10CochilaIntencional;
    private String q10CochilaIntencionalComentarios;

    private String q10CochilarSignificado;
    private String q10CochilarSignificadoOutro;
    private String q10CochilarSignificadoComentarios;
}