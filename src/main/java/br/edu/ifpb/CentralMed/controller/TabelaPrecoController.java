package br.edu.ifpb.CentralMed.controller;

import br.edu.ifpb.CentralMed.model.ConvenioProcedimentoPreco;
import br.edu.ifpb.CentralMed.service.TabelaPrecoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin/precos") // URL que o Front vai usar
public class TabelaPrecoController {
    @Autowired private TabelaPrecoService service;
    
    @GetMapping
    public List<ConvenioProcedimentoPreco> getPrecos() {
        return service.listarPrecos();
    }

    @PostMapping
    public ConvenioProcedimentoPreco setPreco(@RequestBody ConvenioProcedimentoPreco preco) {
        return service.salvarPreco(preco);
    }
}