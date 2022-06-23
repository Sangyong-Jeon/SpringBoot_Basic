package com.example.springboot_basic.service;

import com.example.springboot_basic.domain.member.Member;
import com.example.springboot_basic.domain.post.File;
import com.example.springboot_basic.domain.post.Post;
import com.example.springboot_basic.dto.UploadFile;
import com.example.springboot_basic.dto.member.MemberInfoResponse;
import com.example.springboot_basic.dto.post.FilesResponse;
import com.example.springboot_basic.dto.post.PostForm;
import com.example.springboot_basic.dto.post.PostInfoResponse;
import com.example.springboot_basic.dto.post.PostsResponse;
import com.example.springboot_basic.file.FileStore;
import com.example.springboot_basic.repository.FileRepository;
import com.example.springboot_basic.repository.MemberRepository;
import com.example.springboot_basic.repository.PostRepository;
import lombok.RequiredArgsConstructor;
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

    public List<PostsResponse> findPosts() {
        List<Post> posts = postRepository.findAll();
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
}
