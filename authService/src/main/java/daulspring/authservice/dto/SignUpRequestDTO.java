package daulspring.authservice.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequestDTO {

  @NotBlank(message = "아이디가 입력되지 않았습니다.")
  @Size(min = 4, max = 16)
  private String signId;

  @Email
  @NotBlank(message = "이메일이 입력되지 않았습니다.")
  private String email;

  @NotBlank(message = "비밀번호가 입력되지 않았습니다.")
  @Size(min = 8)
  @Pattern(
      regexp = "^(?=.*[!@#$%^&*(),.?\":{}|<>]).+$",
      message = "비밀번호에는 최소 하나의 특수문자가 포함되어야 합니다."
  )
  private String password;

  // 아래는 User 서비스로 전달할 프로필 데이터
  @NotBlank(message = "이름이 입력되지 않았습니다.")
  @Size(max = 20)
  private String userName;

  @NotBlank(message = "닉네임이 입력되지 않았습니다.")
  @Size(max = 10)
  private String nickname;

  private String profileImg;
}