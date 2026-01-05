package br.edu.ifpb.CentralMed.model;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@Entity
public class Triagem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double peso;
    private Double altura;
    private String pressao;
    private Double temperatura;
    private Double saturacao;
    @Column(columnDefinition = "TEXT") private String observacoes;

    // Vincula ao Agendamento (Fluxo único)
    @OneToOne @JoinColumn(name = "agendamento_id")
    @JsonIgnore
    private Agendamento agendamento;

    // Vincula ao Enfermeiro que fez
    @ManyToOne @JoinColumn(name = "enfermeiro_id")
    private Profissional enfermeiro;
}
