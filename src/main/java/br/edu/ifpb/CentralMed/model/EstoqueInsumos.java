package br.edu.ifpb.CentralMed.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class EstoqueInsumos {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private Integer qtdeAtual;
    private Integer qtdeMinima;
}
