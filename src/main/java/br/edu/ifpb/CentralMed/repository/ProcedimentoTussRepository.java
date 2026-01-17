package br.edu.ifpb.CentralMed.repository;

import br.edu.ifpb.CentralMed.model.ProcedimentoTuss;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // A anotação @Repository é uma boa prática
public interface ProcedimentoTussRepository extends JpaRepository<ProcedimentoTuss, Long> {
    // Pode ficar vazio
}
