package ohjelmistoprojekti.lipputoimisto.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {

    @Bean
    public RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.withDefaultRolePrefix()
                .role("ADMIN").implies("MYYJA")
                .role("ADMIN").implies("JARJESTAJA")
                .build();
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/css/**", "/error").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/tapahtumat/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/lipputyypit/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/kayttajat/*").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/liput/qr").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/myyntitapahtumat/minun").hasRole("MYYJA")
                        .requestMatchers(HttpMethod.GET, "/api/myyntitapahtumat/myyja/**").hasRole("ADMIN")

                        .anyRequest().authenticated())

                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
