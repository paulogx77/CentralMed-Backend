package br.edu.ifpb.CentralMed.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
// Garante que não haverá duas entradas para a mesma combinação
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"convenio_id", "procedimento_id"})
})
public class TabelaPrecos {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "convenio_id", nullable = false)
    private Convenio convenio;

    @ManyToOne
    @JoinColumn(name = "procedimento_id", nullable = false)
    private ProcedimentoTuss procedimento;

    @Column(nullable = false)
    private BigDecimal valor;
}