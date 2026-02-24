package daulspring.authservice.controller;

import daulspring.authservice.dto.*;
import daulspring.authservice.service.AuthService;
import daulspring.commonsecurity.jwt.JwtProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;
  private final JwtProvider jwtProvider;

  // 회원가입
  @PostMapping("/signup")
  public ResponseEntity<TokenResponseDTO> signUp(
      @Valid @RequestBody SignUpRequestDTO dto) {
    return ResponseEntity.ok(authService.signUp(dto));
  }

  // 로그인
  @PostMapping("/login")
  public ResponseEntity<TokenResponseDTO> login(
      @Valid @RequestBody LoginRequestDTO dto) {
    return ResponseEntity.ok(authService.login(dto));
  }

  // 토큰 재발급
  @PostMapping("/refresh")
  public ResponseEntity<TokenResponseDTO> refresh(
      @RequestHeader("Refresh-Token") String refreshToken) {
    return ResponseEntity.ok(authService.refresh(refreshToken));
  }

  // 로그아웃
  @PostMapping("/logout")
  public ResponseEntity<Void> logout(
      @RequestHeader("Authorization") String bearerToken) {
    Long userId = jwtProvider.getUserId(resolveToken(bearerToken));
    authService.logout(userId);
    return ResponseEntity.ok().build();
  }

  // 비밀번호 변경
  @PatchMapping("/password")
  public ResponseEntity<Void> changePassword(
      @RequestHeader("Authorization") String bearerToken,
      @Valid @RequestBody ChangePasswordRequestDTO dto) {
    Long userId = jwtProvider.getUserId(resolveToken(bearerToken));
    authService.changePassword(userId, dto.getCurrentPassword(), dto.getNewPassword());
    return ResponseEntity.ok().build();
  }

  // "Bearer {token}" → "{token}" 파싱
  private String resolveToken(String bearerToken) {
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    throw new IllegalArgumentException("유효하지 않은 토큰 형식입니다.");
  }
}