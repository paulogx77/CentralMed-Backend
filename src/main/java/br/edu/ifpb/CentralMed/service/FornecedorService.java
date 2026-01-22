package br.edu.ifpb.CentralMed.service;

import br.edu.ifpb.CentralMed.model.Fornecedor;
import br.edu.ifpb.CentralMed.repository.FornecedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FornecedorService {

    @Autowired
    private FornecedorRepository fornecedorRepository;

    public List<Fornecedor> listarTodos() {
        return fornecedorRepository.findAll();
    }

    public Fornecedor salvar(Fornecedor fornecedor) {
        // Validação para evitar CNPJ duplicado
        fornecedorRepository.findByCnpj(fornecedor.getCnpj()).ifPresent(f -> {
            if (!f.getId().equals(fornecedor.getId())) {
                throw new IllegalStateException("CNPJ já cadastrado para outro fornecedor.");
            }
        });
        return fornecedorRepository.save(fornecedor);
    }

    public void inativar(Long id) {
        Fornecedor f = fornecedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));
        f.setAtivo(false);
        fornecedorRepository.save(f);
    }
}