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
import java.util.Optional; // Importe
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired private ProfissionalRepository profissionalRepository;
    @Autowired private EstoqueRepository estoqueRepository;
    @Autowired private ConvenioRepository convenioRepository;
    @Autowired private PacienteRepository pacienteRepository;
    @Autowired private LoteInsumoRepository loteRepository;

    // --- PROFISSIONAIS ---
    
    // Seu salvarProfissional pode ficar como está ou pode ser usado o do AuthController
    public Profissional salvarProfissional(Profissional profissional) {
        return profissionalRepository.save(profissional);
    }
    
    public List<Profissional> listarTodos() {
        return profissionalRepository.findAll();
    }
    
    public List<Profissional> listarPorPerfil(PerfilUsuario perfil) {
        // A melhor forma seria criar um método no Repository, mas esta funciona
        return profissionalRepository.findAll().stream()
                .filter(p -> p.getPerfil() == perfil)
                .collect(Collectors.toList());
    }
    
    // --- NOVO MÉTODO ---
    public Optional<Profissional> findById(Long id) {
        return profissionalRepository.findById(id);
    }

    // --- NOVO MÉTODO ---
    public Profissional atualizarProfissional(Long id, Profissional dadosAtualizados) {
        Profissional p = profissionalRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Profissional não encontrado com ID: " + id));

        // Atualiza os campos permitidos
        p.setNome(dadosAtualizados.getNome());
        p.setCargo(dadosAtualizados.getCargo());
        p.setPerfil(dadosAtualizados.getPerfil());
        p.setCrmRegistro(dadosAtualizados.getCrmRegistro());
        // Não atualizamos senha nem login por aqui por segurança

        return profissionalRepository.save(p);
    }
    
    // --- NOVO MÉTODO (INATIVAR/DELETAR) ---
    public void deletarProfissional(Long id) {
        // Se houverem consultas atreladas a este profissional, pode dar erro.
        // A lógica de "inativação" (setar um campo 'ativo=false') seria mais segura
        // para manter o histórico, mas um delete resolve o requisito do Documento de Visão F2.4.
        if (!profissionalRepository.existsById(id)) {
            throw new RuntimeException("Profissional não encontrado com ID: " + id);
        }
        profissionalRepository.deleteById(id);
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