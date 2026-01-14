package br.edu.ifpb.CentralMed.repository;

import br.edu.ifpb.CentralMed.model.Agendamento;
import br.edu.ifpb.CentralMed.model.enums.StatusAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    List<Agendamento> findByDataAndStatusOrderByPrioridadeDescHoraAsc(LocalDate data, StatusAgendamento status);

    // MÉTODO QUE VOCÊ VAI USAR AGORA
    List<Agendamento> findByMedicoIdAndDataAndStatusOrderByPrioridadeDescHoraAsc(
            Long medicoId, LocalDate data, StatusAgendamento status
    );


}
