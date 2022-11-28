package com.example.springboot_basic.service;

import com.example.springboot_basic.domain.comment.Comment;
import com.example.springboot_basic.domain.member.Member;
import com.example.springboot_basic.domain.post.File;
import com.example.springboot_basic.domain.post.Post;
import com.example.springboot_basic.domain.post.PostCategory;
import com.example.springboot_basic.dto.UploadFile;
import com.example.springboot_basic.dto.comment.CommentResponse;
import com.example.springboot_basic.dto.member.MemberInfoResponse;
import com.example.springboot_basic.dto.post.FilesResponse;
import com.example.springboot_basic.dto.post.PostEditResponse;
import com.example.springboot_basic.dto.post.PostForm;
import com.example.springboot_basic.dto.post.PostInfoResponse;
import com.example.springboot_basic.dto.post.PostSearch;
import com.example.springboot_basic.dto.post.PostsPageResponse;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
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

    public PostsPageResponse findPostsPaging(PostSearch postSearch, Pageable pageable) {
        return new PostsPageResponse(getPostsPage(postSearch, pageable));
    }
    
    public Page<Post> getPostsPage(PostSearch postSearch, Pageable pageable) {
        if (postSearch.isEmpty()) {
            return postRepository.findAllPaging(pageable);
        }

        if (postSearch.getPostTitle() == null || postSearch.getPostTitle().equals("")) {
            return postRepository.findPostsByCategory(postSearch, pageable);
        }

        if (postSearch.getPostCategory() == null) {
            return postRepository.findPostsByTitle(postSearch, pageable);
        }

        return postRepository.findPosts(postSearch, pageable);
    }

    public PostInfoResponse findPost(Long postId, HttpServletRequest request, HttpServletResponse response) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotExistException("게시글이 존재하지 않습니다."));
        viewCountValidation(post, request, response);
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

    private void viewCountValidation(Post post, HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        Cookie cookie = null;
        boolean isCookie = false;
        for (int i = 0; cookies != null && i < cookies.length; i++) { // request에 쿠키들이 있을 때
            if (cookies[i].getName().equals("postView")) { // postView 쿠키가 있을 때
                cookie = cookies[i];
                if (!cookie.getValue().contains("[" + post.getId() + "]")) {
                    post.addViewCount();
                    cookie.setValue(cookie.getValue() + "[" + post.getId() + "]");
                }
                isCookie = true;
                break;
            }
        }
        if (!isCookie) { // postView 쿠키가 없을 때
            post.addViewCount();
            cookie = new Cookie("postView", "[" + post.getId() + "]"); // oldCookie에 새 쿠키 생성
        }
        long todayEndSecond = LocalDate.now().atTime(LocalTime.MAX).toEpochSecond(ZoneOffset.UTC);
        long currentSecond = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        cookie.setPath("/"); // 모든 경로에서 접근 가능
        cookie.setMaxAge((int) (todayEndSecond - currentSecond)); // 오늘 하루 자정까지 남은 시간초 설정
        response.addCookie(cookie);
    }

    public PostEditResponse findUpdatePost(Long postId) {
        Optional<Post> findPost = postRepository.findById(postId);
        Post post = findPost.orElse(null);
        if (post == null) return null;
        List<File> files = fileRepository.findByPost(post);
        List<FilesResponse> storeImageNames = files.stream().map(FilesResponse::new).collect(Collectors.toList());
        return new PostEditResponse(post.getId(), post.getTitle(), post.getContent(), storeImageNames);
    }

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
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotExistException("게시글이 존재하지 않습니다."));
        if (!member.getLoginId().equals(post.getMember().getLoginId()))
            return new ResponseData<>(Header.badRequest("등록한 회원이 아닙니다."), "");
        // 파일 삭제
        fileRepository.findByPost(post).forEach(file -> {
            String fullPath = fileStoreUtil.getFullPath(file.getStoreFileName());
            java.io.File imageFile = new java.io.File(fullPath);
            if (imageFile.exists()) imageFile.delete();
            fileRepository.delete(file);
        });
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
