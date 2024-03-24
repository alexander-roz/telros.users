package com.telros.users.services;

import com.telros.users.dto.Request;
import com.telros.users.data.entities.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Long addUser(UserEntity user);

    boolean checkTheLogin(UserEntity user);

    UserEntity findUserByLogin(String login);

    Request deleteUser(UserEntity user);
    List<UserEntity> findAllUsers();
    Optional<UserEntity> getUserByID(long id);
    Request deleteAllUsers();
    UserEntity findUserByName(String name);

    boolean checkTheUser(String name);

    public Page<UserEntity> list(Pageable pageable, Specification<UserEntity> filter);

    public Page<UserEntity> list(Pageable pageable);
}
