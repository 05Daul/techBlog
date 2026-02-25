package daulspring.userservice.service;

import daulspring.userservice.dto.UserCreateRequestDTO;
import daulspring.userservice.entity.UserStatus;
import daulspring.userservice.entity.UsersEntity;
import daulspring.userservice.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userProfileRepository;

  @Override
  public void updateProfile(Long userId, String newProfileImg) {
    UsersEntity profile = userProfileRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

    profile.setProfileImg(newProfileImg);
  }

  @Override
  public void changeNickname(Long userId, String newNickname) {
    if (newNickname == null || newNickname.trim().isEmpty()) {
      throw new IllegalArgumentException("닉네임이 공백입니다.");
    }

    UsersEntity profile = userProfileRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

    if (!profile.getNickName().equals(newNickname)
        && userProfileRepository.existsByNickName(newNickname)) {
      throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
    }

    profile.setNickName(newNickname);
  }

  @Override
  public Long createProfile(UserCreateRequestDTO dto) {
    if (userProfileRepository.existsByEmail(dto.getEmail())) {
      throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
    }
    if (userProfileRepository.existsByNickName(dto.getNickname())) {
      throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
    }

    UsersEntity profile = new UsersEntity();
    profile.setEmail(dto.getEmail());
    profile.setUserName(dto.getUserName());
    profile.setNickName(dto.getNickname());
    profile.setProfileImg(dto.getProfileImg());
    profile.setStatus(UserStatus.ACTIVE);

    // 저장 후 Auth에서 사용할 userId를 반환
    return userProfileRepository.save(profile).getUserId();
  }

  @Override
  public boolean existsByNickname(String nickname) {
    return userProfileRepository.existsByNickName(nickname);
  }

  @Override
  public boolean existsByEmail(String email) {
    return userProfileRepository.existsByEmail(email);
  }

  @Override
  public UsersEntity findById(Long userId) {
    return userProfileRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
  }

  @Override
  public List<UsersEntity> findUsersByIds(List<Long> userIds) {
    return userProfileRepository.findAllById(userIds);
  }
}