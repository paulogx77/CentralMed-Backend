package br.edu.ifpb.CentralMed.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class OrdemDeCompraDTO {
    private Long fornecedorId;
    private LocalDate dataEntregaPrevista;
    private List<ItemOrdemDTO> itens;
}