package br.edu.ifpb.CentralMed.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    @Autowired
    private SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize

                        // 1. ROTAS PÚBLICAS
                        .requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll()
                        .requestMatchers("/api/painel/**").permitAll()

                        // 2. CORREÇÃO: REGRA EXPLÍCITA PARA O ENDPOINT COM PROBLEMA
                        // Garante que POST em /agendamentos seja aceito para RECEPCAO e ADMIN
                        .requestMatchers(HttpMethod.POST, "/api/recepcao/agendamentos").hasAnyRole("RECEPCAO", "ADMIN")
                        .requestMatchers("/api/compras/**").hasRole("ADMIN")
                        // 3. ROTAS GERAIS DE RECEPÇÃO
                        // Mudei de .authenticated() para .hasAnyRole para ser mais seguro e garantir o match correto
                        .requestMatchers("/api/recepcao/**").hasAnyRole("RECEPCAO", "ADMIN")

                        .requestMatchers("/api/fornecedores/**").hasRole("ADMIN")


                        // 4. ROTAS ESPECÍFICAS
                        .requestMatchers("/api/triagem/**").authenticated()
                        .requestMatchers("/api/agendamentos/horarios-ocupados").authenticated()
                        .requestMatchers("/api/fornecedores/**").authenticated()

                        // Rotas de leitura (GET)
                        .requestMatchers(HttpMethod.GET, "/api/admin/convenios").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/admin/profissionais/medicos").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/medico/fila-completa").authenticated()

                        // 5. ROTAS EXCLUSIVAS DO ADMIN
                        .requestMatchers("/api/faturamento/**").hasRole("ADMIN")
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // 6. REGRA FINAL
                        .anyRequest().authenticated()
                )
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

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Permite a origem do seu frontend
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "PATCH"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}