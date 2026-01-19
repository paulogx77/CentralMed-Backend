package br.edu.ifpb.CentralMed.service;

import br.edu.ifpb.CentralMed.model.Agendamento;
import org.springframework.stereotype.Service;
import java.time.format.DateTimeFormatter;

@Service
public class NotificationService {

    public void enviarLembreteDeConsulta(Agendamento agendamento) {

        String emailPaciente = agendamento.getPaciente().getEmail();

        if (emailPaciente == null || emailPaciente.isBlank()) {
            System.out.println("LOG (Simulação): Paciente sem email, notificação pulada.");
            return;
        }

        String data = agendamento.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String hora = agendamento.getHora().format(DateTimeFormatter.ofPattern("HH:mm"));
        String nomeMedico = agendamento.getMedico() != null ? agendamento.getMedico().getNome() : "Clínico Geral";
        String corpo = "Olá, " + agendamento.getPaciente().getNome() + "!\n\n"
                + "Lembrete de sua consulta para " + data + " às " + hora
                + " com Dr(a). " + nomeMedico + ".";

        // --- SIMULAÇÃO DE ENVIO ---
        System.out.println("=========================================");
        System.out.println("SIMULANDO ENVIO DE EMAIL (BACKEND)");
        System.out.println("PARA: " + emailPaciente);
        System.out.println("ASSUNTO: Lembrete de Consulta - CentralMed");
        System.out.println("CORPO: " + corpo);
        System.out.println("=========================================");
    }
}