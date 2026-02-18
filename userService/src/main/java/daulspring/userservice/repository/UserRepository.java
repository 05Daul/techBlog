package daulspring.userservice.repository;

import daulspring.userservice.entity.UsersEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UsersEntity, Long> {
  Optional<UsersEntity> findByEmail(String email);

  boolean existsByNickName(String nickName);
  boolean existsByEmail(String email);

}
