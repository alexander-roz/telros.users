package com.telros.users.services.impl;

import com.telros.users.dto.Request;
import com.telros.users.data.entities.UserEntity;
import com.telros.users.data.repositories.UserEntityRepository;
import com.telros.users.services.UserService;
import com.vaadin.flow.component.notification.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserEntityRepository userEntityRepository;

    public UserServiceImpl(UserEntityRepository userEntityRepository) {
        this.userEntityRepository = userEntityRepository;
    }

    @Override
    public void addUser(UserEntity user) {
        userEntityRepository.save(user);
    }

    @Override
    public boolean checkTheLogin(UserEntity addingUser){
        boolean found = false;
        for (UserEntity user:userEntityRepository.findAll()){
            if (user.getLogin().equalsIgnoreCase(addingUser.getLogin())) {
                found = true;
                break;
            }
        }
        return found;
    }

    @Override
    public Request deleteUser(UserEntity user) {
        if (userEntityRepository.existsById(user.getId())) {
            userEntityRepository.delete(user);
            return new Request(true);
        } else {
            return new Request(false, "User was not found");
        }
    }

    @Override
    public List<UserEntity> findAllUsers() {
        return userEntityRepository.findAll();
    }

    public Page<UserEntity> list(Pageable pageable) {
        return userEntityRepository.findAll(pageable);
    }

    @Override
    public UserEntity findUserByLogin(String login) {
        UserEntity userEntity = null;
        for (UserEntity user:userEntityRepository.findAll()){
            if (user.getLogin().equalsIgnoreCase(login)) {
                userEntity = user;
                break;
            }
        }
        return userEntity;
    }

    public Page<UserEntity> list(Pageable pageable, Specification<UserEntity> filter) {
        return userEntityRepository.findAll(filter, pageable);
    }

    @Override
    public boolean checkTheUser(String name) {
        boolean found = false;
        for (UserEntity user:userEntityRepository.findAll()){
            if (user.getName().equalsIgnoreCase(name)) {
                found = true;
                break;
            }
        }
        return found;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("> method loadUserByUsername, class UserServiceImpl started with user: " + username);
        UserEntity userEntity = userEntityRepository.findUserEntityByLogin(username);
        UserDetails loadedUser;
        if(userEntity != null){
            loadedUser = userEntity;

            System.out.println("> method loadUserByUsername found: \n"
                    + loadedUser.getUsername() + "\n"
                    + loadedUser.getPassword() + "\n"
                    + loadedUser.getAuthorities());
        }
        else{
            Notification.show("There is no user with login " + username + " found");
            loadedUser = null;
        }
        return (UserDetails) loadedUser;
    }
}
