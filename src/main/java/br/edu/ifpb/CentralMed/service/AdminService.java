package br.edu.ifpb.CentralMed.service;

import br.edu.ifpb.CentralMed.model.EstoqueInsumos;
import br.edu.ifpb.CentralMed.model.Profissional;
import br.edu.ifpb.CentralMed.model.enums.PerfilUsuario;
import br.edu.ifpb.CentralMed.repository.EstoqueRepository;
import br.edu.ifpb.CentralMed.repository.ProfissionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private ProfissionalRepository profissionalRepository;

    @Autowired
    private EstoqueRepository estoqueRepository;

    // ==========================================
    // GESTÃO DE PROFISSIONAIS
    // ==========================================

    /**
     * Lista todos os usuários (ativos e inativos)
     */
    public List<Profissional> listarTodos() {
        return profissionalRepository.findAll();
    }

    /**
     * Filtra usuários por perfil (ex: só MÉDICOS para o agendamento)
     */
    public List<Profissional> listarPorPerfil(PerfilUsuario perfil) {
        return profissionalRepository.findAll().stream()
                .filter(p -> p.getPerfil() == perfil)
                .collect(Collectors.toList());
    }

    /**
     * Salva um novo usuário (Criptografa a senha)
     */
    public Profissional salvar(Profissional profissional) {
        // Criptografa a senha antes de salvar no banco
        String senhaHash = new BCryptPasswordEncoder().encode(profissional.getSenha());
        profissional.setSenha(senhaHash);
        
        // Garante que nasce ativo
        profissional.setAtivo(true);
        
        return profissionalRepository.save(profissional);
    }

    /**
     * Atualiza dados de um usuário existente
     */
    public Profissional atualizar(Long id, Profissional dadosAtualizados) {
        Profissional existente = profissionalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profissional não encontrado"));

        // Atualiza campos cadastrais
        existente.setNome(dadosAtualizados.getNome());
        existente.setUsuarioLogin(dadosAtualizados.getUsuarioLogin());
        existente.setCargo(dadosAtualizados.getCargo());
        existente.setCrmRegistro(dadosAtualizados.getCrmRegistro());
        existente.setPerfil(dadosAtualizados.getPerfil());

        // Lógica de Senha: Só altera se o usuário enviou uma nova
        if (dadosAtualizados.getSenha() != null && !dadosAtualizados.getSenha().isEmpty()) {
            String senhaHash = new BCryptPasswordEncoder().encode(dadosAtualizados.getSenha());
            existente.setSenha(senhaHash);
        }

        return profissionalRepository.save(existente);
    }

    /**
     * Bloqueia ou Desbloqueia o acesso (Soft Delete)
     */
    public void alternarStatus(Long id) {
        Profissional p = profissionalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profissional não encontrado"));
        
        // Inverte o status atual (se true vira false, se null vira true)
        boolean statusAtual = p.getAtivo() != null && p.getAtivo();
        p.setAtivo(!statusAtual);
        
        profissionalRepository.save(p);
    }

    // ==========================================
    // GESTÃO DE ESTOQUE
    // ==========================================

    public EstoqueInsumos adicionarEstoque(Long idInsumo, Integer quantidadeAdicional) {
        EstoqueInsumos item = estoqueRepository.findById(idInsumo)
                .orElseThrow(() -> new RuntimeException("Insumo não encontrado"));

        item.setQtdeAtual(item.getQtdeAtual() + quantidadeAdicional);

        return estoqueRepository.save(item);
    }

    public List<EstoqueInsumos> verificarEstoqueBaixo() {
        return estoqueRepository.findAll().stream()
                .filter(i -> i.getQtdeAtual() <= i.getQtdeMinima())
                .collect(Collectors.toList());
    }
}