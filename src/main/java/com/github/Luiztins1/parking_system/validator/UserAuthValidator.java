package com.github.Luiztins1.parking_system.validator;

import com.github.Luiztins1.parking_system.exceptions.DuplicateException;
import com.github.Luiztins1.parking_system.exceptions.NotFoundException;
import com.github.Luiztins1.parking_system.model.entity.UserAuth;
import com.github.Luiztins1.parking_system.repository.UserAuthRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class UserAuthValidator {

    private final UserAuthRepository userAuthRepository;

    public UserAuth validateSource(UUID id){
        return userAuthRepository.findById(id).orElseThrow(() -> new NotFoundException("Usuário não encontrado."));
    }

    public void validateStudentDuplicate(UserAuth userAuth){
        if(duplicateStudent(userAuth)) throw new DuplicateException("Usuário já cadastrado no sistema.");
    }

    public UserAuth validateFindByLogin(String login){
        var user = userAuthRepository.findByLogin(login);
        validateSource(user.getId());
        return user;
    }

    private boolean duplicateStudent(UserAuth userAuth){
        return userAuthRepository.existsByLogin(userAuth.getLogin());
    }
}
