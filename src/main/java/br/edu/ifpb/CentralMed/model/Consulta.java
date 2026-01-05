package br.edu.ifpb.CentralMed.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
public class Consulta {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime dataHoraInicio;
    private LocalDateTime dataHoraFim;
    @Column(columnDefinition = "TEXT") private String anamnese;
    private String diagnosticoCid10;
    @Column(columnDefinition = "TEXT") private String prescricao;

    // Nasce de um Agendamento
    @OneToOne @JoinColumn(name = "agendamento_id")
    private Agendamento agendamento;

    // Lista de materiais gastos
    @OneToMany(mappedBy = "consulta", cascade = CascadeType.ALL)
    private List<ConsumoInsumo> insumosConsumidos;
}