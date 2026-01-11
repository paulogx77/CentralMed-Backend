package br.edu.ifpb.CentralMed.service;

import br.edu.ifpb.CentralMed.model.EstoqueInsumos;
import br.edu.ifpb.CentralMed.model.LoteInsumo;
import br.edu.ifpb.CentralMed.model.Profissional;
import br.edu.ifpb.CentralMed.model.enums.PerfilUsuario;
import br.edu.ifpb.CentralMed.repository.EstoqueRepository;
import br.edu.ifpb.CentralMed.repository.LoteInsumoRepository;
import br.edu.ifpb.CentralMed.repository.ProfissionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired private ProfissionalRepository profissionalRepository;
    @Autowired private EstoqueRepository estoqueRepository;
    @Autowired private LoteInsumoRepository loteRepository; // <--- ADICIONE ISSO

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

    // --- Gestão de Estoque (CORRIGIDO PARA LOTES) ---

    public EstoqueInsumos adicionarEstoque(Long idInsumo, Integer quantidadeAdicional) {
        EstoqueInsumos item = estoqueRepository.findById(idInsumo)
                .orElseThrow(() -> new RuntimeException("Insumo não encontrado"));

        // ANTIGO (Dava Erro): item.setQtdeAtual(item.getQtdeAtual() + quantidadeAdicional);

        // NOVO: Criamos um Lote de Reposição
        LoteInsumo lote = new LoteInsumo();
        lote.setInsumo(item);
        lote.setQuantidade(quantidadeAdicional);
        lote.setNumeroLote("REPOS-" + System.currentTimeMillis()); // Gera lote automático
        lote.setDataValidade(LocalDate.now().plusYears(1)); // Validade padrão de 1 ano

        loteRepository.save(lote);

        return item;
    }

    // Nota: O método verificarEstoqueBaixo pode precisar de ajuste no Repository
    // se ele usava SQL nativo na coluna qtde_atual antiga.
    // Se ele usava JPQL ou findAll, pode precisar de revisão.
    // Por enquanto, mantenho a chamada se o Repository estiver compatível.
    public List<EstoqueInsumos> verificarEstoqueBaixo() {
        // Filtra na memória para garantir compatibilidade com a nova estrutura de lotes
        return estoqueRepository.findAll().stream()
                .filter(item -> item.getQtdeAtual() <= item.getQtdeMinima())
                .collect(Collectors.toList());
    }
}