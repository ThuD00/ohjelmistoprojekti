package ohjelmistoprojekti.lipputoimisto;

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

import static org.springframework.boot.security.autoconfigure.web.servlet.PathRequest.toH2Console;

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
                        .requestMatchers("/css/**").permitAll()

                        .requestMatchers(toH2Console()).permitAll() // h2 consolea varten

                        .requestMatchers(HttpMethod.GET, "/api/tapahtumat/**").permitAll()
                        .anyRequest().authenticated())

                .httpBasic(Customizer.withDefaults())

                // h2 consolea varten
                .headers(headers -> headers
                        .frameOptions(frameoptions -> frameoptions.disable()));
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
