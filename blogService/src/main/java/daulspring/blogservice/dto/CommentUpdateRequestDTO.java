package daulspring.blogservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentUpdateRequestDTO {

  @NotBlank
  private  Long commentId;

  @NotBlank(message = "댓글 내용을 입력해주세요.")
  private String content;
}