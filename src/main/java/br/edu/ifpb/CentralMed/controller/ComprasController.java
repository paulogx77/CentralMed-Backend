package br.edu.ifpb.CentralMed.controller;

import br.edu.ifpb.CentralMed.dto.OrdemDeCompraDTO;
import br.edu.ifpb.CentralMed.model.OrdemDeCompra;
import br.edu.ifpb.CentralMed.service.ComprasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/compras")
public class ComprasController {

    @Autowired
    private ComprasService service;

    @GetMapping
    public List<OrdemDeCompra> listar() {
        return service.listarOrdens();
    }

    @PostMapping
    public OrdemDeCompra criar(@RequestBody OrdemDeCompraDTO dto) {
        return service.criarOrdem(dto);
    }
}