package com.example.springboot_basic.service;

import com.example.springboot_basic.domain.comment.Comment;
import com.example.springboot_basic.domain.member.Member;
import com.example.springboot_basic.domain.post.Post;
import com.example.springboot_basic.dto.comment.CommentForm;
import com.example.springboot_basic.dto.response.Header;
import com.example.springboot_basic.dto.response.ResponseData;
import com.example.springboot_basic.repository.CommentRepository;
import com.example.springboot_basic.repository.MemberRepository;
import com.example.springboot_basic.repository.PostRepository;
import com.example.springboot_basic.security.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // 댓글 등록
    public ResponseData<String> addComment(CommentForm commentForm) {
        // 1. 게시글이 있는가?
        Optional<Post> findPost = postRepository.findById(commentForm.getPostId());
        if (!findPost.isPresent()) return new ResponseData<>(Header.badRequest("게시글이 존재하지 않습니다."), "");
        // 2. 회원이 있는가?
        Member member = null;
        String password = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getName().equals("anonymousUser")) { // 로그인 확인 여부
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            member = principalDetails.getMember();
        } else {
            password = bCryptPasswordEncoder.encode(commentForm.getPassword());
        }
        // 3. 대댓글인가?
        Comment parent = null;
        if (!commentForm.isEmpty("parentId")) {
            Optional<Comment> findParent = commentRepository.findById(commentForm.getParentId());
            parent = findParent.orElse(null);
        }
        // 5. 댓글 생성
        Comment comment = Comment.builder()
                .content(commentForm.getContent())
                .password(password)
                .parent(parent)
                .member(member)
                .post(findPost.get())
                .build();
        commentRepository.save(comment);
        return new ResponseData<>(Header.ok("댓글이 등록되었습니다."), "");
    }
}
