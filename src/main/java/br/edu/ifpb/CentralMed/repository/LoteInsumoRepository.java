package br.edu.ifpb.CentralMed.repository;

import br.edu.ifpb.CentralMed.model.LoteInsumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface LoteInsumoRepository extends JpaRepository<LoteInsumo, Long> {

    // Buscar lotes de um produto, ordenados pelo que vence primeiro (Lógica FEFO)
    // E que tenham quantidade > 0
    @Query("SELECT l FROM LoteInsumo l WHERE l.insumo.id = :insumoId AND l.quantidade > 0 ORDER BY l.dataValidade ASC")
    List<LoteInsumo> findLotesDisponiveisPorValidade(Long insumoId);
}