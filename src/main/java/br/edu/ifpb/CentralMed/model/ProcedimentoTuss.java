package br.edu.ifpb.CentralMed.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class ProcedimentoTuss {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String codigoTuss; // Ex: "10101012"

    private String descricao; // Ex: "Consulta em consultório (em horário normal ou preestabelecido)"
}