package daulspring.blogservice.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "blog_bookmarks",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "post_id"}))
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "user_id", nullable = false)
  private Long userId; // user_profiles.userid 참조

  @Column(name = "post_id", nullable = false)
  private Long postId;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;
}