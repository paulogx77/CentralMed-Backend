package br.edu.ifpb.CentralMed.config.auditoria;

import br.edu.ifpb.CentralMed.model.LogAcessoProntuario;
import br.edu.ifpb.CentralMed.repository.LogAcessoRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

@Aspect
@Component
public class AuditoriaAspect {

    @Autowired private LogAcessoRepository logRepository;

    // Roda DEPOIS que um método com @Auditable executar com sucesso
    @AfterReturning(pointcut = "@annotation(Auditable)")
    public void auditarAcesso(JoinPoint joinPoint) {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        String usuarioLogado = "ANONYMOUS";
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            usuarioLogado = ((UserDetails)principal).getUsername();
        }

        // Tentamos extrair o ID do paciente da URL (funciona para /.../{pacienteId})
        // O AOP não tem acesso fácil ao @PathVariable, então usamos um truque.
        // Isso é uma simplificação. Em produção, usaríamos formas mais robustas.
        String[] pathParts = request.getRequestURI().split("/");
        Long pacienteId = null;
        try {
            // Assume que o ID é o último segmento da URL
            pacienteId = Long.parseLong(pathParts[pathParts.length - 1]);
        } catch (NumberFormatException e) {
            // Ignora se não for um número (Ex: a rota é /medico/historico e não /medico/historico/1)
        }

        if(pacienteId != null){
            LogAcessoProntuario log = new LogAcessoProntuario();
            log.setPacienteId(pacienteId);
            log.setUsuarioLogin(usuarioLogado);
            log.setDataHoraAcesso(LocalDateTime.now());
            log.setEndpointAcessado(request.getRequestURI());
            log.setIpOrigem(request.getRemoteAddr());

            logRepository.save(log);
        }
    }
}