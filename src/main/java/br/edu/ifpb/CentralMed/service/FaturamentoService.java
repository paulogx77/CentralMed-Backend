package br.edu.ifpb.CentralMed.service;

import br.edu.ifpb.CentralMed.model.GuiaConsulta;
import br.edu.ifpb.CentralMed.model.enums.StatusGuia; // <--- Import correto
import br.edu.ifpb.CentralMed.repository.GuiaConsultaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class FaturamentoService {

    @Autowired
    private GuiaConsultaRepository guiaRepository;

    public List<GuiaConsulta> buscarGuiasAbertas() {
        // --- CORREÇÃO AQUI ---
        // Buscando pelo enum StatusGuia diretamente
        return guiaRepository.findByStatus(StatusGuia.ABERTA);
    }

    @Transactional
    public void faturarLote(List<Long> idsDasGuias) {
        List<GuiaConsulta> guias = guiaRepository.findAllById(idsDasGuias);

        for (GuiaConsulta guia : guias) {
            if (guia.getStatus() == StatusGuia.ABERTA) {
                // --- CORREÇÃO AQUI ---
                // Setando o enum StatusGuia diretamente
                guia.setStatus(StatusGuia.FATURADA);
            }
        }

        guiaRepository.saveAll(guias);
    }
}