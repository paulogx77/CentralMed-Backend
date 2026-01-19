package br.edu.ifpb.CentralMed.repository;

import br.edu.ifpb.CentralMed.model.Agendamento;
import br.edu.ifpb.CentralMed.model.enums.StatusAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    /**
     * Busca geral para uma fila (Ex: Triagem, que não depende de médico).
     * Ordena por prioridade e depois por hora.
     */
    List<Agendamento> findByDataAndStatusOrderByPrioridadeDescHoraAsc(
            LocalDate data, StatusAgendamento status
    );

    /**
     * Busca a fila de um médico específico.
     */
    List<Agendamento> findByMedicoIdAndDataAndStatusOrderByPrioridadeDescHoraAsc(
            Long medicoId, LocalDate data, StatusAgendamento status
    );

    /**
     * Busca a "Fila Geral": pacientes em espera que NÃO foram direcionados a nenhum médico.
     * `MedicoIdIsNull` é a chave.
     */
    List<Agendamento> findByMedicoIdIsNullAndDataAndStatusOrderByPrioridadeDescHoraAsc(
            LocalDate data, StatusAgendamento status
    );

    /**
     * Busca para o Admin: todos os pacientes em espera que FORAM direcionados a algum médico.
     * `MedicoIdIsNotNull` é a chave.
     */
    List<Agendamento> findByMedicoIdIsNotNullAndDataAndStatusOrderByPrioridadeDescHoraAsc(
            LocalDate data, StatusAgendamento status
    );

    List<Agendamento> findByDataBetween(LocalDate inicio, LocalDate fim);
}