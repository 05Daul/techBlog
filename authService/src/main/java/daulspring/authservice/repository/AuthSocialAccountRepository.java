package daulspring.authservice.repository;

import daulspring.authservice.entity.AuthSocialAccount;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthSocialAccountRepository extends JpaRepository<AuthSocialAccount, Long> {

  Optional<AuthSocialAccount> findByProviderAndProviderId(String provider, String providerId);

  boolean existsByProviderAndProviderId(String provider, String providerId);
}