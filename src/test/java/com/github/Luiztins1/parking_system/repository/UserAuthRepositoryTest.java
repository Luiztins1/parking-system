package com.github.Luiztins1.parking_system.repository;

import com.github.Luiztins1.parking_system.model.entity.UserAuth;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
public class UserAuthRepositoryTest {

    @Autowired
    UserAuthRepository userAuthRepository;

    UserAuth userAuthInit;

    @BeforeEach
    void setUp() {
        userAuthRepository.deleteAll();
        userAuthInit = new UserAuth();
        userAuthInit.setLogin("admin");
        userAuthInit.setPassword("admin123");
        userAuthInit.setRoles(List.of("ADMIN"));
        userAuthRepository.save(userAuthInit);
    }

    @Test
    void shouldSaveUserAuth(){
        assertNotNull(userAuthInit.getId());
        assertEquals("admin", userAuthInit.getLogin());
        assertEquals("admin123", userAuthInit.getPassword());
        assertThat(userAuthInit.getRoles()).contains("ADMIN");
    }

    @Test
    void shouldFindAll(){
        List<UserAuth> userFindAll = userAuthRepository.findAll();

        assertThat(userFindAll).isNotEmpty();
        assertThat(userFindAll.contains(userAuthInit)).isTrue();
    }

    @Test
    void shouldUpdateUserAuth(){
        var userUpdate = userAuthInit;

        userUpdate.setLogin("admin_update");
        userUpdate.setPassword("admin_update_123");
        userUpdate.setRoles(new ArrayList<>(List.of("ADMIN_MASTER")));

        userAuthRepository.save(userUpdate);

        UserAuth userUpdated = userAuthRepository.findById(userUpdate.getId())
                .orElseThrow(() -> new AssertionError("User not found."));

        assertThat(userUpdated.getLogin()).isNotEmpty();
        assertNotNull(userUpdated.getLogin());

        assertThat(userUpdated.getPassword()).isNotEmpty();
        assertNotNull(userUpdated.getPassword());

        assertThat(userUpdated.getRoles()).isNotEmpty();
        assertNotNull(userUpdated.getRoles());
    }

    @Test
    void shouldDeleteUserAuth(){
        userAuthRepository.delete(userAuthInit);

        Optional<UserAuth> userDelete = userAuthRepository.findById(userAuthInit.getId());

        assertNotNull(userAuthInit.getId());
        assertThat(userDelete).isEmpty();
    }

    @Test
    void shouldDeleteUserLogin(){
        assertThat(userAuthRepository.existsByLogin(userAuthInit.getLogin())).isTrue();

        userAuthRepository.deleteByLogin(userAuthInit.getLogin());

        UserAuth userDeleteLogin = userAuthRepository.findByLogin(userAuthInit.getLogin());

        assertThat(userDeleteLogin).isNull();
        assertThat(userAuthRepository.existsByLogin(userAuthInit.getLogin())).isFalse();
    }

    @Test
    void shouldFindByLogin(){
        var userLogin = userAuthRepository.findByLogin(userAuthInit.getLogin());

        assertNotNull(userLogin.getId());
        assertEquals(userAuthInit.getLogin(), userLogin.getLogin());
    }

    @Test
    void shouldExistLoginTrue(){
        boolean existUser = userAuthRepository.existsByLogin(userAuthInit.getLogin());

        assertNotNull(userAuthInit.getLogin());
        assertTrue(existUser);
    }

    @Test
    void shouldExistLoginFalse(){
        boolean existUser = userAuthRepository.existsByLogin("test_false");

        assertNotNull(userAuthInit.getLogin());
        assertFalse(existUser);
    }
}
