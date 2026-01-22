package br.edu.ifpb.CentralMed.controller;

import br.edu.ifpb.CentralMed.model.UltimaChamada;
import br.edu.ifpb.CentralMed.repository.UltimaChamadaRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/painel")
public class PainelController {

    private final UltimaChamadaRepository repository;

    public PainelController(UltimaChamadaRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/ultima")
    public UltimaChamada getUltimaChamada(){
        return repository.findById(1L)
                .orElse(new UltimaChamada(1L, "Aguarde", "...", null));
    }
}