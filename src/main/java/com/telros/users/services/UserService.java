package com.telros.users.services;

import com.telros.users.data.entities.UserEntity;
import com.telros.users.dto.Request;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface UserService {
    void addUser(UserEntity user);

    boolean checkTheLogin(UserEntity user);

    List<UserEntity> findAllUsers();

    boolean checkTheUser(String name);

    public Page<UserEntity> list(Pageable pageable, Specification<UserEntity> filter);

    public Page<UserEntity> list(Pageable pageable);

    UserEntity findUserByLogin(String login);

    public Request deleteUser(UserEntity user);
}
