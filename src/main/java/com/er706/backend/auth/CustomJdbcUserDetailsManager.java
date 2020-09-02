package com.er706.backend.auth;

import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;

@Service
public class CustomJdbcUserDetailsManager extends JdbcUserDetailsManager {

  @Autowired
  public CustomJdbcUserDetailsManager(DataSource dataSource) {
    super(dataSource);
    setUsersByUsernameQuery("select uid,account,password from accounts where account = ?");
    setAuthoritiesByUsernameQuery("select uid,authority from authorities where uid = ?");
  }

  @Override
  protected List<UserDetails> loadUsersByUsername(String username) {
    return getJdbcTemplate().query(getUsersByUsernameQuery(), new String[]{username},
        (rs, rowNum) -> {

          Long uid = rs.getLong(1);
          String userName = rs.getString(2);
          String password = rs.getString(3);

          boolean accLocked = false;
          boolean accExpired = false;
          boolean credsExpired = false;

          if (rs.getMetaData().getColumnCount() > 3) {
            //NOTE: acc_locked, acc_expired and creds_expired are also to be loaded
            accLocked = rs.getBoolean(4);
            accExpired = rs.getBoolean(5);
            credsExpired = rs.getBoolean(6);
          }
          return new Account(uid, userName, password, true, !accExpired, !credsExpired,
              !accLocked,
              AuthorityUtils.NO_AUTHORITIES);
        });
  }

  @Override
  public void createUser(UserDetails user) {
    Account account = (Account) user;
    getJdbcTemplate()
        .update("insert into accounts (uid, account, password) values (?,?,?)", ps -> {
          ps.setLong(1, account.getUid());
          ps.setString(2, account.getUsername());
          ps.setString(3, account.getPassword());
        });
    if (getEnableAuthorities()) {
      for (GrantedAuthority auth : user.getAuthorities()) {
        getJdbcTemplate()
            .update("insert into authorities (uid, authority) values (?,?)", account.getUid(),
                auth.getAuthority());
      }
    }
  }

  @Override
  protected List<GrantedAuthority> loadUserAuthorities(String username) {
    List<UserDetails> userDetails = loadUsersByUsername(username);
    Account account = (Account) userDetails.get(0);
    return getJdbcTemplate().query("select uid,authority from authorities where uid = ?",
        new Long[]{account.getUid()}, (rs, rowNum) -> {
          String roleName = "ROLE_" + rs.getString(2);

          return new SimpleGrantedAuthority(roleName);
        });
  }

  @Override
  protected UserDetails createUserDetails(String uid, UserDetails userFromUserQuery,
      List<GrantedAuthority> combinedAuthorities) {
    Account account = (Account) userFromUserQuery;
    return new Account(account.getUid(), account.getUsername(), account.getPassword(),
        account.isEnabled(), account.isAccountNonExpired(),
        account.isCredentialsNonExpired(), account.isAccountNonLocked(),
        combinedAuthorities);
  }
}
