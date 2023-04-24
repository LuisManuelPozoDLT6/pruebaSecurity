package com.example.projecttest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityBuilder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig  {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ComponentRoles successHandler;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth)throws Exception{
        auth.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery("SELECT username, password, enabled FROM users WHERE username = ?")
                .authoritiesByUsernameQuery("SELECT u.username, a.authority FROM authorities a " +
                        "INNER JOIN user_authorities ua ON a.id=ua.authority_id " +
                        "INNER JOIN users u ON ua.user_id=u.id " +
                        "WHERE u.username=?");
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        //configura paths publicos
        http.authorizeHttpRequests(
                (requests)->{
                    requests.requestMatchers("/", "/login").permitAll();
                    requests.requestMatchers("/helloAdmin", "/signup", "/create").hasAuthority("ROLE_ADMIN");
                    requests.requestMatchers("/helloUser", "/lista").hasAnyAuthority("ROLE_USER");
                    requests.requestMatchers(
                            "/css/**", "/js/**", "/image/**","/img/**", "/error/**", "/images/**", "imagenes/**", "/docs/**").permitAll();
                    requests.anyRequest().permitAll();
                }
        );

        //configura pagina de login
        http.formLogin(
                (login)->{
                    login.loginPage("/login").permitAll()
                            .successHandler(successHandler)
                            /*.defaultSuccessUrl("/homepage", true)*/;
                }
        );
        http.exceptionHandling().accessDeniedPage("/errors/403");
        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return  new BCryptPasswordEncoder();
    }

}
