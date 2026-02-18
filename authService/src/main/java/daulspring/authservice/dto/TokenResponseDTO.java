package daulspring.authservice.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponseDTO {

  private String accessToken;
  private String refreshToken;
  private Long userId;
  private String role;
}