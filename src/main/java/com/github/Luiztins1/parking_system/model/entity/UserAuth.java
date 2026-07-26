package com.github.Luiztins1.parking_system.model.entity;

import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users_auths")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class UserAuth extends Auditable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "login", nullable = false, unique = true)
    private String login;

    @Column(name = "password", nullable = false)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "users_auths_roles", joinColumns = @JoinColumn(name = "user_auth_id"))
    @Column(name = "role")
    private List<String> roles;
}
