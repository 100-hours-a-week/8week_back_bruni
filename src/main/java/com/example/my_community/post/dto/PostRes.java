package com.example.my_community.post.dto;

import lombok.Data;
import lombok.Getter;

/**
 * 게시물관련 응답 dto 클래스
 */
@Data
public class PostRes {
    private final Long id;
    private final String title;
    private final String content;
    private final Long authorId;
    private String authorNickname;
    private final String createdAt;
    private final int likeCount;
    private final int viewCount;
    private final int commentCount;

    public PostRes(Long id, String title, String content, Long authorId, String authorNickname, String createdAt, int likeCount, int viewCount, int commentCount) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.authorId = authorId;
        this.authorNickname = authorNickname;
        this.createdAt = createdAt;
        this.likeCount = likeCount;
        this.viewCount = viewCount;
        this.commentCount = commentCount;
    }
}
