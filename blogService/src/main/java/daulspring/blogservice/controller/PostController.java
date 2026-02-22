
package daulspring.blogservice.controller;

import daulspring.blogservice.dto.PostCreateRequestDTO;
import daulspring.blogservice.dto.PostResponseDTO;
import daulspring.blogservice.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/blog/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 게시글 작성 (인증 서비스에서 userId를 넘겨받는다고 가정)
    @PostMapping
    public ResponseEntity<PostResponseDTO> createPost(@RequestParam Long userId, @RequestBody PostCreateRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.createPost(userId, dto));
    }

    // 게시글 상세 조회
    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDTO> getPost(@PathVariable Long postId) {
        postService.incrementViewCount(postId); // 조회수 증가
        return ResponseEntity.ok(postService.getPostById(postId));
    }

    // 최신 게시글 목록 (페이징)
    @GetMapping("/recent")
    public ResponseEntity<Page<PostResponseDTO>> getRecentPosts(Pageable pageable) {
        return ResponseEntity.ok(postService.getRecentPosts(pageable));
    }

    // 태그 추가
    @PostMapping("/{postId}/tags")
    public ResponseEntity<Void> addTag(@PathVariable Long postId, @RequestParam String tagName) {
        postService.addTagToPost(postId, tagName);
        return ResponseEntity.ok().build();
    }
}