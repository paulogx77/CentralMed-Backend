package br.edu.ifpb.CentralMed.repository;

import br.edu.ifpb.CentralMed.model.Consulta;
import br.edu.ifpb.CentralMed.model.enums.StatusAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    Optional<Consulta> findByAgendamentoId(Long agendamentoId);

    // Busca consultas finalizadas que AINDA NÃO estão na tabela financeira
    @Query("SELECT c FROM Consulta c WHERE c.agendamento.status = 'FINALIZADO' " +
            "AND c.id NOT IN (SELECT l.consulta.id FROM LancamentoFinanceiro l)")
    List<Consulta> findConsultasPendentesDePagamento();

    // Query para buscar histórico de um paciente
    @Query("SELECT c FROM Consulta c WHERE c.agendamento.paciente.id = :pacienteId " +
            "ORDER BY c.dataHoraInicio DESC")
    List<Consulta> buscarHistoricoDoPaciente(@Param("pacienteId") Long pacienteId);

    // --- NOVO MÉTODO PARA A LÓGICA DE RETORNO ---
    @Query("SELECT c FROM Consulta c " +
            "WHERE c.agendamento.paciente.id = :pacienteId " +
            "AND c.agendamento.medico.id = :medicoId " +
            "ORDER BY c.dataHoraInicio DESC")
    List<Consulta> findUltimaConsultaPorPacienteEMedico(
            @Param("pacienteId") Long pacienteId,
            @Param("medicoId") Long medicoId);
    // ----------------------------------------------
}