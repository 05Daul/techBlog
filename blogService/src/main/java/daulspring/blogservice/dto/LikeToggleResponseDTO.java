package daulspring.blogservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeToggleResponseDTO {
  private Long postId;
  private String userId;
  private Boolean isLiked;
  private Long likeCount;
}