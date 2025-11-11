package com.example.my_community.user.controller;

import com.example.my_community.user.domain.Role;
import com.example.my_community.user.domain.User;
import com.example.my_community.user.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserResponse create(@RequestBody CreateUserRequest request) {
        User saved = userService.create(request.email, request.password, request.role);
        return UserResponse.of(saved);
    }

    @GetMapping(("/{id}"))
    public UserResponse findById(@PathVariable Long id) {
        return UserResponse.of(userService.findById(id));
    }

    @PatchMapping("/{id}")
    public UserResponse update(@PathVariable Long id, @RequestBody UpdateUserRequest request) {
        User updatedUser = userService.update(id, request.nickname);
        return UserResponse.of(updatedUser);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }

    @Data
    public static class UpdateUserRequest {
        private String nickname;
    }

    @Data
    public static class CreateUserRequest {
        private String email;
        private String password;
        private Role role;
    }

    @Data
    public static class UserResponse {
        private Long id;
        private String email;
        private Role role;

        public static UserResponse of(User user) {
            return new UserResponse(user.getId(), user.getEmail(), user.getRole());
        }

        public UserResponse(Long id, String email, Role role) {
            this.id = id;
            this.email = email;
            this.role = role;
        }
    }
}
