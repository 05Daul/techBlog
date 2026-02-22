package daulspring.blogservice.repository;

import daulspring.blogservice.entity.SeriesEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeriesRepository extends JpaRepository<SeriesEntity, Long> {

  // 특정 사용자의 시리즈 목록
  List<SeriesEntity> findByUserIdOrderByCreatedAtDesc(Long userId);
}