package br.edu.ifpb.CentralMed.service;

import br.edu.ifpb.CentralMed.model.Convenio;
import br.edu.ifpb.CentralMed.repository.ConvenioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional; // Import para Optional

@Service
public class ConvenioService {

    @Autowired
    private ConvenioRepository convenioRepository;

    public Convenio salvarConvenio(Convenio convenio) {
        Optional<Convenio> existente = convenioRepository.findByRegistroAns(convenio.getRegistroAns());
        if (existente.isPresent() && !existente.get().getId().equals(convenio.getId())) {
            throw new RuntimeException("Registro ANS já cadastrado.");
        }
        return convenioRepository.save(convenio);
    }

    public List<Convenio> listarConvenios() {
        return convenioRepository.findAll();
    }

    // --- MÉTODO ATUALIZAR QUE FALTAVA ---
    public Convenio atualizarConvenio(Long id, Convenio dadosAtualizados) {
        Convenio convenioExistente = convenioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Convênio não encontrado com o ID: " + id));
        
        // Valida se o novo Registro ANS não pertence a outro convênio
        Optional<Convenio> outroComMesmoRegistro = convenioRepository.findByRegistroAns(dadosAtualizados.getRegistroAns());
        if(outroComMesmoRegistro.isPresent() && !outroComMesmoRegistro.get().getId().equals(id)) {
            throw new RuntimeException("Registro ANS já pertence a outro convênio.");
        }
        
        convenioExistente.setNome(dadosAtualizados.getNome());
        convenioExistente.setCnpj(dadosAtualizados.getCnpj());
        convenioExistente.setRegistroAns(dadosAtualizados.getRegistroAns());
        
        return convenioRepository.save(convenioExistente);
    }

    // --- MÉTODO DELETAR QUE FALTAVA ---
    public void deletarConvenio(Long id) {
        if (!convenioRepository.existsById(id)) {
            throw new RuntimeException("Convênio não encontrado com o ID: " + id);
        }
        convenioRepository.deleteById(id);
    }
}