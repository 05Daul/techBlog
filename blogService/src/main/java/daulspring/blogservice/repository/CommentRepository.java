package daulspring.blogservice.repository;

import daulspring.blogservice.entity.CommentEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

  // 최상위 댓글만 조회 (부모가 없는 댓글)
  @Query("SELECT c FROM CommentEntity c " +
      "WHERE c.postId = :postId " +
      "AND c.parentComment IS NULL " +
      "ORDER BY c.createdAt ASC")
  List<CommentEntity> findTopLevelCommentsByPostId(@Param("postId") Long postId);

  // 특정 댓글의 대댓글 조회
  List<CommentEntity> findByParentComment_IdOrderByCreatedAtAsc(Long parentId);

  // 게시글의 댓글 개수
  Long countByPostIdAndIsDeletedFalse(Long postId);
}