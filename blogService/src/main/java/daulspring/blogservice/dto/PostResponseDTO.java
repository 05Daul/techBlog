package daulspring.blogservice.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponseDTO {

  private Long postId;
  private Long userId;
  private Long seriesId;
  private String title;
  private String content;
  private String thumbnail;
  private Boolean isPublished;
  private Boolean isCrawled;
  private String originUrl;
  private Integer viewCount;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}