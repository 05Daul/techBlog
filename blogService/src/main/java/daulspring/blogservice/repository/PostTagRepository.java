package daulspring.blogservice.repository;

import daulspring.blogservice.entity.PostTagEntity;
import daulspring.blogservice.entity.PostTagId;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostTagRepository extends JpaRepository<PostTagEntity, PostTagId> {

  // 특정 게시글의 모든 태그 조회
  List<PostTagEntity> findByPostTagId_PostId(Long postId);
}
