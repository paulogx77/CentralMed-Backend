package br.edu.ifpb.CentralMed.repository;

import br.edu.ifpb.CentralMed.model.LogAcessoProntuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogAcessoRepository extends JpaRepository<LogAcessoProntuario, Long> {}