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

    // Pergunta 5: Frequências (Salvaremos como Integer: 0, 1, 2 ou 3)
    private Integer q5aDemoraDormir;
    private Integer q5bAcordarNoite;
    private Integer q5cBanheiroNoite;
    private Integer q5dDificuldadeRespirar;
    private Integer q5eTossirRoncar;
    private Integer q5fSentirFrio;
    private Integer q5gSentirCalor;
    private Integer q5hPesadelos;
    private Integer q5iSentirDores;

    // 5j: Outra razão
    private String q5jOutraRazaoDescricao;
    private Integer q5jOutraRazaoFrequencia;

    // Pergunta 6: Classificação Geral (Ex: "MUITO_BOA", "BOA", "RUIM", "MUITO_RUIM")
    private String q6ClassificacaoGeral;

    // Pergunta 7: Remédios
    private Integer q7RemedioFrequencia;
    private String q7RemedioQuais;

    // Pergunta 8: Problema para ficar acordado
    private Integer q8FicarAcordadoAtividades;

    // Pergunta 9: Indisposição / Entusiasmo
    private Integer q9FaltaEntusiasmo; // 0 a 3 conforme as opções
    private String q9Comentarios;

    // Pergunta 10: Cochilos
    private boolean q10Cochila;
    private String q10CochilaComentarios;

    private Boolean q10CochilaIntencional;
    private String q10CochilaIntencionalComentarios;

    private String q10CochilarSignificado; // "PRAZER", "NECESSIDADE", "OUTRO"
    private String q10CochilarSignificadoOutro;
    private String q10CochilarSignificadoComentarios;
}