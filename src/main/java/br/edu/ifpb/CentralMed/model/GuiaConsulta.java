package br.edu.ifpb.CentralMed.model;

import br.edu.ifpb.CentralMed.model.enums.StatusGuia; // <--- O IMPORT CORRETO
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

    // --- CORREÇÃO AQUI ---
    // Use 'StatusGuia', sem nenhum 'StatusNfs.' na frente
    @Enumerated(EnumType.STRING)
    private StatusGuia status;
    // -----------------------

    @OneToOne
    @JoinColumn(name = "consulta_id")
    private Consulta consulta;

    @ManyToOne
    @JoinColumn(name = "convenio_id")
    private Convenio convenio;
}