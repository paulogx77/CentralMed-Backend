package br.edu.ifpb.CentralMed.repository;

import br.edu.ifpb.CentralMed.model.EstoqueInsumos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstoqueRepository extends JpaRepository<EstoqueInsumos, Long> {

    // Útil para buscar insumos pelo nome na tela de gestão
    List<EstoqueInsumos> findByNomeContainingIgnoreCase(String nome);

    @Query("SELECT e FROM EstoqueInsumos e WHERE e.qtdeAtual <= e.qtdeMinima")
    List<EstoqueInsumos> findItensComEstoqueBaixo();
}