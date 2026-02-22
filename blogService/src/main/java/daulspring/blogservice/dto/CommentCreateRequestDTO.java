package daulspring.blogservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentCreateRequestDTO {

  @NotNull(message = "게시글 ID는 필수입니다.")
  private Long postId;

  private Long parentId;

  @NotBlank(message = "댓글 내용을 입력해주세요.")
  private String content;
}