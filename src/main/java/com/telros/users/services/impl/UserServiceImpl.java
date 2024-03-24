package com.telros.users.services.impl;

import com.telros.users.dto.Request;
import com.telros.users.data.entities.UserEntity;
import com.telros.users.data.repositories.UserEntityRepository;
import com.telros.users.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserEntityRepository userEntityRepository;

    public UserServiceImpl(UserEntityRepository userEntityRepository) {
        this.userEntityRepository = userEntityRepository;
    }

    @Override
    public Long addUser(UserEntity user) {
        userEntityRepository.save(user);
        return user.getId();
    }

    @Override
    public boolean checkTheLogin(UserEntity addingUser){
        boolean found = false;
        for (UserEntity user:userEntityRepository.findAll()){
            if (user.getLogin().equalsIgnoreCase(addingUser.getLogin())){
                found = true;
            }
        }
        return found;
    }

    @Override
    public UserEntity findUserByLogin(String login){
        UserEntity userEntity = new UserEntity();
        for (UserEntity user:userEntityRepository.findAll()){
            if (user.getLogin().equalsIgnoreCase(login)){
                userEntity = user;
            }
        }
        return userEntity;
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
    public Request deleteAllUsers() {
        userEntityRepository.deleteAll();
        if (userEntityRepository.findAll().isEmpty()) {
            return new Request(true);
        } else {
            return new Request(false, "Users were not deleted");
        }
    }

    @Override
    public List<UserEntity> findAllUsers() {
        return userEntityRepository.findAll();
    }

    @Override
    public Optional<UserEntity> getUserByID(long id) {
        return userEntityRepository.findById(id);
    }

    @Override
    public UserEntity findUserByName(String name) {
        UserEntity userEntity = null;
        for(UserEntity user:userEntityRepository.findAll()){
            if(user.getName().equalsIgnoreCase(name)){
                userEntity = user;
            }
        }
        return userEntity;
    }

    public Page<UserEntity> list(Pageable pageable) {
        return userEntityRepository.findAll(pageable);
    }

    public Page<UserEntity> list(Pageable pageable, Specification<UserEntity> filter) {
        return userEntityRepository.findAll(filter, pageable);
    }

    @Override
    public boolean checkTheUser(String name) {
        boolean found = false;
        for (UserEntity user:userEntityRepository.findAll()){
            if (user.getName().equalsIgnoreCase(name)){
                found = true;
            }
        }
        return found;
    }
}
