package br.ufsm.spi.ProSaude.infra.security;


import br.ufsm.spi.ProSaude.infra.exceptions.CustomAccessDeniedHandler;
import lombok.AllArgsConstructor;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final AutenticacaoFilter autenticacaoFilter;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http

                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                .csrf(crsf -> crsf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        // Permite criar usuários sem estar logado (para o seu primeiro teste)
                        .requestMatchers(HttpMethod.POST, "/usuario").permitAll()
                        .requestMatchers(HttpMethod.POST, "/login").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/usuario").permitAll()




                        // Qualquer outra requisição exige login
                        .requestMatchers(HttpMethod.POST, "/aluno/importar").hasAnyRole("ADMIN", "BOLSISTA")
                        .anyRequest().authenticated()
                )
                .exceptionHandling(handling -> handling
                        .accessDeniedHandler(customAccessDeniedHandler)
                )
                .addFilterBefore(this.autenticacaoFilter, UsernamePasswordAuthenticationFilter.class)
                .build(); // sessao nao salvs
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Permite o frontend Angular
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));

        // Permite os métodos padrões (POST, GET, etc.) e o OPTIONS (preflight request)
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Permite todos os cabeçalhos, crucial para o token "Authorization"
        configuration.setAllowedHeaders(Arrays.asList("*"));

        // Permite o envio de credenciais (se o Angular estiver configurado para isso)
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Aplica esta configuração a todos os caminhos (/**)
        source.registerCorsConfiguration("/**", configuration);
        return source;
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
