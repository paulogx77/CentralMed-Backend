package br.edu.ifpb.CentralMed.controller;

import br.edu.ifpb.CentralMed.model.EstoqueInsumos;
import br.edu.ifpb.CentralMed.model.Profissional;
import br.edu.ifpb.CentralMed.model.enums.PerfilUsuario;
import br.edu.ifpb.CentralMed.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/profissionais")
    public Profissional cadastrarProfissional(@RequestBody Profissional profissional) {
        return adminService.salvarProfissional(profissional);
    }

    @GetMapping("/profissionais")
    public List<Profissional> listarTodos() {
        return adminService.listarTodos();
    }

    @GetMapping("/profissionais/medicos")
    public List<Profissional> listarMedicos() {
        return adminService.listarPorPerfil(PerfilUsuario.MEDICO);
    }

    @GetMapping("/estoque/alertas")
    public List<EstoqueInsumos> getAlertasEstoque() {
        return adminService.verificarEstoqueBaixo();
    }

    @PostMapping("/estoque/{id}/adicionar")
    public EstoqueInsumos adicionarEstoque(@PathVariable Long id, @RequestParam Integer quantidade) {
        return adminService.adicionarEstoque(id, quantidade);
    }
}