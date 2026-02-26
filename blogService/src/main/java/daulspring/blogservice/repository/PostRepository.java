package daulspring.blogservice.repository;

import daulspring.blogservice.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<PostEntity, Long> {

  Optional<PostEntity> findByPostId(Long postId);
  // 최신순 조회 && 공개글
  Page<PostEntity> findAllByIsPublishedTrueOrderByCreatedAtDesc(Pageable pageable);

  // 특정 사용자의 글 목록 && 공개글
  Page<PostEntity> findByUserIdAndIsPublishedTrueOrderByCreatedAtDesc(Long userId, Pageable pageable);

  // 조회수 증가
  ///  원자적 연산
  ///  DB는 이 쿼리를 한 번의 연산으로 처리.
  @Modifying
  @Query("UPDATE PostEntity p SET p.viewCount = p.viewCount + :count WHERE p.postId = :postId")
  int incrementViewCount(@Param("postId") Long postId, @Param("count") Long count);

}