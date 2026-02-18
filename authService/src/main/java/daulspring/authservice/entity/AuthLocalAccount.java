package daulspring.authservice.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthLocalAccount {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private Long userId;

  @Column(nullable = false, unique = true, length = 16)
  private String signId;

  @Column(nullable = false, unique = true, length = 50)
  private String email;

  @Column(nullable = false, length = 64)
  private String password;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private RoleStatus roleName = RoleStatus.USER;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;
}