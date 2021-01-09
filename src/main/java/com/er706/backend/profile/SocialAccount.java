package com.er706.backend.profile;

import com.er706.backend.auth.Account;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@Entity(name = "social_accounts")
@Data
@NoArgsConstructor
public class SocialAccount {

  @Id
  @Column(nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne
  @Getter(AccessLevel.NONE)
  private Profile profile;

  private String platform;
  private String account;
  private boolean privacy;

  public String getAccount() {
    if (!this.privacy) {
      return this.account;
    } else {
      SecurityContext securityContext = SecurityContextHolder.getContext();
      Authentication authentication = securityContext.getAuthentication();
      if (authentication != null && authentication.getPrincipal() instanceof Account) {
        Account account = (Account) authentication.getPrincipal();
        if (account.getUid() == this.profile.getId()) {
          return this.account;
        }
      }
      return "******";
    }
  }
}
