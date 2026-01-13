package br.edu.ifpb.CentralMed.controller;

import br.edu.ifpb.CentralMed.model.EstoqueInsumos;
import br.edu.ifpb.CentralMed.model.Profissional;
import br.edu.ifpb.CentralMed.model.enums.PerfilUsuario;
import br.edu.ifpb.CentralMed.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    // ==========================================
    // GESTÃO DE PROFISSIONAIS
    // ==========================================

    @PostMapping("/profissionais")
    public ResponseEntity<Profissional> cadastrarProfissional(@RequestBody Profissional profissional) {
        // Correção: Chama o método 'salvar' (que criptografa senha)
        return ResponseEntity.ok(adminService.salvar(profissional));
    }

    @GetMapping("/profissionais")
    public List<Profissional> listarTodos() {
        return adminService.listarTodos();
    }

    @GetMapping("/profissionais/medicos")
    public List<Profissional> listarMedicos() {
        return adminService.listarPorPerfil(PerfilUsuario.MEDICO);
    }

    @PutMapping("/profissionais/{id}")
    public ResponseEntity<Profissional> atualizarProfissional(@PathVariable Long id, @RequestBody Profissional dados) {
        return ResponseEntity.ok(adminService.atualizar(id, dados));
    }

    @PatchMapping("/profissionais/{id}/status")
    public ResponseEntity<Void> alternarStatus(@PathVariable Long id) {
        adminService.alternarStatus(id);
        return ResponseEntity.noContent().build();
    }

    // ==========================================
    // GESTÃO DE ESTOQUE
    // ==========================================

    @GetMapping("/estoque/alertas")
    public List<EstoqueInsumos> getAlertasEstoque() {
        return adminService.verificarEstoqueBaixo();
    }

    @PostMapping("/estoque/{id}/adicionar")
    public EstoqueInsumos adicionarEstoque(@PathVariable Long id, @RequestParam Integer quantidade) {
        return adminService.adicionarEstoque(id, quantidade);
    }
}