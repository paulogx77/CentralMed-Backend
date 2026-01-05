package br.edu.ifpb.CentralMed.repository;

import br.edu.ifpb.CentralMed.model.Agendamento;
import br.edu.ifpb.CentralMed.model.enums.StatusAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    // Busca fila geral (Recepção/Triagem) ordenada por hora de chegada
    List<Agendamento> findByDataAndStatusOrderByHoraAsc(LocalDate data, StatusAgendamento status);

    // Busca fila específica de um médico (caso queira filtrar)
    List<Agendamento> findByMedicoIdAndDataAndStatus(Long medicoId, LocalDate data, StatusAgendamento status);
}
