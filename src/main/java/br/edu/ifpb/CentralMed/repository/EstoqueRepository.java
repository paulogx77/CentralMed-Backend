package br.edu.ifpb.CentralMed.repository;

import br.edu.ifpb.CentralMed.model.EstoqueInsumos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstoqueRepository extends JpaRepository<EstoqueInsumos, Long> {
    // Pode adicionar findByNome se precisar buscar por texto
}