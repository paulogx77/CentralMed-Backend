package br.edu.ifpb.CentralMed.repository;

import br.edu.ifpb.CentralMed.model.Consulta;
import br.edu.ifpb.CentralMed.model.enums.StatusAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;


@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    Optional<Consulta> findByAgendamentoId(Long agendamentoId);

    // --- NOVO: Busca consultas finalizadas que NÃO estão na tabela financeira ---
    @Query("SELECT c FROM Consulta c WHERE c.agendamento.status = 'FINALIZADO' " +
            "AND c.id NOT IN (SELECT l.consulta.id FROM LancamentoFinanceiro l)")
    List<Consulta> findConsultasPendentesDePagamento();

    // SOLUÇÃO: Query manual para buscar histórico do paciente
    @Query("SELECT c FROM Consulta c WHERE c.agendamento.paciente.id = :pacienteId " +
            "AND c.agendamento.status = :status " +
            "ORDER BY c.dataHoraInicio DESC")
    List<Consulta> buscarHistoricoDoPaciente(
            @Param("pacienteId") Long pacienteId,
            @Param("status") StatusAgendamento status

    );

}