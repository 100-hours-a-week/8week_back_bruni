package com.example.my_community.user.service;

import com.example.my_community.user.domain.Role;
import com.example.my_community.user.domain.User;
import com.example.my_community.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public User create(String nickname, String password, Role role) {
        User user = new User(nickname, password, role);
        return userRepository.save(user);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("user not found"));
    }

    public User getReferenceById(Long id) {
        return userRepository.getReferenceById(id);
    }

    @Transactional
    public User update(Long id, String nickname) {
        User user = findById(id);
        if (nickname != null) {
            user.changeNickname(nickname);
        }
        return user;
    }

    @Transactional
    public void delete(Long id) {
        // 프록시 반환 (접근 시 초기화)
        userRepository.delete(findById(id));
    }
}
