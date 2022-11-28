package com.example.springboot_basic.dto.post;

import com.example.springboot_basic.domain.post.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Page;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class PostsPageResponse {

    private int page;
    private int size;
    private int totalPages;
    private int startPage;
    private int endPage;
    private List<PostPageResponse> posts = new ArrayList<>();

    public PostsPageResponse(Page<Post> pages) {
        posts = pages.map(PostPageResponse::new)
                .toList();
        page = pages.getNumber();
        size = pages.getSize();
        totalPages = pages.getTotalPages();
        startPage = calculateStartPage();
        endPage = calculateEndPage();
    }

    // 현재 페이지 그룹의 시작 페이지 구하기
    public int calculateStartPage() {
        if (posts.isEmpty()) {
            return 0;
        }

        return (page / 10) * 10 + 1;
    }

    // 현재 페이지 그룹의 마지막 페이지 구하기
    public int calculateEndPage() {
        if (posts.isEmpty()) {
            return 0;
        }

        return Math.min(startPage + 9, totalPages);
    }

    @Getter
    @ToString
    public static class PostPageResponse {
        private Long id;
        private String title;
        private int viewCount;
        private String createdName;
        private String createdDate;

        public PostPageResponse(Post post) {
            this.id = post.getId();
            this.title = post.getTitle();
            this.viewCount = post.getViewCount();
            this.createdName = post.getCreatedBy();
            this.createdDate = post.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
    }
}
