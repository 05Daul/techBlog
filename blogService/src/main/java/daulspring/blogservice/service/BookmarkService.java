package daulspring.blogservice.service;

import daulspring.blogservice.dto.BookmarkResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookmarkService {


  // 북마크 추가
  void addBookmark(Long userId, Long postId);

  // 북마크 취소
  void removeBookmark(Long userId, Long postId);

  // 특정 사용자의 북마크 목록 조회
  Page<BookmarkResponseDTO> getUserBookmarks(Long userId, Pageable pageable);

  // 특정 게시글 북마크 여부 확인
  boolean isBookmarked(Long userId, Long postId);

}
