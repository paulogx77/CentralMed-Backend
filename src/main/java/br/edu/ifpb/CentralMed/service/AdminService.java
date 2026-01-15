package br.edu.ifpb.CentralMed.service;

import br.edu.ifpb.CentralMed.model.Convenio;
import br.edu.ifpb.CentralMed.model.EstoqueInsumos;
import br.edu.ifpb.CentralMed.model.LoteInsumo;
import br.edu.ifpb.CentralMed.model.Paciente;
import br.edu.ifpb.CentralMed.model.Profissional;
import br.edu.ifpb.CentralMed.model.enums.PerfilUsuario;
import br.edu.ifpb.CentralMed.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired private ProfissionalRepository profissionalRepository;
    @Autowired private EstoqueRepository estoqueRepository;
    @Autowired private ConvenioRepository convenioRepository;
    @Autowired private PacienteRepository pacienteRepository; // Faltando
    @Autowired private LoteInsumoRepository loteRepository; // Faltando

    // --- PROFISSIONAIS ---
    public Profissional salvarProfissional(Profissional profissional) {
        // Lógica futura: Criptografar senha aqui antes de salvar
        return profissionalRepository.save(profissional);
    }

    public List<Profissional> listarTodos() {
        return profissionalRepository.findAll();
    }
    
    public List<Profissional> listarPorPerfil(PerfilUsuario perfil) {
        return profissionalRepository.findAll().stream()
                .filter(p -> p.getPerfil() == perfil)
                .collect(Collectors.toList());
    }

    // --- ESTOQUE ---
    public List<EstoqueInsumos> verificarEstoqueBaixo() {
        return estoqueRepository.findAll().stream()
                .filter(item -> item.getQtdeAtual() <= item.getQtdeMinima())
                .collect(Collectors.toList());
    }

    public EstoqueInsumos adicionarEstoque(Long idInsumo, Integer quantidadeAdicional) {
        EstoqueInsumos item = estoqueRepository.findById(idInsumo)
                .orElseThrow(() -> new RuntimeException("Insumo não encontrado"));
        LoteInsumo lote = new LoteInsumo();
        lote.setInsumo(item);
        lote.setQuantidade(quantidadeAdicional);
        lote.setNumeroLote("REPOSICAO-" + LocalDate.now());
        lote.setDataValidade(LocalDate.now().plusYears(1));
        loteRepository.save(lote);
        return item;
    }
    
    // --- PACIENTES (Faltava este) ---
    public Paciente atualizarPaciente(Long id, Paciente dadosNovos) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado com o ID: " + id));

        paciente.setNome(dadosNovos.getNome());
        paciente.setCpf(dadosNovos.getCpf());
        paciente.setDataNasc(dadosNovos.getDataNasc());
        paciente.setConvenio(dadosNovos.getConvenio());
        paciente.setAlergiasComorbidades(dadosNovos.getAlergiasComorbidades());

        return pacienteRepository.save(paciente);
    }
    
    // --- CONVÊNIOS ---
    public Convenio salvarConvenio(Convenio convenio) {
        convenioRepository.findByRegistroAns(convenio.getRegistroAns()).ifPresent(c -> {
            throw new RuntimeException("Registro ANS já cadastrado.");
        });
        return convenioRepository.save(convenio);
    }
    
    public List<Convenio> listarConvenios() {
        return convenioRepository.findAll();
    }
}