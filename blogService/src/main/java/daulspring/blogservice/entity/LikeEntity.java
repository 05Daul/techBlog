package daulspring.blogservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "likes", uniqueConstraints = {
    // 중복 좋아요 -> 두 컬럼의 조합이 유일해야 함. 그래서 둘다 외래키로 가지고 있음.
    @UniqueConstraint(columnNames = {"post_id", "user_id"})
})
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long likeId;

  @Column(name = "post_id", nullable = false)
  private Long postId;

  @Column(name = "userId", nullable = false)
  private String userId;

  @Column(nullable = false, updatable = false)
  @CreationTimestamp
  private LocalDateTime createdAt;

  @Column(nullable = true)
  @UpdateTimestamp
  private LocalDateTime updatedAt;

}