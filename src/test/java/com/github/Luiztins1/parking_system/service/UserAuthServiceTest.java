package com.github.Luiztins1.parking_system.service;

import com.github.Luiztins1.parking_system.controller.dto.UserAuthDTO;
import com.github.Luiztins1.parking_system.model.entity.UserAuth;
import com.github.Luiztins1.parking_system.model.mapper.UserAuthMapper;
import com.github.Luiztins1.parking_system.repository.UserAuthRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class UserAuthServiceTest {

    @InjectMocks
    UserAuthService userAuthService;

    @Mock
    UserAuthRepository userAuthRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    UserAuthDTO dto;
    UserAuth userAuthInit;

    @BeforeEach
    void setUp(){
        userAuthRepository.deleteAll();
        dto = new UserAuthDTO(UUID.randomUUID(), "admin_test", "admin123", List.of("ADMIN"));
        userAuthInit = UserAuthMapper.toEntity(dto);
    }

    @Test
    void shouldRegisterUserAuth(){

        Mockito.when(passwordEncoder.encode("admin123"))
                .thenReturn("passwordEncoder");

        //Devolve um novo objeto com o ID preenchido.
        Mockito.when(userAuthRepository.save(Mockito.any(UserAuth.class)))
                .thenAnswer(invocation ->{
                   UserAuth saved = invocation.getArgument(0);
                   saved.setId(UUID.randomUUID());

                   return saved;
                });

        var result = userAuthService.registerUserAuth(dto);

        assertThat(result).isNotNull();
        assertThat(result.getLogin()).isEqualTo("admin_test");
        assertThat(result.getPassword()).isEqualTo("passwordEncoder");
        assertThat(result.getRoles()).contains("ADMIN");

        Mockito.verify(userAuthRepository, Mockito.times(1))
                .save(Mockito.any(UserAuth.class));
    }

    @Test
    void shouldFindAll(){
        Mockito.when(userAuthRepository.findAll())
                .thenReturn(List.of(userAuthInit));

       var result = userAuthService.findAll();

        assertThat(result).isNotEmpty();
        assertThat(result).contains(userAuthInit);

        Mockito.verify(userAuthRepository, Mockito.times(1))
                .findAll();
    }

    @Test
    void shouldUpdateUserAuth(){

        Mockito.when(userAuthRepository.findById(userAuthInit.getId()))
                        .thenReturn(Optional.of(userAuthInit));

        Mockito.when(userAuthRepository.save(Mockito.any(UserAuth.class)))
                .thenAnswer(invocation ->{
                   UserAuth saved = invocation.getArgument(0);
                   saved.setId(UUID.randomUUID());
                   return saved;
                });


        Optional<UserAuth> updated = userAuthService.updateUserAuth(userAuthInit.getId(), dto);

        assertThat(updated).isPresent();
        assertThat(updated.get().getLogin()).isEqualTo(dto.login());
        assertThat(updated.get().getPassword()).isEqualTo(dto.password());

        Mockito.verify(userAuthRepository, Mockito.times(1))
                .save(Mockito.any(UserAuth.class));

    }

    @Test
    void shouldCancelUserAuth(){
        Mockito.when(userAuthRepository.findByLogin(userAuthInit.getLogin()))
                        .thenReturn(userAuthInit);

        Mockito.doNothing().when(userAuthRepository)
                .deleteByLogin(userAuthInit.getLogin());

        userAuthService.cancelUserAuth(userAuthInit.getLogin());

        Mockito.verify(userAuthRepository, Mockito.times(1))
                .deleteByLogin(userAuthInit.getLogin());
    }
}
