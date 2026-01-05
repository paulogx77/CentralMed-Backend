package br.edu.ifpb.CentralMed.dto;

import lombok.Data;
@Data public class TriagemDTO {
    private Double peso;
    private Double altura;
    private String pressao;
    private Double temperatura;
    private Double saturacao;
    private String observacoes;
    private Long enfermeiroId; // Quem fez
}