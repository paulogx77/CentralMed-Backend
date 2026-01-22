package br.edu.ifpb.CentralMed.service;

import br.edu.ifpb.CentralMed.model.Agendamento;
import br.edu.ifpb.CentralMed.model.UltimaChamada;
import br.edu.ifpb.CentralMed.repository.AgendamentoRepository;
import br.edu.ifpb.CentralMed.repository.UltimaChamadaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class PainelService {
    @Autowired private UltimaChamadaRepository chamadaRepository;
    @Autowired private AgendamentoRepository agendamentoRepository;

    @Transactional
    public void registrarChamada(Long agendamentoId, String local) {
        Agendamento agendamento = agendamentoRepository.findById(agendamentoId)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado"));

        UltimaChamada chamada = chamadaRepository.findById(1L)
                .orElse(new UltimaChamada()); // Pega a linha 1 ou cria uma nova

        chamada.setSenha(agendamento.getSenhaPainel());
        chamada.setLocal(local);
        chamada.setDataHora(LocalDateTime.now());

        chamadaRepository.save(chamada);
    }
}