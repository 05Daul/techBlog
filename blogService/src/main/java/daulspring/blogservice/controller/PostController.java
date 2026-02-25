package daulspring.blogservice.controller;

import daulspring.blogservice.dto.PostCreateRequestDTO;
import daulspring.blogservice.dto.PostResponseDTO;
import daulspring.blogservice.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/blog/posts")
@RequiredArgsConstructor
@Slf4j
public class PostController {

  private final PostService postService;

  // 게시글 작성 (인증 필요)
  @PostMapping
  public ResponseEntity<PostResponseDTO> createPost(
      @RequestHeader("X-User-Id") Long userId,  // ← Gateway가 넣어준 userId
      @Valid @RequestBody PostCreateRequestDTO dto) {

    log.info("게시글 작성 요청: userId={}, title={}", userId, dto.getTitle());

    PostResponseDTO response = postService.createPost(userId, dto);
    return ResponseEntity.ok(response);
  }

  // 게시글 조회 (인증 불필요)
  @GetMapping("/{postId}")
  public ResponseEntity<PostResponseDTO> getPost(@PathVariable Long postId) {

    log.info("게시글 조회 요청: postId={}", postId);

    PostResponseDTO response = postService.getPostById(postId);
    return ResponseEntity.ok(response);
  }
}