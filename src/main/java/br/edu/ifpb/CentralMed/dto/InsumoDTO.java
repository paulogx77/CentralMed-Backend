package br.edu.ifpb.CentralMed.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class InsumoDTO {
    // Dados do Produto (Pai)
    private String nome;
    private Integer qtdeMinima;

    // Dados do Lote (Filho) - O Front envia tudo junto
    private Integer quantidade;
    private String numeroLote;
    private LocalDate dataValidade;
}