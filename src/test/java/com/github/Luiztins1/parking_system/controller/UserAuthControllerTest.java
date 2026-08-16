package com.github.Luiztins1.parking_system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.Luiztins1.parking_system.config.SecurityConfiguration;
import com.github.Luiztins1.parking_system.controller.dto.UserAuthDTO;
import com.github.Luiztins1.parking_system.controller.rest.UserAuthController;
import com.github.Luiztins1.parking_system.exceptions.NotFoundException;
import com.github.Luiztins1.parking_system.model.entity.UserAuth;
import com.github.Luiztins1.parking_system.model.mapper.UserAuthMapper;
import com.github.Luiztins1.parking_system.service.UserAuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(UserAuthController.class)
@Import(SecurityConfiguration.class)
@ActiveProfiles("test")
public class UserAuthControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserAuthService userAuthService;

    UserAuthDTO expected;
    UserAuth userAuthInit;

    @BeforeEach
    void setUp(){
        userAuthInit = new UserAuth(
                UUID.randomUUID(),
                "luiz",
                "123456789",
                List.of("MANAGER")
        );

        expected = UserAuthMapper.toDto(userAuthInit);
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void shouldRegisterUserAuth() throws Exception{
        when(userAuthService.registerUserAuth(Mockito.any(UserAuthDTO.class)))
                .thenAnswer(invocation ->{
                   UserAuthDTO dto = invocation.getArgument(0);
                   UserAuth saved = UserAuthMapper.toEntity(dto);
                   saved.setId(UUID.randomUUID());
                   return saved;
                });

        mvc.perform(post("/api/v1/users")
                        .content(objectMapper.writeValueAsString(expected))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.login").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$.login").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.login").value(expected.login()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").value(expected.password()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.roles").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.roles").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.roles.length()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.roles[0]").value("MANAGER"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnForbiddenWhenUserIsNotManager() throws Exception{
        mvc.perform(post("/api/v1/users")
                .content(objectMapper.writeValueAsString(expected))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void shouldReturnConflictWhenUserAlreadyExists() throws Exception{
        when(userAuthService.registerUserAuth(Mockito.any(UserAuthDTO.class)))
                .thenThrow(new IllegalArgumentException("Usuário já cadastrado."));

        mvc.perform(post("/api/v1/users")
                        .with(csrf())
                .content(objectMapper.writeValueAsString(expected))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void shouldReturnBadRequestWhenJsonWasError() throws Exception{
        when(userAuthService.registerUserAuth(Mockito.any(UserAuthDTO.class)))
                .thenThrow(new HttpMessageNotReadableException("Erro de leitura de JSON ou valor inválido."));

        mvc.perform(post("/api/v1/users")
                        .with(csrf())
                .content(objectMapper.writeValueAsString(expected))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void shouldFindAll() throws Exception{
        when(userAuthService.findAll())
                .thenReturn(List.of(userAuthInit));

        mvc.perform(get("/api/v1/users")
                        .content(objectMapper.writeValueAsString(expected))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void shouldReturnNoContentWhenListIsEmptyFindAll() throws Exception{
        when(userAuthService.findAll())
                .thenReturn(List.of());

        mvc.perform(get("/api/v1/users")
                        .content(objectMapper.writeValueAsString(expected))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void shouldUpdateUserAuth() throws Exception{
        UUID id = userAuthInit.getId();

        UserAuthDTO dto = new UserAuthDTO(
                UUID.randomUUID(),
                "Marcus",
                "143456789",
                List.of("MANAGER"));

        when(userAuthService.updateUserAuth(Mockito.eq(id), Mockito.any(UserAuthDTO.class)))
                .thenReturn(Optional.of(userAuthInit));

        mvc.perform(put("/api/v1/users/{id}", id)
                        .with(csrf())
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.login").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$.login").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.roles").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.roles").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.roles.length()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.roles[0]").value("MANAGER"));;
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnForbiddenWhenUserIsNotManagerForUpdate() throws Exception{
        UUID id = userAuthInit.getId();

        UserAuthDTO dto = new UserAuthDTO(
                UUID.randomUUID(),
                "Marcus",
                "143456789",
                List.of("MANAGER"));

        when(userAuthService.updateUserAuth(Mockito.eq(id), Mockito.any(UserAuthDTO.class)))
                .thenReturn(Optional.of(userAuthInit));

        mvc.perform(put("/api/v1/users/{id}", id)
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void shouldReturnConflictWhenUserNotExistsUpdate() throws Exception{
        UUID id = UUID.randomUUID();

        UserAuthDTO dto = new UserAuthDTO(
                UUID.randomUUID(),
                "Marcus",
                "143456789",
                List.of("MANAGER"));


        when(userAuthService.updateUserAuth(Mockito.eq(id), Mockito.any(UserAuthDTO.class)))
                .thenReturn(Optional.empty());

        mvc.perform(put("/api/v1/users/{id}", id)
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void shouldReturnBadRequestWhenJsonWasErrorUpdate() throws Exception{
        UUID id = userAuthInit.getId();

        UserAuthDTO dto = new UserAuthDTO(
                UUID.randomUUID(),
                " ",
                "143456789",
                List.of("MANAGER"));

        when(userAuthService.updateUserAuth(Mockito.eq(id), Mockito.any(UserAuthDTO.class)))
                .thenThrow(new HttpMessageNotReadableException("Erro de leitura de JSON ou valor inválido."));

        mvc.perform(put("/api/v1/users/{id}", id)
                        .with(csrf())
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void shouldCancelUserAuth() throws Exception{
        String login = userAuthInit.getLogin();

        doNothing().when(userAuthService).cancelUserAuth(login);

        mvc.perform(delete("/api/v1/users/{login}", login)
                        .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnForbiddenWhenUserIsNotManagerForCancel() throws Exception{
        String login = userAuthInit.getLogin();

        doNothing().when(userAuthService).cancelUserAuth(login);

        mvc.perform(delete("/api/v1/users/{login}", login)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void shouldReturnConflictWhenUserNotExistsCancel() throws Exception{
        String login = expected.login();

        doThrow(new NotFoundException("Usuário não encontrado.")).
                when(userAuthService).cancelUserAuth(login);

        mvc.perform(delete("/api/v1/users/{login}", login)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void shouldReturnBadRequestWhenJsonWithErrorCancel() throws Exception{
        String login = " ";

        doThrow(new HttpMessageNotReadableException("Erro de leitura de JSON ou valor inválido."))
                .when(userAuthService).cancelUserAuth(login);

        mvc.perform(delete("/api/v1/users/{login}", login)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void shouldFindById() throws Exception{
        UUID id = userAuthInit.getId();

        when(userAuthService.findById(Mockito.eq(id)))
                .thenReturn(Optional.of(userAuthInit));

        mvc.perform(get("/api/v1/users/{id}/find-id", id)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id.toString().trim()));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldFindByIdWhenUserIsNotManager() throws Exception{
        UUID id = userAuthInit.getId();

        when(userAuthService.registerUserAuth(expected))
                .thenAnswer(invocation ->{
                   UserAuthDTO dto = invocation.getArgument(0);
                   UserAuth saved = UserAuthMapper.toEntity(dto);
                   saved.setId(id);
                   return saved;
                });

        when(userAuthService.findById(Mockito.any(UUID.class)))
                .thenReturn(Optional.of(UserAuthMapper.toEntity(expected)));

        mvc.perform(get("/api/v1/users/{id}/find-id", id)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void shouldFindByIdWhenUserIsNotExists() throws Exception{
        UUID id = userAuthInit.getId();

        when(userAuthService.findById(Mockito.eq(id)))
                .thenReturn(Optional.empty());

        mvc.perform(get("/api/v1/users/{id}/find-id", id)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void shouldFindByIdWhenBadRequestError() throws Exception{
        mvc.perform(get("/api/v1/users/{id}/find-id", "")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void shouldFindByLogin() throws Exception{
        String login = userAuthInit.getLogin();
        when(userAuthService.findByLogin(Mockito.eq(login)))
                .thenReturn(Optional.of(userAuthInit));

        mvc.perform(get("/api/v1/users/{login}", login)
                .with(csrf())
                .content(objectMapper.writeValueAsString(expected))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.login").value(login))
                .andExpect(MockMvcResultMatchers.jsonPath("$.login").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$.login").isNotEmpty());
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldFindByLoginReturnForbiddenWhenUserIsNotManager() throws Exception{
        UserAuth loginForbidden = userAuthInit;
        loginForbidden.setRoles(List.of("USER"));

        when(userAuthService.findByLogin(Mockito.eq(loginForbidden.getLogin())))
                .thenReturn(Optional.of(userAuthInit));

        mvc.perform(get("/api/v1/users/{login}", loginForbidden.getLogin())
                .content(objectMapper.writeValueAsString(expected))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void shouldFindByLoginReturnNotFoundWhenUserNotExists() throws Exception{
        String login = userAuthInit.getLogin();

        doThrow(new NotFoundException("Usuário não encontrado.")).when(userAuthService).findByLogin(login);

        mvc.perform(get("/api/v1/users/{login}", login)
                .with(csrf())
                .content(objectMapper.writeValueAsString(expected))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void shouldFindByLoginReturnBadRequestWhenJsonWithError() throws Exception{
        String login = " ";

        doThrow(new HttpMessageNotReadableException("Erro de leitura de JSON ou valor inválido."))
                .when(userAuthService).findByLogin(login);

        mvc.perform(get("/api/v1/users/{login}", login)
                .with(csrf())
                .content(objectMapper.writeValueAsString(expected))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}