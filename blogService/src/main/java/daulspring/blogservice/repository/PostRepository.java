package daulspring.blogservice.repository;

import daulspring.blogservice.entity.PostEntity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<PostEntity, Long> {

  Optional<PostEntity> findById(Long id);


  // 조회수 처리
  @Modifying
  @Query("UPDATE PostEntity p SET p.viewCount = p.viewCount + 1 WHERE p.id = :postId")
  int incrementViewCount(@Param("postId") Long postId);

  // 특정 사용자의 공개 글
  Page<PostEntity> findByUserIdAndIsPublishedTrue(Long userId, Pageable pageable);

  // 최신순 조회
  Page<PostEntity> findAllByIsPublishedTrueOrderByCreatedAtDesc(Pageable pageable);

  // 트렌딩 (7일 이내 + 조회수 높은 순)
  @Query("SELECT p FROM PostEntity p " +
      "WHERE p.isPublished = true " +
      "AND p.createdAt >= :sevenDaysAgo " +
      "ORDER BY p.viewCount DESC, p.createdAt DESC")
  Page<PostEntity> findTrendingPosts(@Param("sevenDaysAgo") LocalDateTime sevenDaysAgo, Pageable pageable);

  // 피드 (팔로우한 사용자들의 글)
  @Query("SELECT p FROM PostEntity p " +
      "WHERE p.isPublished = true " +
      "AND p.userId IN :userIds " +
      "ORDER BY p.createdAt DESC")
  Page<PostEntity> findFeedPostsByUserIds(@Param("userIds") List<Long> userIds, Pageable pageable);

  // 검색
  @Query("SELECT p FROM PostEntity p " +
      "WHERE p.isPublished = true " +
      "AND (LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
      "OR LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
      "ORDER BY p.createdAt DESC")
  Page<PostEntity> searchPosts(@Param("keyword") String keyword, Pageable pageable);
}