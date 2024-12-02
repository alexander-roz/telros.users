package com.telros.users.security;

import com.telros.users.data.entities.UserEntity;
import com.telros.users.data.entities.UserEntityRole;
import com.telros.users.services.UserService;
import com.telros.users.services.impl.UserServiceImpl;
import com.telros.users.views.LoginView;
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.ArrayList;
import java.util.List;


@EnableWebSecurity
@Configuration
public class SecurityConfiguration
        extends VaadinWebSecurity {
    private final UserService userService;

    public SecurityConfiguration(UserService userService) {
        this.userService = userService;

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .formLogin(new Customizer<FormLoginConfigurer<HttpSecurity>>() {
                    @Override
                    public void customize(FormLoginConfigurer<HttpSecurity> httpSecurityFormLoginConfigurer) {
                        httpSecurityFormLoginConfigurer.loginPage("/login");
                        httpSecurityFormLoginConfigurer.defaultSuccessUrl("/users", true);
                        httpSecurityFormLoginConfigurer.failureForwardUrl("/login");
                    }
                })
                .authorizeHttpRequests(auth ->
                auth
                        .requestMatchers(
                                new AntPathRequestMatcher("/login")).permitAll()
                        .requestMatchers(
                                new AntPathRequestMatcher("/users")).hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                        .requestMatchers(
                                new AntPathRequestMatcher("/newUser")).hasAuthority("ROLE_ADMIN")
                );

        super.configure(http);
        setLoginView(http, LoginView.class);
   }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // Customize your WebSecurity configuration.
        super.configure(web);
    }
    @Bean
    public AuthenticationManager authenticationManager (AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    @Bean
    public AuthenticationProvider authenticationProvider(UserServiceImpl userService){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        return authProvider;
    }

    @Bean
    public UserDetailsManager userDetailsManager() {
        UserEntity admin = new UserEntity();
        admin.setLogin("admin");
        admin.setPassword("{noop}pass");
        admin.setRole(UserEntityRole.ROLE_ADMIN);
        admin.setName("Admin");
        admin.setSurname("Admin");
        userService.addUser(admin);

        UserEntity user = new UserEntity();
        user.setLogin("user");
        user.setPassword("{noop}user");
        user.setRole(UserEntityRole.ROLE_USER);
        user.setName("User");
        user.setSurname("User");
        userService.addUser(user);

        return new InMemoryUserDetailsManager(admin, user);
    }

}