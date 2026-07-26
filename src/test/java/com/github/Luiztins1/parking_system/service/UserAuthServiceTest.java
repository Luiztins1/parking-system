package com.github.Luiztins1.parking_system.service;

import com.github.Luiztins1.parking_system.model.entity.UserAuth;
import com.github.Luiztins1.parking_system.model.mapper.UserAuthMapper;
import com.github.Luiztins1.parking_system.repository.UserAuthRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;


@SpringBootTest
@ActiveProfiles("test")
public class UserAuthServiceTest {

    @Autowired
    UserAuthService userAuthService;

    @Autowired
    UserAuthRepository userAuthRepository;

    UserAuth userAuthInit;

    @BeforeEach
    void setUp(){
        userAuthRepository.deleteAll();
        userAuthInit = new UserAuth();
        userAuthInit.setLogin("admin_test");
        userAuthInit.setPassword("admin123");
        userAuthInit.setRoles(List.of("ADMIN"));
        userAuthRepository.save(userAuthInit);
    }

    @Test
    void shouldRegisterUserAuth(){
        var registerUser = userAuthService
                .registerUserAuth(UserAuthMapper.toDto(userAuthInit));

        assertNotNull(registerUser.getId());
        assertThat(registerUser.getLogin()).isNotEmpty();
        assertThat(registerUser.getPassword()).isNotEmpty();
        assertThat(registerUser.getRoles()).isNotEmpty();
    }

    @Test
    void shouldFindAll(){
        List<UserAuth> listUserAuth = userAuthService.findAll();

        assertThat(listUserAuth).isNotEmpty();
        assertThat(listUserAuth.contains(userAuthInit)).isTrue();
    }

    @Test
    void shouldUpdateUserAuth(){
        Optional<UserAuth> updateUserAuth = userAuthService.
                updateUserAuth(userAuthInit.getId(), UserAuthMapper.toDto(userAuthInit));

        var updated = updateUserAuth.get();

        assertThat(updated.getLogin()).isNotEmpty();
        assertThat(updated.getPassword()).isNotEmpty();
    }

    @Test
    void shouldCancelUserAuth(){
        var cancel = userAuthInit;
        userAuthService.cancelUserAuth(cancel.getLogin());

        var canceled = userAuthRepository.findByLogin(cancel.getLogin());

        assertThat(canceled).isNull();
    }
}
