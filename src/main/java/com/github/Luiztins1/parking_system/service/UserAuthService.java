package com.github.Luiztins1.parking_system.service;

import com.github.Luiztins1.parking_system.controller.dto.UserAuthDTO;
import com.github.Luiztins1.parking_system.model.entity.UserAuth;
import com.github.Luiztins1.parking_system.model.mapper.UserAuthMapper;
import com.github.Luiztins1.parking_system.repository.UserAuthRepository;
import com.github.Luiztins1.parking_system.validator.UserAuthValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserAuthService {

    private final UserAuthRepository userAuthRepository;
    private final UserAuthValidator userAuthValidator;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserAuth registerUserAuth(UserAuthDTO userAuthDTO){
        var user = UserAuthMapper.toEntity(userAuthDTO);
        var password = user.getPassword();
        user.setPassword(passwordEncoder.encode(password));

        return userAuthRepository.save(user);
    }

    public List<UserAuth> findAll(){
        return userAuthRepository.findAll();
    }

    @Transactional
    public Optional<UserAuth> updateUserAuth(UUID id, UserAuthDTO userAuthDTO){
        return userAuthRepository.findById(id)
                .map(userAuth -> {
                    userAuth.setLogin(userAuthDTO.login());
                    userAuth.setPassword(userAuthDTO.password());

                    if(userAuth.getId() == null) throw new UsernameNotFoundException("Usuário não encontrado.");

                    return userAuthRepository.save(userAuth);
                });

    }

    @Transactional
   public void cancelUserAuth(String login){
        var userAuth = userAuthRepository.findByLogin(login);
        if(userAuth == null) throw new UsernameNotFoundException("Usuário não encnotrado.");
        userAuthRepository.deleteByLogin(userAuth.getLogin());
    }

    public Optional<UserAuth> findById(UUID id){
        return Optional.of(userAuthValidator.validateSource(id));
    }

    public Optional<UserAuth> findByLogin(String login){
        return Optional.of(userAuthValidator.validateFindByLogin(login));
    }
}
