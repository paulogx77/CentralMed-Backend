package br.edu.ifpb.CentralMed.service;

import br.edu.ifpb.CentralMed.model.GuiaConsulta;
import br.edu.ifpb.CentralMed.model.enums.StatusNfs;
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
        return guiaRepository.findByStatus(StatusNfs.StatusGuia.ABERTA);
    }

    @Transactional
    public void faturarLote(List<Long> idsDasGuias) {
        List<GuiaConsulta> guias = guiaRepository.findAllById(idsDasGuias);

        for (GuiaConsulta guia : guias) {
            if (guia.getStatus() == StatusNfs.StatusGuia.ABERTA) {
                guia.setStatus(StatusNfs.StatusGuia.FATURADA);
            }
        }

        guiaRepository.saveAll(guias);
        // Lógica futura: Gerar o XML do Lote TISS aqui
    }
}