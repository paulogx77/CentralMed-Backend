package br.edu.ifpb.CentralMed.repository;

import br.edu.ifpb.CentralMed.model.ConsumoInsumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsumoInsumoRepository extends JpaRepository<ConsumoInsumo, Long> {
}
