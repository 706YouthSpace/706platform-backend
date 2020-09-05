package com.er706.backend.signin;

import com.er706.backend.JsonResult;
import com.er706.backend.auth.CustomJdbcUserDetailsManager;
import com.er706.backend.auth.JwtTokenUtil;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SigninController {

  @Autowired
  private AuthenticationManager authenticationManager;
  @Autowired
  private JwtTokenUtil jwtTokenUtil;
  @Autowired
  private CustomJdbcUserDetailsManager userDetailsService;

  @RequestMapping(value = "/signin", method = RequestMethod.POST)
  public JsonResult<String> signin(@Valid @RequestBody SigninInput signinInput)
      throws Exception {
    authenticate(signinInput.getUsername(), signinInput.getPassword());
    UserDetails userDetails = userDetailsService
        .loadUserByUsername(signinInput.getUsername());
    String token = jwtTokenUtil.generateToken(userDetails);
    return JsonResult.ok(token);
  }

  private void authenticate(String username, String password) throws Exception {
    try {
      authenticationManager
          .authenticate(new UsernamePasswordAuthenticationToken(username, password));
    } catch (DisabledException e) {
      throw new Exception("USER_DISABLED", e);
    } catch (BadCredentialsException e) {
      throw new Exception("INVALID_CREDENTIALS", e);
    }
  }

}
