package com.example.my_community.user.service;

import com.example.my_community.common.exception.NotFoundException;
import com.example.my_community.user.domain.Role;
import com.example.my_community.user.domain.User;
import com.example.my_community.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public User create(String email, String password, String nickname, byte[] profileImage) {
        Role defaultRole = Role.USER; // 도메인 규칙 : 회원 가입은 항상 일반 유저
        User user = new User(email, password, nickname, profileImage, defaultRole);
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(()
                -> new IllegalArgumentException("존재하지 않는 사용자입니다. id=" + id));
    }

    // 내 정보 조회
    @Transactional(readOnly = true)
    public User getMe(Long userId) {
        return findById(userId);
    }

    // 프로필 변경
    public User updateProfile(Long userId, String nickname, byte[] profileImage) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다. id=" + userId));

        // 닉네임 변경
        if (nickname != null && !nickname.isBlank()) {
            user.changeNickname(nickname);   // ★ 이 메서드 내부에서 this.nickname = nickname; 으로 되어 있는지 꼭 확인!
        }

        // 프로필 이미지 변경 (선택)
        if (profileImage != null) {
            user.setProfileImage(profileImage);
        }

        // ★ 변경 감지로도 되지만, 확실하게 save() 해두면 헷갈릴 일이 적음
        return userRepository.save(user);
    }

    // 비밀번호 변경
    public void changePassword(Long userId, String newPassword) {
        validatePassword(newPassword); // 프론트랑 동일한 규칙

        User user = findById(userId);

        // 아직 평문 저장 중이라 가정 (추후 BCrypt 적용하면 여기서 인코딩)
        user.changePassword(newPassword);
    }

    @Transactional
    public void deleteMe(Long userId) {
        userRepository.deleteById(userId);
    }

    /* 비밀번호 유효성 검사  */
    private void validatePassword(String pw) {
        if (pw == null || pw.length() < 8 || pw.length() > 20) {
            throw new IllegalArgumentException("비밀번호는 8자 이상, 20자 이하여야 합니다.");
        }

        boolean hasUpper = pw.chars().anyMatch(ch -> Character.isUpperCase(ch));
        boolean hasLower = pw.chars().anyMatch(ch -> Character.isLowerCase(ch));
        boolean hasDigit = pw.chars().anyMatch(ch -> Character.isDigit(ch));
        boolean hasSpecial = pw.chars().anyMatch(ch -> !Character.isLetterOrDigit(ch));

        if (!hasUpper || !hasLower || !hasDigit || !hasSpecial) {
            throw new IllegalArgumentException(
                    "비밀번호는 대문자, 소문자, 숫자, 특수문자를 각각 최소 1개 포함해야 합니다."
            );
        }
    }
}
