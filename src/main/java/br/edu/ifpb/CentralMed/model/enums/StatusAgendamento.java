package br.edu.ifpb.CentralMed.model.enums;

public enum StatusAgendamento {

    AGENDADO,           // Marcado, mas paciente não chegou
    AGUARDANDO_TRIAGEM, // Check-in feito (Recepção)
    AGUARDANDO_CONSULTA,// Triagem feita (Enfermagem)
    EM_ATENDIMENTO,     // Dentro da sala com Médico
    FINALIZADO,         // Consulta Encerrada
    CANCELADO

}
