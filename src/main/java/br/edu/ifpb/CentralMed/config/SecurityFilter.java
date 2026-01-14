package br.edu.ifpb.CentralMed.config;

import br.edu.ifpb.CentralMed.repository.ProfissionalRepository;
import br.edu.ifpb.CentralMed.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ProfissionalRepository profissionalRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        var token = this.recuperarToken(request);

        if (token != null) {
            // Valida o token e extrai o login
            var login = tokenService.validarToken(token);

            // Se o login for válido, busca o usuário
            if (login != null && !login.isEmpty()) {

                // Usa o método que retorna UserDetails para maior compatibilidade com Spring Security
                profissionalRepository.findUserDetailsByUsuarioLogin(login).ifPresent(userDetails -> {

                    // Cria a autenticação com o UserDetails (que contém as authorities)
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    // Coloca o usuário autenticado no contexto da requisição
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                });
            }
        }

        // Continua o fluxo para os próximos filtros do Spring
        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.replace("Bearer ", "").trim();
    }
}