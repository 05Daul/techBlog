package daulspring.blogservice.controller;

import daulspring.blogservice.dto.BookmarkResponseDTO;
import daulspring.blogservice.service.BookmarkService;
import daulspring.blogservice.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/blog/interactions")
@RequiredArgsConstructor
public class InteractionController {

    private final LikeService likeService;
    private final BookmarkService bookmarkService;

    // 좋아요 추가
    @PostMapping("/likes/{postId}")
    public ResponseEntity<Void> addLike(@PathVariable Long postId, @RequestParam Long userId) {
        likeService.addLike(postId, userId);
        return ResponseEntity.ok().build();
    }

    // 좋아요 취소
    @DeleteMapping("/likes/{postId}")
    public ResponseEntity<Void> deleteLike(@PathVariable Long postId, @RequestParam Long userId) {
        likeService.deleteByPostIdAndUserSignId(postId, userId);
        return ResponseEntity.noContent().build();
    }

    // 북마크 추가
    @PostMapping("/bookmarks/{postId}")
    public ResponseEntity<Void> addBookmark(@PathVariable Long postId, @RequestParam Long userId) {
        bookmarkService.addBookmark(userId, postId);
        return ResponseEntity.ok().build();
    }

    // 내 북마크 목록 조회
    @GetMapping("/bookmarks")
    public ResponseEntity<Page<BookmarkResponseDTO>> getMyBookmarks(@RequestParam Long userId, Pageable pageable) {
        return ResponseEntity.ok(bookmarkService.getUserBookmarks(userId, pageable));
    }
}