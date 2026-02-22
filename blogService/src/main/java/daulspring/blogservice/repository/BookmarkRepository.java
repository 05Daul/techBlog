package daulspring.blogservice.repository;

import daulspring.blogservice.dto.BookmarkResponseDTO;
import daulspring.blogservice.entity.BookmarkEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<BookmarkEntity, Long> {

  Page<BookmarkEntity> getUserBookmarks(Long userId, Pageable pageable);
  // 북마크 존재 여부
  boolean existsByUserIdAndPostId(Long userId, Long postId);

  // 특정 북마크 조회
  Optional<BookmarkEntity> findByUserIdAndPostId(Long userId, Long postId);

  // 사용자의 북마크 목록
  Page<BookmarkEntity> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
}