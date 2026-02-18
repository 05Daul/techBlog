package daulspring.authservice.service;

import daulspring.authservice.dto.*;

public interface AuthService {

  TokenResponseDTO signUp(SignUpRequestDTO dto);

  TokenResponseDTO login(LoginRequestDTO dto);

  TokenResponseDTO refresh(String refreshToken);

  void logout(Long userId);

  void changePassword(Long userId, String currentPassword, String newPassword);
}