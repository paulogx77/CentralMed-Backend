package br.edu.ifpb.CentralMed.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    @Autowired
    SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // Habilita a configuração de CORS que definimos no WebConfig
                .cors(Customizer.withDefaults())
                // Desabilita proteção CSRF pois usaremos Tokens (Stateless)
                .csrf(csrf -> csrf.disable())
                // Configura a sessão como STATELESS: não guarda estado do usuário no servidor
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Define as regras de autorização para cada endpoint
                .authorizeHttpRequests(req -> {

                    // 1. ROTAS PÚBLICAS (Acesso liberado sem login)
                    req.requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll();
                    req.requestMatchers(HttpMethod.POST, "/api/auth/register").permitAll();

                    // 2. ROTAS DE ADMIN (Acesso restrito apenas ao perfil ADMIN)
                    req.requestMatchers("/api/admin/**").hasRole("ADMIN");
                    req.requestMatchers("/api/faturamento/**").hasRole("ADMIN");

                    // 3. REGRA GERAL (Catch-all)
                    // Qualquer outra rota que não foi definida acima, precisa de autenticação.
                    // Isso cobre /api/recepcao, /api/medico, /api/triagem, etc.
                    req.anyRequest().authenticated();
                })
                // Adiciona nosso filtro JWT antes do filtro padrão do Spring
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}