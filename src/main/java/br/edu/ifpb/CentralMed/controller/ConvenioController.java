package br.edu.ifpb.CentralMed.controller;

import br.edu.ifpb.CentralMed.model.Convenio;
import br.edu.ifpb.CentralMed.repository.ConvenioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/convenios")
public class ConvenioController {

    @Autowired
    private ConvenioRepository repository;

    @PostMapping
    public Convenio salvar(@RequestBody Convenio convenio) {
        return repository.save(convenio);
    }

    @GetMapping
    public List<Convenio> listar() {
        return repository.findAll();
    }
}