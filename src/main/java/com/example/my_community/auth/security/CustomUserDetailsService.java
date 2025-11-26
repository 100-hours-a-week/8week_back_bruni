package com.example.my_community.auth.security;

import com.example.my_community.user.domain.User;
import com.example.my_community.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // username = email로 사용
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found: " + username));
        // USER, ADMIN 방식의 enum 쓴다고 가정
        String roleName = user.getRole().name(); // USER

        List<GrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority("ROLE_" + roleName)); // ROLE_USER

        return new CustomUserDetails(
                user.getId(),
                user.getEmail(),
                user.getPassword(), // 암호화된 비밀번호
                authorities
        );
    }
}
