package br.edu.ifpb.CentralMed.repository;

import br.edu.ifpb.CentralMed.model.Triagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TriagemRepository extends JpaRepository<Triagem, Long> {
    // Permite buscar a triagem pelo ID do agendamento (usado na tela do médico)
    Triagem findByAgendamentoId(Long agendamentoId);
}
