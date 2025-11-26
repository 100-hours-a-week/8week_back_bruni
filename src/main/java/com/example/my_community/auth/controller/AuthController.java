package com.example.my_community.auth.controller;

import com.example.my_community.auth.dto.LoginRequest;
import com.example.my_community.auth.security.CustomUserDetails;
import com.example.my_community.common.exception.UnauthorizedException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "Auth", description = "세션 기반 인증 API")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;

    public AuthController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }


    @Operation(summary = "로그인(세션 생성)")
    @ApiResponse(responseCode = "204", description = "로그인 성공 (세션 생성)")
    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest request,
                                      HttpServletRequest httpServletRequest) {

        UsernamePasswordAuthenticationToken authenticationToken = new
                UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());

        try {
            // 1) 인증 시도 (CustomUserDetailsService 호출)
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            // 2) 인증 성공 (SecurityContext에 저장)
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);

            // 3) 세션에 SecurityContext 저장
            HttpSession session = httpServletRequest.getSession(true);
            session.setAttribute(
                    HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context
            );

            // 프론트(login.js)에서 기대하는 200 OK 응답
            return ResponseEntity.noContent().build();
        } catch (BadCredentialsException e) {
            // 비밀번호 틀림
            throw new UnauthorizedException("이메일 또는 비밀번호가 올바르지 않습니다.");
        } catch (AuthenticationException e) {
            // 기타 인증 관련 예외
            throw new UnauthorizedException("로그인에 실패했습니다.");
        }
    }

    @Operation(summary = "로그아웃(세션 만료)")
    @ApiResponse(responseCode = "204", description = "로그아웃 성공 (세션 무효화)")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        // SecurityContext 비우기
        SecurityContextHolder.clearContext();

        // 세션 무효화
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        return ResponseEntity.noContent().build();
    }

    private Long extractUserId(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails customUserDetails) {
            return customUserDetails.getId();
        }
        return null;
    }

    static class MeResponse {
        @Schema(example = "1")
        public Long userId;
        public MeResponse(Long userId) { this.userId = userId; }
    }

    @Operation(summary = "내 세션 확인")
    @ApiResponse(
            responseCode = "200",
            description = "세션에 저장된 사용자",
            content = @Content(schema = @Schema(implementation = MeResponse.class))
    )

    // 회원 정보 수정 매핑 : 수정 필요
    @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> me(@Parameter(hidden = true) HttpServletRequest request) {
        Long uid = 1L;
        return ResponseEntity.ok(Map.of("userId", uid));
    }
}
