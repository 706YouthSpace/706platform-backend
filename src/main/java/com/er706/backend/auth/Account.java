package com.er706.backend.auth;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 * 这个对象是用在验证环节的
 */
public class Account extends User {

  private Long uid;

  public Account(Long uid, String username, String password,
      Collection<? extends GrantedAuthority> authorities) {
    super(username, password, authorities);
    this.uid = uid;
  }

  public Account(Long uid, String username, String password, boolean enabled,
      boolean accountNonExpired,
      boolean credentialsNonExpired, boolean accountNonLocked,
      Collection<? extends GrantedAuthority> authorities) {
    super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked,
        authorities);
    this.uid = uid;
  }

  public Long getUid() {
    return this.uid;
  }
}
