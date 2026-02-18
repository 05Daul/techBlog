package daulspring.authservice.repository;

import daulspring.authservice.entity.AuthLocalAccount;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthLocalAccountRepository extends JpaRepository<AuthLocalAccount, Long> {

  Optional<AuthLocalAccount> findBySignId(String signId);
  Optional<AuthLocalAccount> findByEmail(String email);
  Optional<AuthLocalAccount> findByUserId(Long userId);

  boolean existsBySignId(String signId);
  boolean existsByEmail(String email);
}