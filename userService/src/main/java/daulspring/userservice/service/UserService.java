package daulspring.userservice.service;

import daulspring.userservice.dto.UserCreateRequestDTO;
import daulspring.userservice.entity.UsersEntity;
import java.util.List;

public interface UserService {

  void updateProfile(Long userId, String newProfileImg);

  void changeNickname(Long userId, String newNickname);

  Long createProfile(UserCreateRequestDTO dto);

  // 조회
  UsersEntity findById(Long userId);

  List<UsersEntity> findUsersByIds(List<Long> userIds);

  boolean existsByNickname(String nickname);

  boolean existsByEmail(String email);

}