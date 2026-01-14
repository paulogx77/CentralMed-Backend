package br.edu.ifpb.CentralMed.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dataHoraInicio;
    private LocalDateTime dataHoraFim;

    @Column(columnDefinition = "TEXT")
    private String anamnese;

    private String diagnosticoCid10;

    @Column(columnDefinition = "TEXT")
    private String prescricao;

    @OneToOne
    @JoinColumn(name = "agendamento_id", unique = true) // Garante que uma agenda só gera uma consulta
    private Agendamento agendamento;

    @OneToMany(mappedBy = "consulta", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ConsumoInsumo> insumosConsumidos;

    @OneToOne(mappedBy = "consulta", cascade = CascadeType.ALL)
    @JsonIgnore
    private GuiaConsulta guia;


    @OneToMany(mappedBy = "consulta", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Anexo> anexos = new ArrayList<>();
}