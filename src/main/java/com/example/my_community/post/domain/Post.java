package com.example.my_community.post.domain;

import com.example.my_community.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private  Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private  User author;

    @Column(nullable = false, length = 26) // 제목 최대 26자
    private String title;

    @Lob
    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String content;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] image;

    @Column(nullable = false)
    private OffsetDateTime createdAt;

    @Column(nullable = false)
    private int likeCount;

    @Column(nullable = false)
    private int viewCount; // 조회수


    // 생성자 (도메인 규칙 반영)
    public Post(User author, String title, String content, byte[] image) {
        this.author = author;
        this.title = title;
        this.content = content;
        this.image = image;
        this.createdAt = OffsetDateTime.now();
        this.likeCount = 0;
        this.viewCount = 0;   // 최초 0
    }

    // 수정 메서드
    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeContent(String content) {
        this.content = content;
    }

    // 조회수 증가 메서드 (단건 조회 시 사용)
    public void increaseViewCount() {
        this.viewCount++;
    }

    public void increaseLikeCount() {
        this.likeCount++;
    }

    public void decreaseLikeCount() {
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }


    public void changeImage(byte[] image) {
        this.image = image;
    }


}
