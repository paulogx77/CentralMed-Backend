package br.edu.ifpb.CentralMed.dto;

import lombok.Data;
import java.util.List;
@Data public class FinalizarConsultaDTO {
    private String anamnese;
    private String diagnosticoCid10;
    private String prescricao;
    private List<InsumoRequestDTO> insumosConsumidos;
}