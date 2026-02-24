package daulspring.blogservice.repository;

import daulspring.blogservice.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PostRepository extends JpaRepository<PostEntity, Long> {

  Optional<PostEntity> findByPostId(Long postId);
}