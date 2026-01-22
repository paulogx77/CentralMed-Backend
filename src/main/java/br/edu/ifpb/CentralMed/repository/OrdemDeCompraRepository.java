package br.edu.ifpb.CentralMed.repository;

import br.edu.ifpb.CentralMed.model.OrdemDeCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdemDeCompraRepository extends JpaRepository<OrdemDeCompra, Long> {
    // Por enquanto, não precisa de métodos customizados aqui.
    // O JpaRepository já fornece: save(), findById(), findAll(), deleteById(), etc.
}