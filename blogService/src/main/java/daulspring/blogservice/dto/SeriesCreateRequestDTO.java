package daulspring.blogservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeriesCreateRequestDTO {

  @NotBlank(message = "시리즈 이름을 입력해주세요.")
  @Size(max = 100, message = "시리즈 이름은 100자를 초과할 수 없습니다.")
  private String name;
}