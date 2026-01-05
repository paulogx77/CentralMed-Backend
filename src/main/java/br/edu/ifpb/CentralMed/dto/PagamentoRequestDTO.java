package br.edu.ifpb.CentralMed.dto;

import lombok.Data;
import java.math.BigDecimal;
@Data public class PagamentoRequestDTO {
    private Long consultaId;
    private BigDecimal valor;
    private String formaPagamento;
}
