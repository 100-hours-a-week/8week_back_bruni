package com.example.my_community.user.repository;

import com.example.my_community.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // 이메일로 유저 찾기
    Optional<User> findByEmail(String email);
    // 닉네임 중복 체크용
    boolean existsByNickname(String nickname);
}
