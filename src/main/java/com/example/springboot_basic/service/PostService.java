package com.example.springboot_basic.service;

import com.example.springboot_basic.domain.comment.Comment;
import com.example.springboot_basic.domain.member.Member;
import com.example.springboot_basic.domain.post.File;
import com.example.springboot_basic.domain.post.Post;
import com.example.springboot_basic.dto.UploadFile;
import com.example.springboot_basic.dto.comment.CommentResponse;
import com.example.springboot_basic.dto.member.MemberInfoResponse;
import com.example.springboot_basic.dto.post.*;
import com.example.springboot_basic.dto.response.Header;
import com.example.springboot_basic.dto.response.ResponseData;
import com.example.springboot_basic.exception.PostNotExistException;
import com.example.springboot_basic.repository.CommentRepository;
import com.example.springboot_basic.security.PrincipalDetails;
import com.example.springboot_basic.util.FileStoreUtil;
import com.example.springboot_basic.repository.FileRepository;
import com.example.springboot_basic.repository.MemberRepository;
import com.example.springboot_basic.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final FileStoreUtil fileStoreUtil;
    private final FileRepository fileRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;

    public Long savePost(PostForm form, MemberInfoResponse memberInfo) throws IOException {
        Optional<Member> findMember = memberRepository.findById(memberInfo.getId());
        Post post = Post.builder()
                .title(form.getTitle())
                .content(form.getContent())
                .viewCount(0)
                .category(form.getCategory())
                .member(findMember.get())
                .build();
        Post savePost = postRepository.save(post);
        List<UploadFile> storeImageFiles = fileStoreUtil.storeFiles(form.getImageFiles());
        storeImageFiles.forEach(file -> fileRepository.save(new File(file, savePost)));
        return savePost.getId();
    }

    public List<PostsResponse> findPosts(PostSearch postSearch) {
        // 동적으로 쿼리를 넣을수 없어서 if문과 repository 함수를 전부 만들어서 실행해야함!!!
        List<Post> posts;
        if (postSearch.isEmpty()) posts = postRepository.findAll();
        else if (postSearch.getPostTitle() == null || postSearch.getPostTitle().equals(""))
            posts = postRepository.findPostsByCategory(postSearch);
        else if (postSearch.getPostCategory() == null) posts = postRepository.findPostsByTitle(postSearch);
        else posts = postRepository.findPosts(postSearch);
        List<PostsResponse> postsDto = posts.stream().map(PostsResponse::new).collect(Collectors.toList());
        return postsDto;
    }

    public PostInfoResponse findPost(Long postId) {
        Post post = postRepository.findById(postId)
                .map(Post::addViewCount)
                .orElseThrow(() -> new PostNotExistException("게시글이 존재하지 않습니다."));
        List<FilesResponse> storeImageNames = fileRepository.findByPost(post).stream()
                .map(FilesResponse::new)
                .collect(Collectors.toList());
        List<CommentResponse> commentResponses = commentRepository.findCommentsForPost(post).stream()
                .map(CommentResponse::new)
                .collect(Collectors.toList());
        return PostInfoResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .viewCount(post.getViewCount())
                .createdName(post.getCreatedBy())
                .createdDate(post.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .updatedDate(post.getUpdatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .storeImageNames(storeImageNames)
                .comments(commentResponses)
                .build();
    }

    public PostEditResponse findUpdatePost(Long postId) {
        Optional<Post> findPost = postRepository.findById(postId);
        Post post = findPost.orElse(null);
        if (post == null) return null;
        List<File> files = fileRepository.findByPost(post);
        List<FilesResponse> storeImageNames = files.stream().map(FilesResponse::new).collect(Collectors.toList());
        return new PostEditResponse(post.getId(), post.getTitle(), post.getContent(), storeImageNames);
    }

    // orElse는 값이 있든 없든 무조건 실행, orElseGet은 값이 없을때만 실행, orElseThrow로 예외 던지기
    public ResponseData<String> updatePost(PostForm form, Long postId) throws IOException {
        Post post = postRepository.findById(postId)
                .map(p -> p.updateForm(form)) // Optional에 값이 있으면 연산 수행
                .orElseThrow(() -> new PostNotExistException("게시글이 존재하지 않습니다."));
        if (!form.getImageFiles().isEmpty()) {
            List<UploadFile> storeImageFiles = fileStoreUtil.storeFiles(form.getImageFiles());
            storeImageFiles.forEach(f -> fileRepository.save(new File(f, post)));
        }
        return new ResponseData<>(Header.ok("수정되었습니다."), "");
    }

    public ResponseData<String> deletePost(Long postId) {
        PrincipalDetails principalDetails = (PrincipalDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Member member = principalDetails.getMember();
        Optional<Post> findPost = postRepository.findById(postId);
        Post post = findPost.orElse(null);
        if (post == null) return new ResponseData<>(Header.badRequest("게시글이 존재하지 않습니다."), "");
        if (!member.getLoginId().equals(post.getMember().getLoginId()))
            return new ResponseData<>(Header.badRequest("등록한 회원이 아닙니다."), "");
        // 파일 삭제
        List<File> files = fileRepository.findByPost(post);
        if (files.size() > 0) {
            files.forEach(file -> {
                String fullPath = fileStoreUtil.getFullPath(file.getStoreFileName());
                java.io.File imageFile = new java.io.File(fullPath);
                if (imageFile.exists()) imageFile.delete();
                fileRepository.delete(file);
            });
        }
        // 댓글 삭제
        List<Comment> comments = commentRepository.findCommentsForPost(post);
        List<Comment> childs = new ArrayList<>();
        comments.forEach(c -> {
            if (c.getChild().size() > 0) childs.addAll(c.getChild());
        });
        commentRepository.deleteAllInBatch(childs);
        commentRepository.deleteAllInBatch(comments);
        postRepository.delete(post);
        return new ResponseData<>(Header.ok("게시글이 삭제되었습니다."), "");
    }

    public ResponseData<String> validatePost(Long postId) {
        PrincipalDetails principalDetails = (PrincipalDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Member member = principalDetails.getMember();
        Optional<Post> findPost = postRepository.findById(postId);
        Post post = findPost.orElse(null);
        if (post == null) return new ResponseData<>(Header.badRequest("게시글이 존재하지 않습니다."), "");
        if (!member.getLoginId().equals(post.getMember().getLoginId()))
            return new ResponseData<>(Header.badRequest("등록한 회원이 아닙니다."), "");
        return new ResponseData<>(Header.ok("게시글을 등록한 회원입니다."), "");
    }
}
