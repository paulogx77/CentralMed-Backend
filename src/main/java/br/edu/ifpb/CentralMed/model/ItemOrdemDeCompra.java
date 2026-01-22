package br.edu.ifpb.CentralMed.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
public class ItemOrdemDeCompra {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantidade;
    private BigDecimal valorUnitario;

    @ManyToOne
    @JoinColumn(name = "insumo_id")
    private EstoqueInsumos insumo;

    @ManyToOne
    @JoinColumn(name = "ordem_de_compra_id")
    @JsonIgnore // Essencial para evitar loop
    private OrdemDeCompra ordemDeCompra;
}