package com.telros.users.services;

import com.telros.users.data.entities.UserEntity;
import com.telros.users.data.entities.UserEntityRole;
import com.telros.users.views.LoginView;
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
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
        http.authorizeHttpRequests(auth -> auth.requestMatchers(new AntPathRequestMatcher("/public/**"))
                .permitAll());

        super.configure(http);
        setLoginView(http, LoginView.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // Customize your WebSecurity configuration.
        super.configure(web);
    }

    /**
     * Demo UserDetailsManager which only provides two hardcoded
     * in memory users and their roles.
     * NOTE: This shouldn't be used in real world applications.
     */
    @Bean
    public UserDetailsManager userDetailsService() {
        UserDetails user =
                User.withUsername("user")
                        .password("{noop}user")
                        .roles("USER")
                        .build();

        UserDetails admin =
                User.withUsername("admin")
                        .password("{noop}admin")
                        .roles("ADMIN")
                        .build();

        List<UserDetails> users = new ArrayList<>();
        users.add(admin);
        users.add(user);

        //Внесение в базу данных тестовых пользователей
        // с различными правами доступа для входа в приложение при первом запуске.
        UserEntity testUser = new UserEntity();
        testUser.setLogin("user");
        testUser.setPassword("user");
        testUser.setName("User");
        testUser.setSurname("User");
        testUser.setRole(UserEntityRole.USER);
        if(!userService.checkTheLogin(testUser)){
            userService.addUser(testUser);
        }
        UserEntity testAdmin = new UserEntity();
        testAdmin.setLogin("admin");
        testAdmin.setPassword("admin");
        testAdmin.setName("Admin");
        testAdmin.setSurname("Admin");
        testAdmin.setRole(UserEntityRole.ADMIN);
        if(!userService.checkTheLogin(testAdmin)){
            userService.addUser(testAdmin);
        }

        //Процедуру проверки внесенных данных в login форме со значениями БД реализовать не удалось

//        for(UserEntity userEntity: userService.findAllUsers()){
//            if(userEntity.getRole().equals(UserEntityRole.ADMINISTRATOR)){
//                UserDetails administrator = User
//                                .withUsername(userEntity.getLogin())
//                                    .password(userEntity.getPassword())
//                                    .roles("ADMIN")
//                                    .build();
//                users.add(administrator);
//            }
//            if(userEntity.getRole().equals(UserEntityRole.USER)){
//                UserDetails nextUser = User
//                                .withUsername(userEntity.getLogin())
//                                        .password(userEntity
//                                        .getPassword())
//                                        .roles("USER")
//                                        .build();
//                users.add(nextUser);
//            }
//        }

        return new InMemoryUserDetailsManager(users);
    }

}