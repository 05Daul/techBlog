package daulspring.blogservice.controller;

import daulspring.blogservice.dto.PostCreateRequestDTO;
import daulspring.blogservice.dto.PostResponseDTO;
import daulspring.blogservice.dto.PostUpdateRequestDTO;
import daulspring.blogservice.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/blog/posts")
@RequiredArgsConstructor
@Slf4j
public class PostController {

  private final PostService postService;

  // 게시글 작성 (jwt 인증 필요)
  @PostMapping
  public ResponseEntity<PostResponseDTO> createPost(
      @RequestHeader("X-User-Id") Long userId,  // ← Gateway가 넣어준 userId
      @Valid @RequestBody PostCreateRequestDTO dto) {

    log.info("게시글 작성 요청: userId={}, title={}", userId, dto.getTitle());

    PostResponseDTO response = postService.createPost(userId, dto);
    return ResponseEntity.ok(response);
  }

  // 게시글 조회 (jwt 인증 불필요)
  @GetMapping("/{postId}")
  public ResponseEntity<PostResponseDTO> getPost(@PathVariable Long postId) {

    log.info("게시글 조회 요청: postId={}", postId);

    PostResponseDTO response = postService.getPostById(postId);
    return ResponseEntity.ok(response);
  }

  // 게시물 업데이트(jwt토큰 인증필요)
  @PutMapping("/{postId}")
  public ResponseEntity<PostResponseDTO> updatePost(
      @RequestHeader("X-User-Id") Long userId,
      @PathVariable Long postId,
      @Valid @RequestBody PostUpdateRequestDTO dto) {

    log.info("게시글 수정 요청: userId={}, postId={}", userId, postId);

    PostResponseDTO response = postService.updatePost(userId, postId, dto);
    return ResponseEntity.ok(response);
  }

  // 게시글 삭제(jwt토큰 인증필요)
  @DeleteMapping("/{postId}")
  public ResponseEntity<Void> deletePost(
      @RequestHeader("X-User-Id") Long userId,
      @PathVariable Long postId) {

    log.info("게시글 삭제 요청: userId={}, postId={}", userId, postId);

    postService.deletePost(userId, postId);
    return ResponseEntity.noContent().build();
  }


  // 최신 게시글 목록
  @GetMapping
  public ResponseEntity<Page<PostResponseDTO>> getRecentPosts(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {

    log.info("최신 게시글 목록 조회: page={}, size={}", page, size);

    Pageable pageable = PageRequest.of(page, size);
    Page<PostResponseDTO> posts = postService.getRecentPosts(pageable);

    return ResponseEntity.ok(posts);
  }

  // 내 게시글 목록
  @GetMapping("/my")
  public ResponseEntity<Page<PostResponseDTO>> getMyPosts(
      @RequestHeader("X-User-Id") Long userId,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {

    log.info("내 게시글 조회: userId={}, page={}", userId, page);

    Pageable pageable = PageRequest.of(page, size);
    Page<PostResponseDTO> posts = postService.getMyPosts(userId, pageable);

    return ResponseEntity.ok(posts);
  }
}