package daulspring.blogservice.repository;

import daulspring.blogservice.entity.LikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<LikeEntity, Long> {

  // 특정 사용자가 특정 게시글에 좋아요 눌렀는지 확인
  boolean existsByPostIdAndUserId(Long postId, Long userId);
  
  // 게시글의 좋아요 개수
  long countByPostId(Long postId);
  
  // 좋아요 삭제 (좋아요 취소할 때)
  void deleteByPostIdAndUserId(Long postId, Long userId);
}