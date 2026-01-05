package br.edu.ifpb.CentralMed.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
public class LancamentoFinanceiro {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tipo; // "RECEITA"
    private BigDecimal valor;
    private LocalDateTime dataLancamento;
    private String formaPagamento; // Simulado

    @OneToOne @JoinColumn(name = "consulta_id")
    private Consulta consulta;
}
