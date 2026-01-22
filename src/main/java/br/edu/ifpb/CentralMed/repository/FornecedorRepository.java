package br.edu.ifpb.CentralMed.repository;

import br.edu.ifpb.CentralMed.model.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {
    Optional<Fornecedor> findByCnpj(String cnpj); // Útil para validar se CNPJ já existe
}