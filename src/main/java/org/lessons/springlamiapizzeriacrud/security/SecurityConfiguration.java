package org.lessons.springlamiapizzeriacrud.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    @Bean
    DbUserDetailService userDetailsService() {
        return new DbUserDetailService();
    }

    @Bean //??
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .requestMatchers("/api/**").permitAll() //per far funzionare le API
                .requestMatchers(HttpMethod.POST).hasAuthority("ADMIN")
                .requestMatchers("/pizzas/create").hasAuthority("ADMIN")
                .requestMatchers("/pizzas/edit/**").hasAuthority("ADMIN")
                .requestMatchers("/ingredients").hasAuthority("ADMIN")
                .requestMatchers("/offers/**").hasAuthority("ADMIN")
                .requestMatchers("/pizzas").hasAnyAuthority("ADMIN","USER")
                .requestMatchers("/pizzas/**").hasAnyAuthority("ADMIN","USER")
                .requestMatchers(("/**")).permitAll()
                .and().formLogin()
                    .loginPage("/login")
                .and().logout()
                    .logoutSuccessUrl("/");

        http.csrf().disable(); //per far funzionare le API

        return http.build();
    }

}
