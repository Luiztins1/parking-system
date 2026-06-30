package com.github.Luiztins1.parking_system.service;

import com.github.Luiztins1.parking_system.controller.dto.UserAuthDTO;
import com.github.Luiztins1.parking_system.model.entity.UserAuth;
import com.github.Luiztins1.parking_system.model.mapper.UserAuthMapper;
import com.github.Luiztins1.parking_system.repository.UserAuthRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserAuthService {

    private final UserAuthRepository userAuthRepository;

    @Transactional
    public UserAuth registerUserAuth(UserAuthDTO userAuthDTO){
        var user = UserAuthMapper.toEntity(userAuthDTO);
        var password = user.getPassword();
        //user.setPassword(passwordEncoder.encode(password));

        return userAuthRepository.save(user);
    }

    public List<UserAuth> findAll(){
        return userAuthRepository.findAll();
    }

    public Optional<UserAuth> updateUserAuth(UUID id, UserAuthDTO userAuthDTO){
        return userAuthRepository.findById(id)
                .map(userAuth -> {
                    userAuth.setPassword(userAuthDTO.password());

                    if(userAuth.getId() == null) throw new UsernameNotFoundException("Usuário não encontado.");

                    return userAuthRepository.save(userAuth);
                });

    }

   /* public void cancelUserAuth(UUID id){
        authValidator.validateSource(id);
        userAuthRepository.deleteById(id);
    }

    public Optional<UserAuth> findByid(UUID id){
        return Optional.of(authValidator.validateSource(id));
    }

    public Optional<UserAuth> findByLogin(String login){
        return Optional.of(authValidator.validateFindByLogin(login));
    }*/
}
