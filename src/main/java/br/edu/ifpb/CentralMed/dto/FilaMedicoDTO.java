package br.edu.ifpb.CentralMed.dto;

import br.edu.ifpb.CentralMed.model.Agendamento;
import java.util.List;

/**
 * DTO (Data Transfer Object) para encapsular as duas filas do médico:
 * - minhaFila: Pacientes direcionados especificamente para o médico.
 * - filaGeral: Pacientes aguardando qualquer médico disponível.
 */
public record FilaMedicoDTO(
        List<Agendamento> minhaFila,
        List<Agendamento> filaGeral
) {}