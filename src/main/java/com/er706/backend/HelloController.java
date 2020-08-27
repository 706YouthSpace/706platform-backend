package com.er706.backend;

import com.er706.backend.auth.Account;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
public class HelloController {

  @GetMapping("/hello")
  @Secured("ROLE_USER")
  public String hello(@ApiIgnore Authentication currentUser) {
    Account account = (Account) currentUser.getPrincipal();
    return "world " + account;
  }
}
