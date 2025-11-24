package com.example.my_community.comment.dto;

import lombok.Data;

@Data
public class CommentRes {
    private final Long id;
    private final Long postId;
    private final Long authorId;
    private final String authorNickname;
    private final String content;
    private final String createdAt;
    private final boolean mine;

    public CommentRes(Long id, Long postId, Long authorId, String authorNickname, String content, String createdAt, boolean mine) {
        this.id = id;
        this.postId = postId;
        this.authorId = authorId;
        this.authorNickname = authorNickname;
        this.content = content;
        this.createdAt = createdAt;
        this.mine = mine;
    }

}
