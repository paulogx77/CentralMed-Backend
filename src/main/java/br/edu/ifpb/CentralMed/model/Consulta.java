package br.edu.ifpb.CentralMed.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
// IMPORTS DO LOMBOK
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// --- CORREÇÃO AQUI ---
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id") // Evita o loop infinito comparando apenas o ID
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
    @JoinColumn(name = "agendamento_id", unique = true)
    private Agendamento agendamento;

    @OneToMany(mappedBy = "consulta", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ConsumoInsumo> insumosConsumidos = new ArrayList<>(); // Boa prática inicializar

    @OneToOne(mappedBy = "consulta", cascade = CascadeType.ALL)
    @JsonIgnore
    private GuiaConsulta guia;

    @OneToMany(mappedBy = "consulta", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Anexo> anexos = new ArrayList<>();
}