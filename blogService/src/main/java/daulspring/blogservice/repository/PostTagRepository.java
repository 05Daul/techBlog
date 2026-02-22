package daulspring.blogservice.repository;

import daulspring.blogservice.entity.PostTagEntity;
import daulspring.blogservice.entity.PostTagId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

// PostTagRepository.java 수정
public interface PostTagRepository extends JpaRepository<PostTagEntity, PostTagId> {

  // 특정 게시글의 모든 태그
  List<PostTagEntity> findByPostTagId_PostId(Long postId);

  // 특정 게시글 + 특정 태그 존재 여부
  boolean existsByPostTagId_PostIdAndPostTagId_TagId(Long postId, Long tagId);

  // 특정 게시글의 모든 태그 삭제
  @Modifying
  @Transactional
  void deleteByPostTagId_PostId(Long postId);

  // 특정 게시글의 특정 태그 삭제
  @Modifying
  @Transactional
  void deleteByPostTagId_PostIdAndPostTagId_TagId(Long postId, Long tagId);
}