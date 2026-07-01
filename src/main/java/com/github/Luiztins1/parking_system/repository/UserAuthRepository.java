package com.github.Luiztins1.parking_system.repository;

import com.github.Luiztins1.parking_system.model.entity.UserAuth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserAuthRepository extends JpaRepository<UserAuth, UUID> {
    UserAuth findByLogin(String login);
    boolean existsByLogin(String login);
    void deleteByLogin(String login);
}
