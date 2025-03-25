package com.lcwd.electronic.store.ElectronicStore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity(debug = true)
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception{

        security.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.disable());

        security.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());

        security.authorizeHttpRequests(request ->
                request.requestMatchers(HttpMethod.DELETE, "/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/users").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/users/**").hasAnyRole("ADMIN","NORMAL")
                        .requestMatchers(HttpMethod.GET,"/products/**").permitAll()
                        .requestMatchers("/products/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/users/**").permitAll()
                        .requestMatchers(HttpMethod.GET,"/categories/**").permitAll()
                        .requestMatchers("/categories/**").hasRole("ADMIN")
                        .anyRequest().permitAll()
                );


        //to bypass auth to configure basic users on startups
        //security.authorizeHttpRequests(request -> request.anyRequest().authenticated());

        security.httpBasic(Customizer.withDefaults());
        return security.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
