package daulspring.blogservice.service;

import daulspring.blogservice.entity.LikeEntity;
import java.util.Optional;

public interface LikeService {

  void addLike(Long postId, Long userId);

 // 좋아요를 눌렀는 지 확인하는 메서드
  Optional<LikeEntity> findByPostIdAndUserId(Long postId, Long userId);
  // 좋아요 취소
  void deleteByPostIdAndUserSignId(Long postId, Long userId);

  // 좋아요의 총갯수
  long countByPostId(Long postId);

  boolean existsByPostIdAndUserSignId(Long postId, Long userId);

}

