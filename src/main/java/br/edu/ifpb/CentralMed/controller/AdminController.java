package br.edu.ifpb.CentralMed.controller;

// Imports Organizados
import br.edu.ifpb.CentralMed.model.Convenio;
import br.edu.ifpb.CentralMed.model.EstoqueInsumos;
import br.edu.ifpb.CentralMed.model.ProcedimentoTuss;
import br.edu.ifpb.CentralMed.model.Profissional;
import br.edu.ifpb.CentralMed.model.TabelaPrecos;
import br.edu.ifpb.CentralMed.model.enums.PerfilUsuario;
import br.edu.ifpb.CentralMed.repository.TabelaPrecosRepository;
import br.edu.ifpb.CentralMed.service.AdminService;
import br.edu.ifpb.CentralMed.service.ConvenioService;
import br.edu.ifpb.CentralMed.service.ProcedimentoTussService; // Estava faltando
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired private AdminService adminService;
    @Autowired private ConvenioService convenioService;
    @Autowired private ProcedimentoTussService procedimentoTussService;
    @Autowired private TabelaPrecosRepository tabelaPrecosRepository;
    
    

    // === PROFISSIONAIS ===

    @PostMapping("/profissionais")
    public Profissional cadastrarProfissional(@RequestBody Profissional profissional) {
        return adminService.salvarProfissional(profissional);
    }

    @GetMapping("/profissionais")
    public List<Profissional> listarTodosProfissionais() { // Nome do método ficou mais claro
        return adminService.listarTodos();
    }

    @GetMapping("/profissionais/medicos")
    public List<Profissional> listarMedicos() {
        return adminService.listarPorPerfil(PerfilUsuario.MEDICO);
    }

    // === ESTOQUE ===

    @GetMapping("/estoque/alertas")
    public List<EstoqueInsumos> getAlertasEstoque() {
        return adminService.verificarEstoqueBaixo();
    }
    // === CONVÊNIOS ===

    @PostMapping("/convenios")
    public Convenio criarConvenio(@RequestBody Convenio convenio) {
        return convenioService.salvarConvenio(convenio);
    }

    @GetMapping("/convenios")
    public List<Convenio> listarConvenios() { // APENAS UM GET /convenios agora
        return convenioService.listarConvenios();
    }

    @PutMapping("/convenios/{id}")
    public Convenio atualizarConvenio(@PathVariable Long id, @RequestBody Convenio convenio) {
        return convenioService.atualizarConvenio(id, convenio);
    }

    @DeleteMapping("/convenios/{id}")
    public ResponseEntity<Void> deletarConvenio(@PathVariable Long id) {
        convenioService.deletarConvenio(id);
        return ResponseEntity.noContent().build();
    }

    // === PROCEDIMENTOS TUSS ===

    @PostMapping("/procedimentos")
    public ProcedimentoTuss criarProcedimento(@RequestBody ProcedimentoTuss procedimento) {
        return procedimentoTussService.salvar(procedimento);
    }

    @GetMapping("/procedimentos")
    public List<ProcedimentoTuss> listarProcedimentos() {
        return procedimentoTussService.listarTodos();
    }

    @PostMapping("/tabela-precos")
    public TabelaPrecos definirPreco(@RequestBody TabelaPrecos preco) {
        return tabelaPrecosRepository.save(preco);
    }
    @GetMapping("/tabela-precos/{convenioId}")
    public List<TabelaPrecos> listarPrecosPorConvenio(@PathVariable Long convenioId){
        // Lógica para filtrar no Service
        // return convenioService.getTabelaDePrecos(convenioId);
        return new ArrayList<>(); // Por enquanto, até criarmos a tela
    }
}