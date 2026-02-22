package daulspring.blogservice.repository;

import daulspring.blogservice.entity.LikeEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<LikeEntity, Long> {

  // 좋아요 존재 여부
  boolean existsByPostIdAndUserId(Long postId, Long userId);

  // 특정 좋아요 조회
  Optional<LikeEntity> findByPostIdAndUserId(Long postId, Long userId);

  // 게시글의 좋아요 개수
  long countByPostId(Long postId);

  // 좋아요 삭제
  void deleteByPostIdAndUserId(Long postId,Long userId);
}