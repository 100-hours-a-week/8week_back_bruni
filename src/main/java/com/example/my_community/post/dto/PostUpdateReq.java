package com.example.my_community.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

/**
 * 게시물 수정 요청에 대한 dto 클래스
 */
@Data
public class PostUpdateReq {

    @NotBlank
    @Size(min = 1, max = 100)
    private String title;

    @NotBlank
    @Size(min = 1, max = 5000)
    private String content;

    private MultipartFile image;

    public PostUpdateReq() { }

    public PostUpdateReq(String title, String content) {
        this.title = title;
        this.content = content;
    }

}
