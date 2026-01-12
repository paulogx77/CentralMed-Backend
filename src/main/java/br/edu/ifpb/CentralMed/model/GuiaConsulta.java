package br.edu.ifpb.CentralMed.model;

// --- Importe o Enum CORRETO ---
import br.edu.ifpb.CentralMed.model.enums.StatusGuia;
// ----------------------------

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
public class GuiaConsulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numeroGuia;
    private LocalDate dataEmissao;
    private BigDecimal valorConsulta;

    // --- CORREÇÃO AQUI: Garanta que está usando 'StatusGuia' ---
    @Enumerated(EnumType.STRING)
    private StatusGuia status;
    // -----------------------------------------------------------

    @OneToOne
    @JoinColumn(name = "consulta_id")
    private Consulta consulta;
}