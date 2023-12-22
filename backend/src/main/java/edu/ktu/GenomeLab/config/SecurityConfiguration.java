package edu.ktu.GenomeLab.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import static edu.ktu.GenomeLab.models.enums.Permission.*;
import static edu.ktu.GenomeLab.models.enums.Role.ADMIN;
import static edu.ktu.GenomeLab.models.enums.Role.RESEARCHER;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {
    private final JWTAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    private static final String[] WHITE_LIST_URL = {
            "/api/v1/auth/**",
            //"/api/v1/research/**",
            //"/api/v1/admin/**",
            "/api/v1/test/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html"};

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req.requestMatchers(WHITE_LIST_URL)
                                .permitAll()

//                                .requestMatchers("/api/v1/admin/**").hasAnyRole(ADMIN.name())
//                                .requestMatchers(HttpMethod.GET, "/api/v1/admin/**").hasAnyAuthority(ADMIN_READ.name())
//                                .requestMatchers(HttpMethod.POST, "/api/v1/admin/**").hasAnyAuthority(ADMIN_CREATE.name())
//                                .requestMatchers(HttpMethod.PUT, "/api/v1/admin/**").hasAnyAuthority(ADMIN_UPDATE.name())
//                                .requestMatchers(HttpMethod.DELETE, "/api/v1/admin/**").hasAnyAuthority(ADMIN_DELETE.name())

                                .requestMatchers("/api/v1/research/**").hasAnyRole(ADMIN.name(), RESEARCHER.name())
                                .requestMatchers(HttpMethod.GET, "/api/v1/research/**").hasAnyAuthority(ADMIN_READ.name(), RESEARCHER_READ.name())
                                .requestMatchers(HttpMethod.POST, "/api/v1/research/**").hasAnyAuthority(ADMIN_CREATE.name(), RESEARCHER_CREATE.name())
                                .requestMatchers(HttpMethod.PUT, "/api/v1/research/**").hasAnyAuthority(ADMIN_UPDATE.name(), RESEARCHER_UPDATE.name())
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/research/**").hasAnyAuthority(ADMIN_DELETE.name(), RESEARCHER_DELETE.name())
                                .anyRequest()
                                .authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        ;
                return http.build();
    }
}
