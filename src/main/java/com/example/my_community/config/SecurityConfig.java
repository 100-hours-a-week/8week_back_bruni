package com.example.my_community.config;

import com.example.my_community.auth.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // CSRF 토큰 일단 비활성화
                .csrf(csrf -> csrf.disable())

                // 기존 WebMvcConfigurer(CorsConfig) 설정을 Security에서도 사용
                .cors(cors -> {})

                // 세션 기반(쿠키) 인증 유지
                .sessionManagement(sess ->
                        sess.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))

                // 인가 규칙
                .authorizeHttpRequests(auth -> auth
                        //회원가입, 로그인, 문서는 인증 없이 허용
                        .requestMatchers(
                                "/api/auth/login",
                                "/api/users",
                                "/v3/api-docs/**",
                                "/swagger-ui.html",
                                "/swagger-ui/**"
                        ).permitAll()
                        // 나머지 /api/** 는 인증 필요
                        .requestMatchers("/api/**").hasRole("USER")
                        // 그 외(정적 리소스 등)는 일단 모두 허용
                        .anyRequest().permitAll()
                )
                // 기본 로그인 페이지, HTTP Basic 끄기
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable());
        return httpSecurity.build();
    }

    // 비밀번호 암호화를 위한 PasswordEncoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // DaoAuthenticationProvider 기반 AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager(PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(provider);
    }
}
