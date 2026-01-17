package br.edu.ifpb.CentralMed.service;

import br.edu.ifpb.CentralMed.model.ProcedimentoTuss;
import br.edu.ifpb.CentralMed.repository.ProcedimentoTussRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProcedimentoTussService {

    // --- CORREÇÃO AQUI ---
    // Você precisa declarar e injetar o repository que vai ser usado
    @Autowired
    private ProcedimentoTussRepository repository;
    // -----------------------

    public ProcedimentoTuss salvar(ProcedimentoTuss procedimento) {
        return repository.save(procedimento);
    }

    public List<ProcedimentoTuss> listarTodos() {
        return repository.findAll();
    }
}
