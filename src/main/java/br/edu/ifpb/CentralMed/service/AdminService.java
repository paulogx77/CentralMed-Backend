package br.edu.ifpb.CentralMed.service;

import br.edu.ifpb.CentralMed.model.Convenio; // <-- Importar Convenio
import br.edu.ifpb.CentralMed.model.EstoqueInsumos;
import br.edu.ifpb.CentralMed.model.Profissional;
import br.edu.ifpb.CentralMed.model.enums.PerfilUsuario;
import br.edu.ifpb.CentralMed.repository.ConvenioRepository; // <-- Importar o Repository
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

    @Autowired
    private ConvenioRepository convenioRepository; // <-- Injetar o novo Repository

    // --- PROFISSIONAIS ---
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

    // --- ESTOQUE ---
    public List<EstoqueInsumos> verificarEstoqueBaixo() {
        return estoqueRepository.findAll().stream()
                .filter(item -> item.getQtdeAtual() <= item.getQtdeMinima())
                .collect(Collectors.toList());
    }

    // (A sua lógica de adicionarEstoque com lotes deve estar aqui)

    // --- CONVÊNIOS (MÉTODO QUE ESTAVA FALTANDO) ---

    /**
     * Salva um novo convênio, validando se o Registro ANS já existe.
     * @param convenio
     * @return
     */
    public Convenio salvarConvenio(Convenio convenio) {
        // Validação para evitar erro de duplicata no banco
        convenioRepository.findByRegistroAns(convenio.getRegistroAns()).ifPresent(c -> {
            throw new RuntimeException("Registro ANS já cadastrado.");
        });
        return convenioRepository.save(convenio);
    }

    /**
     * Lista todos os convênios.
     * @return
     */
    public List<Convenio> listarConvenios() {
        return convenioRepository.findAll();
    }
}