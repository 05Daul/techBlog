package daulspring.blogservice.repository;

import daulspring.blogservice.entity.BookmarkEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<BookmarkEntity, Long> {

  // 북마크 존재 여부
  boolean existsByUserIdAndPostId(Long userId, Long postId);
  
  // 북마크 찾기
  Optional<BookmarkEntity> findByUserIdAndPostId(Long userId, Long postId);
  
  // 사용자의 북마크 목록 (페이징)
  Page<BookmarkEntity> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
}