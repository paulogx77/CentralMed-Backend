package br.edu.ifpb.CentralMed.dto;

public record AgendamentoFuturoDTO(
        String data,
        String hora,
        Long pacienteId,
        Long medicoId
) {}