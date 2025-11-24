package com.example.my_community.post.domain;

import com.example.my_community.user.domain.Role;
import com.example.my_community.user.domain.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springdoc.core.annotations.RouterOperation;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PostTest {
    @PersistenceContext
    EntityManager entityManager;

//    @Test
//    @Rollback(false)
//    void idTest() {
//        User user = new User("bruni@gmail.com", "123123", "bruni", Role.USER);
//        Post post = new Post( user,"게시글 제목", "게시글 본문");
//        entityManager.persist(post);
//    }


}