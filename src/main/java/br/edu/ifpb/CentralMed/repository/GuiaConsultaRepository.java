package br.edu.ifpb.CentralMed.repository;

import br.edu.ifpb.CentralMed.model.GuiaConsulta;
import br.edu.ifpb.CentralMed.model.enums.StatusNfs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List; // Importe o List

@Repository
public interface GuiaConsultaRepository extends JpaRepository<GuiaConsulta, Long> {

    // --- ADICIONE ESTE MÉTODO ---
    // Spring Data vai entender e criar a query "SELECT * FROM guia_consulta WHERE status = ?"
    List<GuiaConsulta> findByStatus(StatusNfs.StatusGuia status);

}
