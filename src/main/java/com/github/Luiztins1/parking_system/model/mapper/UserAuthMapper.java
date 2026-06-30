package com.github.Luiztins1.parking_system.model.mapper;

import com.github.Luiztins1.parking_system.controller.dto.UserAuthDTO;
import com.github.Luiztins1.parking_system.model.entity.UserAuth;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserAuthMapper {

    public static UserAuthDTO toDto(UserAuth userAuth){
        if(userAuth == null ) return null;

        return new UserAuthDTO(
                userAuth.getId(),
                userAuth.getLogin(),
                userAuth.getPassword(),
                userAuth.getRoles()
        );
    }

    public static UserAuth toEntity(UserAuthDTO userAuthDTO){
        if(userAuthDTO == null) return null;

        UserAuth userAuth = new UserAuth();

        userAuth.setId(userAuthDTO.id());
        userAuth.setLogin(userAuthDTO.login());
        userAuth.setPassword(userAuthDTO.password());
        userAuth.setRoles(userAuthDTO.roles());

        return userAuth;
    }
}
