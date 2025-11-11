package com.example.my_community.user.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String email;
    private String password; // 예: "{noop}pass1" (추후 BCrypt로 교체)

    @Enumerated(EnumType.STRING)
    private Role role;


    public User() {}

    public User(String email, String password, Role role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public void changeNickname(String nickname) {
        this.email = nickname;
    }
}
