package br.edu.ifpb.CentralMed.repository;

import br.edu.ifpb.CentralMed.model.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    Optional<Consulta> findByAgendamentoId(Long agendamentoId);
}