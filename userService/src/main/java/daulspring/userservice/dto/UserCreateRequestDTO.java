
package daulspring.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequestDTO {

  @Email
  @NotBlank(message = "이메일이 입력되지 않았습니다.")
  private String email;

  @NotBlank(message = "이름이 입력되지 않았습니다.")
  @Size(max = 20)
  private String userName;

  @NotBlank(message = "닉네임이 입력되지 않았습니다.")
  @Size(max = 10)
  private String nickname;

  private String profileImg; // 선택값 — null 허용

}
