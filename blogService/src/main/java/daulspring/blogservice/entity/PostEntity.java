package daulspring.blogservice.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "blog_posts")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "user_id", nullable = false)
  private Long userId; // user_profiles.userid 참조

  @Column(name = "series_id", nullable = true)
  private Long seriesId; // blog_series.id 참조

  @Column(nullable = false, length = 254)
  private String title;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String content;

  @Column(nullable = true)
  private String thumbnail;

  @Column(name = "is_published", nullable = false)
  private Boolean isPublished = true;

  @Column(name = "is_crawled", nullable = false)
  private Boolean isCrawled = false;

  @Column(name = "origin_url", nullable = true, length = 500)
  private String originUrl;

  @Column(name = "view_count", nullable = false)
  private Integer viewCount = 0;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = true)
  private LocalDateTime updatedAt;
}