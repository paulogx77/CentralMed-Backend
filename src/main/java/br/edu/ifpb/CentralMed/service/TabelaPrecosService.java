package br.edu.ifpb.CentralMed.service;

import br.edu.ifpb.CentralMed.model.TabelaPrecos;
import br.edu.ifpb.CentralMed.repository.TabelaPrecosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TabelaPrecosService {

    @Autowired private TabelaPrecosRepository repository;

    public TabelaPrecos salvarPreco(TabelaPrecos tabelaPrecos){
        return repository.save(tabelaPrecos);
    }

    public List<TabelaPrecos> listarPrecos(){
        return repository.findAll();
    }
}