package br.edu.ifpb.CentralMed.controller;

import br.edu.ifpb.CentralMed.model.EstoqueInsumos;
import br.edu.ifpb.CentralMed.repository.EstoqueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/estoque")
public class EstoqueController {
    @Autowired private EstoqueRepository repo;

    @GetMapping
    public List<EstoqueInsumos> listar() { return repo.findAll(); }

    @PostMapping
    public EstoqueInsumos cadastrar(@RequestBody EstoqueInsumos e) { return repo.save(e); }
}
