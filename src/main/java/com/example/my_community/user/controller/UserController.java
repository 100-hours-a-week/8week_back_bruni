package com.example.my_community.user.controller;

import com.example.my_community.auth.Auth;
import com.example.my_community.common.exception.FileInputException;
import com.example.my_community.common.exception.UnauthorizedException;
import com.example.my_community.user.domain.User;
import com.example.my_community.user.dto.UserResponse;
import com.example.my_community.user.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final Auth auth;

    // 회원 가입
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponse> signup(
            @RequestPart("email") String email,
            @RequestPart("password") String password,
            @RequestPart("nickname") String nickname,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage
            ) {

        // 프로필 이미지가 있으면 byte[]로 저장
        try {
            byte[] profileImageToByte = null;
            if (profileImage != null && !profileImage.isEmpty()) {
                profileImageToByte = profileImage.getBytes();
            }
            User saved = userService.create(email, password, nickname, profileImageToByte);
            UserResponse res = UserResponse.of(saved);
            return ResponseEntity.status(HttpStatus.CREATED).body(res);
        } catch (IOException e) {
            throw new FileInputException("프로필 이미지를 처리하는 중 오류가 발생했습니다.", e);
        }
    }

    @GetMapping(("/{id}"))
    public UserResponse findById(@PathVariable Long id) {
        return UserResponse.of(userService.findById(id));
    }


    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userService.deleteMe(id);
    }

    // 프로필 이미지 가져오기(현재 로그인한 사용자(세션 정보 가진))
    @GetMapping("/me/profile-image")
    public ResponseEntity<byte[]> getMyProfileImage() {
        // 1) 로그인 중인 유저 확인
        Long loginUserId = auth.requireUserId();

        // 2) 유저 조회
        User user = userService.findById(loginUserId);
        byte[] profileImage = user.getProfileImage();

        // 3) 프로필 이미지가 없는 경우
        if (profileImage == null || profileImage.length == 0) {
            return ResponseEntity.notFound().build();
        }

        // 4) 헤더에 Content-Type 설정 (지금은 일단 PNG으로 가정)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);

        return new ResponseEntity<>(profileImage, headers, HttpStatus.OK);
    }

    // authorId로 프로필 이미지 가져오는 API
    @GetMapping("/{authorId}/profile-image")
    public ResponseEntity<byte[]> getProfileImage(@PathVariable Long authorId) {
        // 1) 유저 조회
        User user = userService.findById(authorId);
        byte[] profileImage = user.getProfileImage();

        // 2) 프로필 이미지가 없는 경우
        if (profileImage == null || profileImage.length == 0) {
            return ResponseEntity.notFound().build();
        }

        // 3) 헤더에 Content-Type 설정 (지금은 일단 PNG으로 가정)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);

        return new ResponseEntity<>(profileImage, headers, HttpStatus.OK);
    }

    /* === 1) 내 정보 조회 === */
    @GetMapping("/me")
    public UserResponse getMe() {
        Long loginUserId = auth.requireUserId();
        if (loginUserId == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        User me = userService.getMe(loginUserId);
        return UserResponse.of(me);
    }

    /* === 2) 프로필 변경 === */
    @PatchMapping(
            value = "/me/profile",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<UserResponse> updateMyProfile(
            @RequestPart("nickname") String nickname,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage
    ) {
        Long loginUserId = auth.requireUserId();
        if (loginUserId == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        byte[] profileBytes = null;
        if (profileImage != null && !profileImage.isEmpty()) {
            try {
                profileBytes = profileImage.getBytes();
            } catch (IOException e) {
                throw new FileInputException("프로필 이미지를 처리하는 중 오류가 발생했습니다.", e);
            }
        }

        User updated = userService.updateProfile(loginUserId, nickname, profileBytes);
        return ResponseEntity.ok(UserResponse.of(updated));
    }


    /* === 3) 내 비밀번호 수정 === */
    @PatchMapping("/me/password")
    public ResponseEntity<Void> changePassword(@RequestBody UpdatePasswordRequest request) {
        Long loginUserId = auth.requireUserId();
        if (loginUserId == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        if (!request.getPassword().equals(request.getPasswordConfirm())) {
            throw new IllegalArgumentException("비밀번호와 비밀번호 확인이 다릅니다.");
        }

        userService.changePassword(loginUserId, request.getPassword());
        return ResponseEntity.noContent().build(); // 204
    }

    /* === 4) 회원 탈퇴 === */
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMe() {
        Long loginUserId = auth.requireUserId();
        if (loginUserId == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        userService.deleteMe(loginUserId);

        return ResponseEntity.noContent().build();
    }

    // Request 클래스
    @Data
    public static class UpdateUserRequest {
        private String nickname;
    }

    @Data
    public static class UpdatePasswordRequest {
        private String password;
        private String passwordConfirm;
    }

    @Data
    public static class CreateUserRequest {
        private String email;
        private String password;
        private String nickname;
    }


}
