package com.github.Luiztins1.parking_system.service;

import com.github.Luiztins1.parking_system.controller.dto.UserAuthDTO;
import com.github.Luiztins1.parking_system.exceptions.NotFoundException;
import com.github.Luiztins1.parking_system.model.entity.UserAuth;
import com.github.Luiztins1.parking_system.model.mapper.UserAuthMapper;
import com.github.Luiztins1.parking_system.repository.UserAuthRepository;
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
        UserAuth userAuth = userAuthRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));

        userAuth.setLogin(userAuthDTO.login());
        userAuth.setPassword(userAuthDTO.password());

        return Optional.of(userAuth);
    }

    @Transactional
   public void cancelUserAuth(String login){
        UserAuth userAuth = Optional.of(userAuthRepository.findByLogin(login))
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado."));
        userAuthRepository.deleteByLogin(userAuth.getLogin());
    }

    public Optional<UserAuth> findById(UUID id){
        return userAuthRepository.findById(id);
    }

    public Optional<UserAuth> findByLogin(String login){
        return Optional.of(userAuthRepository.findByLogin(login));
    }
}
