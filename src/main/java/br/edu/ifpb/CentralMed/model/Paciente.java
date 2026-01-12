package br.edu.ifpb.CentralMed.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
public class Paciente {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    @Column(unique = true) private String cpf;
    private LocalDate dataNasc;
    @Column(columnDefinition = "TEXT") private String alergiasComorbidades;
    @ManyToOne
    @JoinColumn(name = "convenio_id")
    private Convenio convenio;
}
