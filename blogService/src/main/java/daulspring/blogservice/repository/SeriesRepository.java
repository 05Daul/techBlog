package daulspring.blogservice.repository;

import daulspring.blogservice.entity.SeriesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SeriesRepository extends JpaRepository<SeriesEntity, Long> {

  // 특정 사용자의 시리즈 목록 (최신순)
  List<SeriesEntity> findByUserIdOrderByCreatedAtDesc(Long userId);
}