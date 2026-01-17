package br.edu.ifpb.CentralMed.controller;

import br.edu.ifpb.CentralMed.dto.InsumoDTO; // <--- Importe o DTO novo
import br.edu.ifpb.CentralMed.model.EstoqueInsumos;
import br.edu.ifpb.CentralMed.model.LoteInsumo;
import br.edu.ifpb.CentralMed.repository.EstoqueRepository;
import br.edu.ifpb.CentralMed.repository.LoteInsumoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estoque")
public class EstoqueController {

    @Autowired private EstoqueRepository estoqueRepo;
    @Autowired private LoteInsumoRepository loteRepo;

    @GetMapping
    public List<EstoqueInsumos> listar() {
        return estoqueRepo.findAll();
    }

    @PostMapping
    @Transactional
    public EstoqueInsumos cadastrar(@RequestBody InsumoDTO dto) { // <--- Usa o DTO aqui
        // 1. Cria o Produto
        EstoqueInsumos insumo = new EstoqueInsumos();
        insumo.setNome(dto.getNome());
        insumo.setQtdeMinima(dto.getQtdeMinima());
        insumo = estoqueRepo.save(insumo);

        // 2. Cria o Lote
        LoteInsumo lote = new LoteInsumo();
        lote.setNumeroLote(dto.getNumeroLote());
        lote.setDataValidade(dto.getDataValidade());
        lote.setQuantidade(dto.getQuantidade());
        lote.setInsumo(insumo);

        loteRepo.save(lote);

        return insumo;
    }
}