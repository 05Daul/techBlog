package daulspring.blogservice.repository;

import daulspring.blogservice.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

  // 특정 게시글의 최상위 댓글만 조회 (대댓글 제외)
  @Query("SELECT c FROM CommentEntity c " +
         "WHERE c.postId = :postId " +
         "AND c.parentComment IS NULL " +
         "ORDER BY c.createdAt ASC")
  List<CommentEntity> findTopLevelCommentsByPostId(@Param("postId") Long postId);
  
  // 특정 댓글의 대댓글 조회
  List<CommentEntity> findByParentComment_CommentIdOrderByCreatedAtAsc(Long parentId);
  
  // 게시글의 댓글 개수 (삭제 안 된 것만)
  Long countByPostIdAndIsDeletedFalse(Long postId);
}