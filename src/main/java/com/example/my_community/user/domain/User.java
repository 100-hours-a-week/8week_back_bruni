package com.example.my_community.user.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;
    private String password; // 예: "{noop}pass1" (추후 BCrypt로 교체)
    private String nickname;

    // 프로필 이미지 변경
    @Setter
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] profileImage;

    @Enumerated(EnumType.STRING)
    private Role role;



    protected User() {} // JPA용 기본 생성자

    public User(String email, String password, String nickname, byte[] profileImage, Role role) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.role = role;
    }

    // 닉네임 변경
    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

    // 비밀번호 변경 (나중에 BCrypt로 바꾸면 여기서 인코딩된 값만 받으면 됨)
    public void changePassword(String password) {
        this.password = password;
    }
}
