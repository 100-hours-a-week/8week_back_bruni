package com.example.my_community.comment.service;

import com.example.my_community.comment.domain.Comment;
import com.example.my_community.comment.dto.CommentCreateReq;
import com.example.my_community.comment.dto.CommentRes;
import com.example.my_community.comment.repository.CommentRepository;
import com.example.my_community.common.exception.ForbiddenException;
import com.example.my_community.common.exception.NotFoundException;
import com.example.my_community.post.domain.Post;
import com.example.my_community.post.repository.PostRepository;
import com.example.my_community.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository; // 포스트 존재 확인용
    private final UserRepository userRepository;
    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    @Transactional
    /* 생성 (세션 사용자 authorId 주입) */
    public CommentRes create(Long postId, CommentCreateReq req, Long currentUserId) {
        // 게시글 존재 확인
        Post post = postRepository.findById(postId).orElseThrow(() ->
                new NotFoundException("게시글을 찾을 수 없습니다. id=" + postId));

        Comment c = new Comment(null, postRepository.getReferenceById(postId), userRepository.getReferenceById(currentUserId), req.getContent(), OffsetDateTime.now());
        Comment saved = commentRepository.save(c);
        return toRes(saved);
    }

    /** 게시글별 목록 (페이지네이션) */
    public List<CommentRes> listByPost(Long postId, int page, int size, String sort, String dir) {
        // 게시글 존재 확인(없는 postId로 요청하면 404)
        postRepository.findById(postId).orElseThrow(() ->
                new NotFoundException("게시글을 찾을 수 없습니다. id=" + postId));

        boolean desc = "desc".equalsIgnoreCase(dir);
        return commentRepository.findById(postId).stream()
                .map(this::toRes).toList();
    }

    public long count() {
        return commentRepository.count();
    }

    @Transactional
    /* 삭제(작성자만) */
    public void delete(Long commentId, Long currentUserId) {
        Comment c = commentRepository.findById(commentId).orElseThrow(() ->
                new NotFoundException("댓글을 찾을 수 없습니다. id=" + commentId));
        if (!c.getUser().getId().equals(currentUserId)) {
            throw new ForbiddenException("작성자만 삭제할 수 있습니다.");
        }
        commentRepository.deleteById(commentId);
    }

    private CommentRes toRes(Comment c) {
        String created = (c.getCreatedAt() != null) ? ISO.format(c.getCreatedAt()) : null;
        return new CommentRes(c.getId(), c.getPost().getId(), c.getUser().getId(), c.getContent(), created);
    }
}
