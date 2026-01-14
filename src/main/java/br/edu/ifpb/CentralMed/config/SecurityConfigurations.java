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
    private SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(req -> {

                    // 1. ROTAS PÚBLICAS (NÃO precisa de login)
                    req.requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll();

                    // --- A CORREÇÃO DE ORDEM ESTÁ AQUI ---
                    // 2. EXCEÇÕES E ROTAS ESPECÍFICAS
                    // Libera a LEITURA de médicos para qualquer um logado, ANTES da regra geral de Admin
                    req.requestMatchers(HttpMethod.GET, "/api/admin/profissionais/medicos").authenticated();

                    // 3. REGRA GERAL DE ADMIN
                    // O restante das rotas de admin são trancadas apenas para ROLE_ADMIN
                    req.requestMatchers("/api/admin/**").hasRole("ADMIN");
                    req.requestMatchers("/api/faturamento/**").hasRole("ADMIN");
                    req.requestMatchers("/api/notas-fiscais/**").hasRole("ADMIN");

                    // 4. REGRA FINAL GERAL
                    // Se não for nenhuma das acima, basta estar autenticado.
                    req.anyRequest().authenticated();
                })
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