package br.edu.ifpb.CentralMed.model;

import br.edu.ifpb.CentralMed.model.enums.Prioridade;
import br.edu.ifpb.CentralMed.model.enums.StatusAgendamento;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Data
@Entity
public class Agendamento {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate data;
    private LocalTime hora;
    @Enumerated(EnumType.STRING)
    private StatusAgendamento status;
    private String senhaPainel;


    @Enumerated(EnumType.ORDINAL)
    private Prioridade prioridade = Prioridade.NORMAL;

    @ManyToOne @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    @ManyToOne @JoinColumn(name = "profissional_id")
    private Profissional medico;


}