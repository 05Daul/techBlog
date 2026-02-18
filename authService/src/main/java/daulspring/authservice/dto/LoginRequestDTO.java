package daulspring.authservice.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO {

  @NotBlank(message = "아이디가 입력되지 않았습니다.")
  private String signId;

  @NotBlank(message = "비밀번호가 입력되지 않았습니다.")
  private String password;
}