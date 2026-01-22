package br.edu.ifpb.CentralMed.service;

import br.edu.ifpb.CentralMed.dto.OrdemDeCompraDTO;
import br.edu.ifpb.CentralMed.model.*;
import br.edu.ifpb.CentralMed.model.enums.StatusOrdem;
import br.edu.ifpb.CentralMed.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ComprasService {

    @Autowired private OrdemDeCompraRepository ordemRepository;
    @Autowired private FornecedorRepository fornecedorRepository;
    @Autowired private EstoqueRepository estoqueRepository;

    public List<OrdemDeCompra> listarOrdens() {
        return ordemRepository.findAll();
    }

    @Transactional
    public OrdemDeCompra criarOrdem(OrdemDeCompraDTO dto) {
        Fornecedor fornecedor = fornecedorRepository.findById(dto.getFornecedorId()).orElseThrow();

        OrdemDeCompra ordem = new OrdemDeCompra();
        ordem.setFornecedor(fornecedor);
        ordem.setDataEmissao(LocalDate.now());
        ordem.setDataEntregaPrevista(dto.getDataEntregaPrevista());
        ordem.setStatus(StatusOrdem.PENDENTE);

        List<ItemOrdemDeCompra> itens = new ArrayList<>();
        BigDecimal valorTotal = BigDecimal.ZERO;

        for (var itemDto : dto.getItens()) {
            EstoqueInsumos insumo = estoqueRepository.findById(itemDto.insumoId()).orElseThrow();

            ItemOrdemDeCompra item = new ItemOrdemDeCompra();
            item.setInsumo(insumo);
            item.setQuantidade(itemDto.quantidade());
            item.setValorUnitario(itemDto.valorUnitario());
            item.setOrdemDeCompra(ordem); // Vincula ao pai

            itens.add(item);
            valorTotal = valorTotal.add(itemDto.valorUnitario().multiply(new BigDecimal(itemDto.quantidade())));
        }

        ordem.setItens(itens);
        ordem.setValorTotal(valorTotal);

        return ordemRepository.save(ordem);
    }

    // (A lógica de Recebimento e atualização de Lotes virá depois)
}