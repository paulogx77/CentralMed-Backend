package br.edu.ifpb.CentralMed.model;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@Entity
@Table(name = "consumo_insumo")
public class ConsumoInsumo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "consulta_id")
    @JsonIgnore
    private Consulta consulta;

    @ManyToOne @JoinColumn(name = "insumo_id")
    private EstoqueInsumos insumo;

    private Integer quantidadeUtilizada;
}