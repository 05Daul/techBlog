package daulspring.blogservice.service;

import daulspring.blogservice.entity.LikeEntity;
import daulspring.blogservice.repository.LikeRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

  private final LikeRepository likeRepository;

  @Override
  public long countByPostId(Long postId) {
    return likeRepository.countByPostId(postId);
  }
  @Override
  public void addLike(Long postId, Long userId) {
    if (likeRepository.existsByPostIdAndUserId(postId, userId)) {
      throw new IllegalStateException("이미 좋아요를 눌렀습니다.");
    }

    LikeEntity like = new LikeEntity();
    like.setPostId(postId);
    like.setUserId(userId);
    likeRepository.save(like);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<LikeEntity> findByPostIdAndUserId(Long postId, Long userId) {
    return likeRepository.findByPostIdAndUserId(postId, userId);
  }

  @Override
  public void deleteByPostIdAndUserSignId(Long postId, Long userId) {
    likeRepository.deleteByPostIdAndUserId(postId, userId);

  }

  @Override
  public boolean existsByPostIdAndUserSignId(Long postId, Long userId){
    return likeRepository.existsByPostIdAndUserId(postId, userId);
  }
}
