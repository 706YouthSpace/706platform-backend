package com.er706.backend.profile;

import com.er706.backend.JsonResult;
import com.er706.backend.auth.Account;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController()
@RequestMapping("/profile")
public class ProfileController {

  @Autowired
  ProfileDAO profileDAO;

  @GetMapping("/me")
  @Secured("ROLE_USER")
  public JsonResult<Profile> me(@ApiIgnore Authentication currentUser) {
    Account account = (Account) currentUser.getPrincipal();
    Optional<Profile> profile = profileDAO.findById(account.getUid());
    return JsonResult.ok(profile.get());
  }
}
