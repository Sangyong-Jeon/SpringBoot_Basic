package com.example.springboot_basic.service;

import com.example.springboot_basic.domain.member.Member;
import com.example.springboot_basic.domain.post.File;
import com.example.springboot_basic.domain.post.Post;
import com.example.springboot_basic.dto.UploadFile;
import com.example.springboot_basic.dto.member.MemberInfoResponse;
import com.example.springboot_basic.dto.post.*;
import com.example.springboot_basic.file.FileStore;
import com.example.springboot_basic.repository.FileRepository;
import com.example.springboot_basic.repository.MemberRepository;
import com.example.springboot_basic.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final FileStore fileStore;
    private final FileRepository fileRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

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

        List<UploadFile> storeImageFiles = fileStore.storeFiles(form.getImageFiles());
        storeImageFiles.forEach(file -> fileRepository.save(new File(file, savePost)));
        return savePost.getId();
    }

    public List<PostsResponse> findPosts(PostSearch postSearch) {
        // 동적으로 쿼리를 넣을수 없어서 if문과 repository 함수를 전부 만들어서 실행해야함!!!
        List<Post> posts;
        if (postSearch.isEmpty()) posts = postRepository.findAll();
        else if (postSearch.getPostTitle() == null || postSearch.getPostTitle().equals("")) posts = postRepository.findPostsByCategory(postSearch);
        else if (postSearch.getPostCategory() == null) posts = postRepository.findPostsByTitle(postSearch);
        else posts = postRepository.findPosts(postSearch);
        List<PostsResponse> postsDto = posts.stream().map(PostsResponse::new).collect(Collectors.toList());
        return postsDto;
    }

    public PostInfoResponse findPost(Long postId) {
        Optional<Post> findPost = postRepository.findById(postId);
        Post post = findPost.orElse(null);
        if (post == null) return null;
        List<File> files = fileRepository.findByPost(post);
        List<FilesResponse> storeImageName = files.stream().map(FilesResponse::new).collect(Collectors.toList());

        return PostInfoResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .viewCount(post.getViewCount())
                .createdName(post.getCreatedBy())
                .createdDate(post.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .updatedDate(post.getUpdatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .storeImageName(storeImageName)
                .build();
    }

    public Long updatePost(PostInfoResponse form) throws IOException {
        Optional<Post> findPost = postRepository.findById(form.getId());
        Post post = findPost.orElse(null);
        if (post == null) return null;
        post.updateForm(form);
        List<UploadFile> storeImageFiles = fileStore.storeFiles(form.getImageFiles());
        storeImageFiles.forEach(f -> fileRepository.save(new File(f, post)));
        return post.getId();
    }

    public ResponseEntity deletePost(Long postId) {
        Optional<Post> findPost = postRepository.findById(postId);
        Post post = findPost.orElse(null);
        if (post == null) return null;

        List<File> files = fileRepository.findByPost(post);
        if (files.size() > 0) {
            files.forEach(file -> {
                String fullPath = fileStore.getFullPath(file.getStoreFileName());
                java.io.File imageFile = new java.io.File(fullPath);
                if (imageFile.exists()) imageFile.delete();
                fileRepository.delete(file);
            });
        }
        postRepository.delete(post);
        return new ResponseEntity(HttpStatus.OK);
    }
}
