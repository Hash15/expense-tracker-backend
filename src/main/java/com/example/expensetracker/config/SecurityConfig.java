package com.example.expensetracker.config;

import com.example.expensetracker.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

  private final JwtTokenUtil jwtTokenUtil;
  private final UserService userService;

  public SecurityConfig(JwtTokenUtil jwtTokenUtil, UserService userService) {
    this.jwtTokenUtil = jwtTokenUtil;
    this.userService = userService;
  }

  @Bean
  public JwtAuthenticationFilter jwtAuthenticationFilter() {
    return new JwtAuthenticationFilter(jwtTokenUtil, userService);
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    // Use the lambda DSL to avoid deprecated chained methods
    http
        // enable CORS using CorsConfigurationSource bean (defined in AppConfig)
        .cors(Customizer.withDefaults())

        // disable CSRF (for stateless REST API)
        .csrf(csrf -> csrf.disable())

        // authorize requests
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers("/api/auth/**", "/h2-console/**").permitAll()
            .anyRequest().authenticated()
        )

        // allow H2 console frames
        .headers(headers -> headers.frameOptions(frame -> frame.disable()))
    ;

    // Register JWT filter before UsernamePasswordAuthenticationFilter
    http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  // Expose AuthenticationManager if needed by controllers/services
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager();
  }
}
