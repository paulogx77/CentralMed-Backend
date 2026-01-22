package br.edu.ifpb.CentralMed.controller;

import br.edu.ifpb.CentralMed.model.Fornecedor;
import br.edu.ifpb.CentralMed.service.FornecedorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/fornecedores") // A URL base será esta
public class FornecedorController {

    @Autowired
    private FornecedorService service;

    @GetMapping
    public List<Fornecedor> getAll() {
        return service.listarTodos();
    }

    @PostMapping
    public Fornecedor create(@RequestBody @Valid Fornecedor fornecedor) {
        return service.salvar(fornecedor);
    }

    @PutMapping("/{id}")
    public Fornecedor update(@PathVariable Long id, @RequestBody @Valid Fornecedor fornecedor) {
        fornecedor.setId(id); // Garante que estamos atualizando o registro certo
        return service.salvar(fornecedor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inactivate(@PathVariable Long id) {
        service.inativar(id);
        return ResponseEntity.noContent().build();
    }
}