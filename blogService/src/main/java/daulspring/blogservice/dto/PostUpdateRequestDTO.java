package daulspring.blogservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostUpdateRequestDTO {

  @NotBlank(message = "제목을 입력해주세요.")
  @Size(max = 254, message = "제목은 254자를 초과할 수 없습니다.")
  private String title;

  @NotBlank(message = "내용을 입력해주세요.")
  private String content;

  private String thumbnail;

  private Boolean isPublished;

  private Long seriesId;

  private List<String> tags;
}