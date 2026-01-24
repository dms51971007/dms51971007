package com.signalm.manager.config;

import com.signalm.manager.serv.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserService userService;

    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    public SecurityConfig(UserService userService, CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler) {
        this.userService = userService;
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authenticationProvider(authenticationProvider());

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(
                        "/resource/**",
                        "/css/**",
                        "/js/**",
                        "/img/**",
                        "/webjars/**",
                        "/showMyLoginPage",
                        "/access-denied")
                .permitAll()
                .requestMatchers("/").hasRole("USER")
                .requestMatchers("/task/**").hasRole("USER")
                .requestMatchers("/user/**").hasRole("ADMIN"))
            .formLogin(form -> form
                .loginPage("/showMyLoginPage")
                .loginProcessingUrl("/authenticateTheUser")
                .successHandler(customAuthenticationSuccessHandler)
                .permitAll())
            .logout(logout -> logout
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
                .permitAll())
            .exceptionHandling(ex -> ex.accessDeniedPage("/access-denied"))
            .csrf(csrf -> csrf.ignoringRequestMatchers("/task/savememo"));

        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    //authenticationProvider bean definition
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }

}
