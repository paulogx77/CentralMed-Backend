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
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        var token = this.recuperarToken(request);

        if (token != null) {
            var login = tokenService.validarToken(token);

            if (login != null && !login.isEmpty()) {
                profissionalRepository.findUserDetailsByUsuarioLogin(login).ifPresent(userDetails -> {

                    // --- INICIO DO DEBUG (ISSO VAI SALVAR A PÁTRIA) ---
                    System.out.println("--------------------------------------------------");
                    System.out.println("QUEM ESTÁ TENTANDO ENTRAR?");
                    System.out.println("Login: " + userDetails.getUsername());
                    System.out.println("Permissões (Authorities): " + userDetails.getAuthorities());
                    System.out.println("URL Tentada: " + request.getRequestURI());
                    System.out.println("--------------------------------------------------");
                    // --- FIM DO DEBUG ---

                    var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                });
            }
        }
        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.replace("Bearer ", "").trim();
    }
}