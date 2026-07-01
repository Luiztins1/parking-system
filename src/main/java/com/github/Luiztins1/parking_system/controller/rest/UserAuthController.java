package com.github.Luiztins1.parking_system.controller.rest;

import com.github.Luiztins1.parking_system.controller.dto.UserAuthDTO;
import com.github.Luiztins1.parking_system.model.entity.UserAuth;
import com.github.Luiztins1.parking_system.model.mapper.UserAuthMapper;
import com.github.Luiztins1.parking_system.service.UserAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserAuthController {

    private final UserAuthService userAuthService;

    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<UserAuthDTO> registerUserAuth(@RequestBody UserAuthDTO userAuthDTO){
        var userAuth = UserAuthMapper.toEntity(userAuthDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(userAuth.getId())
                .toUri();

        return ResponseEntity.created(location).body(UserAuthMapper.toDto(userAuth));
    }

    @GetMapping
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<UserAuthDTO>> findAll(){
        List<UserAuthDTO> userAuthList = userAuthService.findAll()
                .stream()
                .map(UserAuthMapper::toDto)
                .toList();

        if(userAuthList.isEmpty()) return ResponseEntity.noContent().build();

        return ResponseEntity.ok(userAuthList);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<UserAuthDTO> updateUserAuth(@PathVariable UUID id, @RequestBody UserAuthDTO userAuthDTO){
        Optional<UserAuth> userAuthDTOOptional = userAuthService.updateUserAuth(id, userAuthDTO);

        if(userAuthDTOOptional.isPresent()) return ResponseEntity.ok().build();

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{login}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Void> cancelUserAuth(@PathVariable String login){
        userAuthService.cancelUserAuth(login);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<UserAuthDTO> findById(@PathVariable UUID id){
        return userAuthService.findByid(id)
                .map(UserAuthMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{login}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<UserAuthDTO> findByLogin(String login){
        Optional<UserAuth> userAuthOptional = userAuthService.findByLogin(login);

        if(userAuthOptional.isPresent()) return ResponseEntity.ok().build();

        return ResponseEntity.notFound().build();
    }


}
