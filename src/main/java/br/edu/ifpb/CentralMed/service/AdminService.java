package br.edu.ifpb.CentralMed.service;

import br.edu.ifpb.CentralMed.model.EstoqueInsumos;
import br.edu.ifpb.CentralMed.model.Profissional;
import br.edu.ifpb.CentralMed.model.enums.PerfilUsuario;
import br.edu.ifpb.CentralMed.repository.EstoqueRepository;
import br.edu.ifpb.CentralMed.repository.ProfissionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private ProfissionalRepository profissionalRepository;

    @Autowired
    private EstoqueRepository estoqueRepository;

    // --- Gestão de Profissionais ---

    public Profissional salvarProfissional(Profissional profissional) {
        return profissionalRepository.save(profissional);
    }

    public List<Profissional> listarPorPerfil(PerfilUsuario perfil) {
        return profissionalRepository.findAll().stream()
                .filter(p -> p.getPerfil() == perfil)
                .collect(Collectors.toList());
    }

    public List<Profissional> listarTodos() {
        return profissionalRepository.findAll();
    }

    // --- Gestão de Estoque ---

    public EstoqueInsumos adicionarEstoque(Long idInsumo, Integer quantidadeAdicional) {
        EstoqueInsumos item = estoqueRepository.findById(idInsumo)
                .orElseThrow(() -> new RuntimeException("Insumo não encontrado"));

        item.setQtdeAtual(item.getQtdeAtual() + quantidadeAdicional);

        return estoqueRepository.save(item);
    }

    public List<EstoqueInsumos> verificarEstoqueBaixo() {
        return estoqueRepository.findItensComEstoqueBaixo();
    }
}