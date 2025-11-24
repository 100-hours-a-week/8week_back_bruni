package com.example.my_community.comment.domain;

import com.example.my_community.post.domain.Post;
import com.example.my_community.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;


import java.time.OffsetDateTime;

@Entity
@Getter
public class Comment {
    @Id @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 1000)
    private String content;


    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;


    public Comment() {}

    public Comment(Post post, User user, String content) {
        this.post = post;
        this.user = user;
        this.content = content;
        this.createdAt = OffsetDateTime.now();
    }

    public void changeContent(String content) {
        this.content = content;
    }
}
