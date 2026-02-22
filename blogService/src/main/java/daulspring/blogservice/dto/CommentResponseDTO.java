package daulspring.blogservice.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponseDTO {

  private Long id;
  private Long postId;
  private Long userId;
  private Long parentId;
  private String content;
  private Boolean isDeleted;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  
  private List<CommentResponseDTO> childComments;  // 대댓글 목록
  private Integer childCount;  // 대댓글 개수
}