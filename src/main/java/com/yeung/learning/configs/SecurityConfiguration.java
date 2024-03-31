package com.yeung.learning.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.util.StringUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

// @EnableWebSecurity
// @EnableMethodSecurity

@Configuration
public class SecurityConfiguration {

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String jwkSetUri;

    public class CustomBearerTokenResolver implements BearerTokenResolver {

        @Override
        public String resolve(HttpServletRequest request) {
            HttpSession session = request.getSession();
            String token = (String) session.getAttribute("token");
            if (StringUtils.hasText(token)) {
                return token;
            }
            return null;
        }
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize.requestMatchers("api/auth/**")
                .permitAll()
                .requestMatchers("api/oauth/**")
                .permitAll()
                .requestMatchers("api/test/**")
                .permitAll()
                .anyRequest()
                .authenticated());
        http.sessionManagement((s) -> s.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));
        http.oauth2ResourceServer((oauth2ResourceServer) -> oauth2ResourceServer
                .bearerTokenResolver(new CustomBearerTokenResolver())
                .jwt(jwt -> jwt.decoder(jwtDecoder())));
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));
        http.csrf(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("http://localhost:3000");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Bean
    public JwtDecoder jwtDecoder() {

        JwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();

        return new JwtDecoder() {
            @Override
            public Jwt decode(String token) throws JwtException {
                System.out.println("token: " + token);
                Jwt jwt = jwtDecoder.decode(token);
                System.out.println("jwt: " + jwt);
                return jwt;
            }
        };
    }
}
