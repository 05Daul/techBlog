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

  Optional<PostEntity> findByPostId(Long postId);

  @Modifying
  @Query("UPDATE PostEntity p SET p.viewCount = p.viewCount + 1 WHERE p.postId = :postId")
  int incrementViewCount(@Param("postId") Long postId);

  Page<PostEntity> findByUserIdAndIsPublishedTrue(Long userId, Pageable pageable);

  Page<PostEntity> findAllByIsPublishedTrueOrderByCreatedAtDesc(Pageable pageable);

  @Query("SELECT p FROM PostEntity p " +
      "WHERE p.isPublished = true " +
      "AND p.createdAt >= :sevenDaysAgo " +
      "ORDER BY p.viewCount DESC, p.createdAt DESC")
  Page<PostEntity> findTrendingPosts(@Param("sevenDaysAgo") LocalDateTime sevenDaysAgo, Pageable pageable);

  @Query("SELECT p FROM PostEntity p " +
      "WHERE p.isPublished = true " +
      "AND p.userId IN :userIds " +
      "ORDER BY p.createdAt DESC")
  Page<PostEntity> findFeedPostsByUserIds(@Param("userIds") List<Long> userIds, Pageable pageable);

  @Query("SELECT p FROM PostEntity p " +
      "WHERE p.isPublished = true " +
      "AND (LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
      "OR LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
      "ORDER BY p.createdAt DESC")
  Page<PostEntity> searchPosts(@Param("keyword") String keyword, Pageable pageable);
}