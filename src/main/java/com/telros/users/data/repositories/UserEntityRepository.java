package com.telros.users.data.repositories;

import com.telros.users.data.entities.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Long>{

    UserEntity findUserEntityByLogin(String username);

    Page<UserEntity> findAll(Specification<UserEntity> filter, Pageable pageable);
}
