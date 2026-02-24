package daulspring.authservice.service;

import daulspring.authservice.dto.*;
import daulspring.authservice.entity.*;
import daulspring.authservice.grpc.UserGrpcClient;
import daulspring.authservice.jwt.JwtProvider;
import daulspring.authservice.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final AuthLocalAccountRepository localAccountRepository;
  private final UserGrpcClient userGrpcClient;
  private final JwtProvider jwtProvider;
  private final PasswordEncoder passwordEncoder;
  private final RedisTemplate<String, String> redisTemplate;

  private static final long REFRESH_EXPIRE_DAYS = 7;
  private static final String REFRESH_KEY_PREFIX = "refresh:";

  @Override
  public TokenResponseDTO signUp(SignUpRequestDTO dto) {

    // 1. Auth DB 중복 체크
    if (localAccountRepository.existsBySignId(dto.getSignId())) {
      throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
    }
    if (localAccountRepository.existsByEmail(dto.getEmail())) {
      throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
    }

    // 2. User 서비스에 프로필 생성 요청 (gRPC) → userId 반환
    Long userId = userGrpcClient.createProfile(
        dto.getEmail(),
        dto.getUserName(),
        dto.getNickname(),
        dto.getProfileImg()
    );

    // 3. 로컬 계정 저장
    AuthLocalAccount account = new AuthLocalAccount();
    account.setUserId(userId);
    account.setSignId(dto.getSignId());
    account.setEmail(dto.getEmail());
    account.setPassword(passwordEncoder.encode(dto.getPassword()));
    account.setRoleName(RoleStatus.USER);
    localAccountRepository.save(account);

    // 4. JWT 발급
    return issueToken(userId, RoleStatus.USER.name());
  }

  @Override
  public TokenResponseDTO login(LoginRequestDTO dto) {

    // 1. signId로 계정 조회
    AuthLocalAccount account = localAccountRepository.findBySignId(dto.getSignId())
        .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 올바르지 않습니다."));

    // 2. 비밀번호 검증
    if (!passwordEncoder.matches(dto.getPassword(), account.getPassword())) {
      throw new IllegalArgumentException("아이디 또는 비밀번호가 올바르지 않습니다.");
    }
    // 3. JWT 발급
    return issueToken(account.getUserId(), account.getRoleName().name());
  }

  @Override
  public TokenResponseDTO refresh(String refreshToken) {

    // 1. 토큰 유효성 검증
    if (!jwtProvider.isValid(refreshToken)) {
      throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
    }

    // 2. Redis에 저장된 토큰과 비교
    Long userId = jwtProvider.getUserId(refreshToken);
    String savedToken = redisTemplate.opsForValue().get(REFRESH_KEY_PREFIX + userId);

    if (savedToken == null || !savedToken.equals(refreshToken)) {
      throw new IllegalArgumentException("만료되었거나 이미 사용된 토큰입니다.");
    }

    // 3. 계정 조회 후 재발급
    AuthLocalAccount account = localAccountRepository.findByUserId(userId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

    return issueToken(userId, account.getRoleName().name());
  }

  @Override
  public void logout(Long userId) {
    // Redis에서 Refresh Token 삭제
    redisTemplate.delete(REFRESH_KEY_PREFIX + userId);
  }

  @Override
  public void changePassword(Long userId, String currentPassword, String newPassword) {

    AuthLocalAccount account = localAccountRepository.findByUserId(userId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

    if (!passwordEncoder.matches(currentPassword, account.getPassword())) {
      throw new IllegalArgumentException("현재 비밀번호가 올바르지 않습니다.");
    }

    account.setPassword(passwordEncoder.encode(newPassword));
  }

  // 공통 — 토큰 발급 + Redis 저장
  private TokenResponseDTO issueToken(Long userId, String role) {
    String accessToken = jwtProvider.createAccessToken(userId, role);
    String refreshToken = jwtProvider.createRefreshToken(userId);

    redisTemplate.opsForValue().set(
        REFRESH_KEY_PREFIX + userId,
        refreshToken,
        REFRESH_EXPIRE_DAYS,
        TimeUnit.DAYS
    );

    return new TokenResponseDTO(accessToken, refreshToken, userId, role);
  }
}