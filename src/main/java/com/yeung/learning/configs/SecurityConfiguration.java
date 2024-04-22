package com.yeung.learning.configs;

import java.io.IOException;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.util.StringUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

// @EnableWebSecurity
// @EnableMethodSecurity

@Configuration
public class SecurityConfiguration {

    @Autowired
    HttpSession httpSession;

    @Value("${com.yeung.learning.google.jwk-set-uri}")
    private String googleJwkSetUri;

    @Value("${com.yeung.learning.okta.jwk-set-uri}")
    private String oktaJwkSetUri;

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
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));
        http.csrf(csrf -> csrf.disable());
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("api/auth/**")
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
                .jwt(jwt -> jwt.decoder(jwtDecoder()))
                .accessDeniedHandler(accessDeniedHandler())
                .authenticationEntryPoint(authenticationEntryPoint()));
        http.formLogin((form) -> form.loginPage("http://localhost:3000/login"));
        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("http://localhost:3000");
        corsConfiguration.addAllowedOrigin("http://localhost:8080");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new AccessDeniedHandler() {

            @Override
            public void handle(HttpServletRequest request, HttpServletResponse response,
                    AccessDeniedException accessDeniedException) throws IOException, ServletException {
                System.out.println("error in AccessDeniedHandler");
                System.out.println(accessDeniedException.getMessage());
                response.sendRedirect("http://localhost:3000/dashboard");
            }
        };
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new AuthenticationEntryPoint() {
            @Override
            public void commence(HttpServletRequest request, HttpServletResponse response,
                    org.springframework.security.core.AuthenticationException authException)
                    throws IOException, ServletException {
                System.out.println("error in AuthenticationEntryPoint");
                System.out.println(authException.getMessage());
                httpSession.removeAttribute("token");
                httpSession.removeAttribute("tokenType");
                response.sendRedirect("http://localhost:3000/login");
            }
        };
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        JwtDecoder googleJwtDecoder = NimbusJwtDecoder.withJwkSetUri(googleJwkSetUri).build();
        JwtDecoder oktaJwtDecoder = NimbusJwtDecoder.withJwkSetUri(oktaJwkSetUri).build();
        return new JwtDecoder() {
            @Override
            public Jwt decode(String token) throws JwtException {
                String tokenType = (String) httpSession.getAttribute("tokenType");
                System.out.println("tokenType in JwtDecoder: " + tokenType);
                if ("okta".equals(tokenType)) {
                    return oktaJwtDecoder.decode(token);
                } else if ("google".equals(tokenType)) {
                    return googleJwtDecoder.decode(token);
                }
                return null;
            }
        };
    }
}
