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

                    // 1. ROTAS PÚBLICAS (Acesso livre)
                    req.requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll();
                    req.requestMatchers("/v3/api-docs/**", "/swagger-ui/**").permitAll();

                    // 2. EXCEÇÃO: Libera a LEITURA da lista de médicos para qualquer usuário autenticado.
                    req.requestMatchers(HttpMethod.GET, "/api/admin/profissionais/medicos").authenticated();

                    // --- NOVA REGRA PARA UPLOAD DE ARQUIVOS ---
                    // Libera qualquer usuário logado para enviar anexos
                    req.requestMatchers("/api/anexos/**").authenticated();
                    // ---------------------------------------------

                    // 3. REGRA GERAL DE ADMIN
                    req.requestMatchers("/api/admin/**").hasRole("ADMIN");
                    req.requestMatchers("/api/faturamento/**").hasRole("ADMIN");
                    req.requestMatchers("/api/notas-fiscais/**").hasRole("ADMIN");

                    // 4. REGRA FINAL (Qualquer outra coisa precisa de login)
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