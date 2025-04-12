package com.lcwd.electronic.store.ElectronicStore.config;

import com.lcwd.electronic.store.ElectronicStore.security.JwtAuthenticationEntryPoint;
import com.lcwd.electronic.store.ElectronicStore.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity(debug = true)
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception{

        security.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.disable());

        security.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());

        security.authorizeHttpRequests(request ->
                request.requestMatchers(HttpMethod.DELETE, "/users/**").hasRole(AppConstants.ROLE_ADMIN)
                        .requestMatchers(HttpMethod.POST,"/users").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/users/**").hasAnyRole(AppConstants.ROLE_ADMIN,AppConstants.ROLE_NORMAL)
                        .requestMatchers(HttpMethod.GET,"/products/**").permitAll()
                        .requestMatchers("/products/**").hasRole(AppConstants.ROLE_ADMIN)
                        .requestMatchers(HttpMethod.GET,"/users/**").permitAll()
                        .requestMatchers(HttpMethod.GET,"/categories/**").permitAll()
                        .requestMatchers("/categories/**").hasRole(AppConstants.ROLE_ADMIN)
                        .requestMatchers(HttpMethod.POST,"/auth/generate-token").permitAll()
                        .requestMatchers("/auth/**").authenticated()
                        .anyRequest().permitAll()
                );


        //to bypass auth to configure basic users on startups
        //security.authorizeHttpRequests(request -> request.anyRequest().authenticated());

        //security.httpBasic(Customizer.withDefaults());

        //entry point
        security.exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint));

        //session creation policy
        security.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        //main ->
        security.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return security.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
        return builder.getAuthenticationManager();
    }

}
