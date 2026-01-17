package br.edu.ifpb.CentralMed.service;

import br.edu.ifpb.CentralMed.model.ConvenioProcedimentoPreco;
import br.edu.ifpb.CentralMed.repository.ConvenioProcedimentoPrecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TabelaPrecoService {
    @Autowired private ConvenioProcedimentoPrecoRepository repository;

    public List<ConvenioProcedimentoPreco> listarPrecos() {
        return repository.findAll();
    }
    
    public ConvenioProcedimentoPreco salvarPreco(ConvenioProcedimentoPreco preco) {
        // Validação: Ver se já existe preço para este convênio+procedimento e atualizar em vez de criar
        // Fica como melhoria futura
        return repository.save(preco);
    }
}