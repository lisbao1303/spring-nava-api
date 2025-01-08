package com.elisbao.spring_nava_api.configs;

import com.elisbao.spring_nava_api.security.JWTAuthenticationFilter;
import com.elisbao.spring_nava_api.security.JWTAuthorizationFilter;
import com.elisbao.spring_nava_api.security.JWTUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

        private final JWTUtil jwtUtil;

        public SecurityConfig(UserDetailsService userDetailsService, JWTUtil jwtUtil) {
                this.userDetailsService = userDetailsService;
                this.jwtUtil = jwtUtil;
        }

        private static final String[] PUBLIC_MATCHERS = {
                        "/",
                "/swagger-ui/**",
                "/v3/api-docs/**"
        };
        private static final String[] PUBLIC_MATCHERS_POST = {
                        "/user",
                        "/login"
        };

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

                http
                        .csrf(AbstractHttpConfigurer::disable) // Desabilita CSRF
                        .cors(cors -> cors.configurationSource(corsConfigurationSource())); // Configura CORS

                AuthenticationManagerBuilder authenticationManagerBuilder = http
                                .getSharedObject(AuthenticationManagerBuilder.class);
                authenticationManagerBuilder.userDetailsService(this.userDetailsService)
                                .passwordEncoder(bCryptPasswordEncoder());
            AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

                http.authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, PUBLIC_MATCHERS_POST).permitAll()
                        .requestMatchers(PUBLIC_MATCHERS).permitAll()
                        .anyRequest().authenticated()
                ).authenticationManager(authenticationManager);

                http.addFilter(new JWTAuthenticationFilter(authenticationManager, this.jwtUtil));
                http.addFilter(new JWTAuthorizationFilter(authenticationManager, this.jwtUtil,
                                this.userDetailsService));

                http.sessionManagement(session -> session
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Define a política como STATELESS
                        );
                return http.build();
        }

        @Bean
        CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
                configuration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE"));
                final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                return source;
        }

        @Bean
        public BCryptPasswordEncoder bCryptPasswordEncoder() {
                return new BCryptPasswordEncoder();
        }

}
