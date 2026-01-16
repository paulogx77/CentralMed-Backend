package br.edu.ifpb.CentralMed.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
public class ConvenioProcedimentoPreco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "convenio_id")
    private Convenio convenio;

    @ManyToOne
    @JoinColumn(name = "procedimento_id")
    private ProcedimentoTuss procedimento;

    private BigDecimal valor; // O preço do procedimento para este convênio
}