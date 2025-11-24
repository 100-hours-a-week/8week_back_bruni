package com.example.my_community.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * 게시물 등록 요청에 대한 dto 클래스
 */
@Data
@AllArgsConstructor
public class PostCreateRequest {
    @NotBlank
    @Size(max = 26)
    private String title; // 게시글 제목

    @NotBlank
    private String content; // 게시글 본문

    private MultipartFile image;

    public PostCreateRequest() {} // 역직렬화용 기본 생성자

}
