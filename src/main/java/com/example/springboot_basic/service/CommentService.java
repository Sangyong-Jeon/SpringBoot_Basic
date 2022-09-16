package com.example.springboot_basic.service;

import com.example.springboot_basic.domain.comment.Comment;
import com.example.springboot_basic.domain.member.Member;
import com.example.springboot_basic.domain.post.Post;
import com.example.springboot_basic.dto.comment.ChildCommentForm;
import com.example.springboot_basic.dto.comment.CommentForm;
import com.example.springboot_basic.dto.response.Header;
import com.example.springboot_basic.dto.response.ResponseData;
import com.example.springboot_basic.exception.CommentNotExistException;
import com.example.springboot_basic.exception.PostNotExistException;
import com.example.springboot_basic.repository.CommentRepository;
import com.example.springboot_basic.repository.MemberRepository;
import com.example.springboot_basic.repository.PostRepository;
import com.example.springboot_basic.security.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // 댓글 등록
    public ResponseData<String> addComment(CommentForm commentForm) {
        // 1. 게시글이 있는가?
        Post post = postRepository.findById(commentForm.getPostId())
                .orElseThrow(() -> new PostNotExistException("게시글이 존재하지 않습니다."));
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
        if (!commentForm.isParentIdEmpty())
            parent = commentRepository.findById(commentForm.getParentId())
                    .orElseThrow(() -> new CommentNotExistException("댓글이 존재하지 않습니다."));
        // 5. 댓글 생성
        Comment comment = Comment.builder()
                .content(commentForm.getContent())
                .password(password)
                .parent(parent)
                .member(member)
                .post(post)
                .build();
        commentRepository.save(comment);
        return new ResponseData<>(Header.ok("댓글이 등록되었습니다."), "");
    }

    // 댓글 수정
    public ResponseData<String> updateComment(CommentForm commentForm) {
        // 1. 댓글 여부 확인
        Comment comment = commentRepository.findById(commentForm.getCommentId())
                .orElseThrow(() -> new CommentNotExistException("댓글이 존재하지 않습니다."));
        // 2. 회원이면 등록한 회원이 맞는지 or 비회원이면 비밀번호가 동일한지 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getName().equals("anonymousUser")) {
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            if (!comment.getMember().getLoginId().equals(principalDetails.getUsername()))
                return new ResponseData<>(Header.badRequest("댓글을 등록한 회원이 아닙니다."), "");
        } else {
            if (!bCryptPasswordEncoder.matches(commentForm.getPassword(), comment.getPassword()))
                return new ResponseData<>(Header.badRequest("댓글을 등록한 사용자가 아닙니다."), "");
        }
        // 3. 댓글 수정
        comment.updateContent(commentForm.getContent());
        return new ResponseData<>(Header.ok("댓글이 수정되었습니다."), "");
    }

    // 대댓글 수정
    public ResponseData<String> updateChildComment(ChildCommentForm childForm) {
        // 1. 대댓글 여부 확인
        Comment childComment = commentRepository.findById(childForm.getChildId())
                .orElseThrow(() -> new CommentNotExistException("대댓글이 존재하지 않습니다."));
        // 2. 회원이면 등록한 회원이 맞는지 or 비회원이면 비밀번호가 동일한지 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getName().equals("anonymousUser")) {
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            if (!childComment.getMember().getLoginId().equals(principalDetails.getUsername()))
                return new ResponseData<>(Header.badRequest("대댓글을 등록한 회원이 아닙니다."), "");
        } else {
            if (!bCryptPasswordEncoder.matches(childForm.getPassword(), childComment.getPassword()))
                return new ResponseData<>(Header.badRequest("대댓글을 등록한 사용자가 아닙니다."), "");
        }
        childComment.updateContent(childForm.getContent());
        return new ResponseData<>(Header.ok("대댓글이 수정되었습니다."), "");
    }
}
