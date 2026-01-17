package br.edu.ifpb.CentralMed.model;

import br.edu.ifpb.CentralMed.model.enums.StatusNfs;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
public class NotaFiscal {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numero;
    private LocalDateTime dataEmissao;
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    private StatusNfs status;

    private String chaveAcesso;

    @OneToOne
    @JoinColumn(name = "lancamento_financeiro_id", unique = true)
    private LancamentoFinanceiro lancamentoFinanceiro;

}