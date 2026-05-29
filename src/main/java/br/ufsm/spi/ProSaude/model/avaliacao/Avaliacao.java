package br.ufsm.spi.ProSaude.model.avaliacao;

import br.ufsm.spi.ProSaude.model.usuario.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dataAvaliacao;

    @ManyToOne
    @JoinColumn(name = "aluno_id")
    private Usuario aluno;

    @ManyToOne
    @JoinColumn(name = "avaliador_id")
    private Usuario avaliador;

    // ANAMNESE (ANA)
    private String anaProfi;
    private Double anaHsTrab;
    private String anaTurnTrab;
    private boolean anaFuma;
    private String anaFumaTempo;
    private boolean anaAlcool;

    @Enumerated(EnumType.STRING)
    private Qualidade anaQualiSono;

    private Double anaHsSono;
    private Double anaCoposAguaDia;

    @Enumerated(EnumType.STRING)
    private Qualidade anaAlimentacao;

    private Double anaRefDia;
    private String anaCirurgia;
    private String anaProbCardiaco;

    // ANTROPOMETRIA (ANT)
    private Double antPeso;
    private Double antAltura;
    private Double antImc;
    private String antImcClass;
    private Double antPeriCintura;
    private Double antPeriQuadril;
    private Double antRcq;
    private String antRcqClass;

    // COMPOSIÇÃO / DORES (COM)
    private Double comEscalaFig;
    private Double comEscalaFigQuer;
    private boolean comDorehj;

    @ElementCollection
    @CollectionTable(name = "avaliacao_dores", joinColumns = @JoinColumn(name = "avaliacao_id"))
    @MapKeyColumn(name = "local_dor")
    @Column(name = "intensidade_dor")
    private Map<String, Integer> comDores;

    // QUESTIONÁRIO DE PITTSBURGH (IQS)
    @Embedded
    private DadosSono dadosSono;

    // POSTURA - VISTA ANTERIOR (Apenas Texto)
    private String posAnteriorCabeca;
    private String posAnteriorOmbros;
    private String posAnteriorCompBracos;
    private String posAnteriorTrianguloTales;
    private String posAnteriorTronco;
    private String posAnteriorLinhaMamilar;
    private String posAnteriorEquiHorizPelvico;
    private String posAnteriorCicatrizUmbilical;
    private String posAnteriorQuadrilRod;
    private String posAnteriorJoelhos;
    private String posAnteriorPes;


    // POSTURA - VISTA PERFIL (PLANO SAGITAL)
    private String posPerfilCabeca;
    private String posPerfilOmbros;
    private String posPerfilMembrosSuperiores;

    // COLUNA VERTEBRAL
    private String posColunaCervical;
    private String posColunaDorsal;
    private String posColunaLombar;
    private String posColunaQuadril;
    private String posColunaJoelhos;

    // POSTURA - VISTA POSTERIOR (PLANO DORSAL)
    private String posPosteriorEscoliose;
    private String posPosteriorGibosidade;
    private String posPosteriorTendaoAquiles;

    @Column(columnDefinition = "TEXT")
    private String obs;

    /**
     * Realiza os cálculos de IMC e RCQ com base nos dados fornecidos pelo Flutter
     * e preenche as variáveis de classificação automaticamente.
     */
    public void calcularParametros() {
        // 1. CÁLCULO E CLASSIFICAÇÃO DO IMC
        if (this.antPeso != null && this.antAltura != null && this.antAltura > 0) {
            // Se a altura vier em centímetros (ex: 175.0), converte para metros (1.75)
            double alturaMetros = this.antAltura > 3 ? this.antAltura / 100 : this.antAltura;

            this.antImc = this.antPeso / (alturaMetros * alturaMetros);

            if (this.antImc < 18.5) this.antImcClass = "Abaixo do peso";
            else if (this.antImc < 25) this.antImcClass = "Peso normal";
            else if (this.antImc < 30) this.antImcClass = "Sobrepeso";
            else if (this.antImc < 35) this.antImcClass = "Obesidade Grau I";
            else if (this.antImc < 40) this.antImcClass = "Obesidade Grau II";
            else this.antImcClass = "Obesidade Grau III";
        } else {
            this.antImc = 0.0;
            this.antImcClass = "Não calculado";
        }

        // 2. CÁLCULO E CLASSIFICAÇÃO DO RCQ (Relação Cintura-Quadril)
        if (this.antPeriCintura != null && this.antPeriQuadril != null && this.antPeriQuadril > 0) {
            this.antRcq = this.antPeriCintura / this.antPeriQuadril;

            // Classificação baseada no padrão da OMS
            // Se você futuramente quiser separar por sexo, precisaria checar this.aluno.getSexo()
            if (this.antRcq >= 1.0) {
                this.antRcqClass = "Risco Alto / Muito Alto";
            } else if (this.antRcq >= 0.85) {
                this.antRcqClass = "Risco Moderado";
            } else {
                this.antRcqClass = "Risco Baixo";
            }
        } else {
            this.antRcq = 0.0;
            this.antRcqClass = "Não calculado";
        }
    }
}
