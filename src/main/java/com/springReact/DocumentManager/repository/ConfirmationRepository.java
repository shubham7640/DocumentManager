package com.springReact.DocumentManager.repository;

import com.springReact.DocumentManager.entity.ConfirmationEntity;

import com.springReact.DocumentManager.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfirmationRepository extends JpaRepository<ConfirmationEntity,Long> {
    Optional<ConfirmationEntity> findBykey(String key);
    Optional<ConfirmationEntity> findByUserEntity(UserEntity userEntity);
}


