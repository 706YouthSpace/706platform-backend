package com.er706.backend.signin;

import com.er706.backend.JsonResult;
import com.er706.backend.auth.CustomJdbcUserDetailsManager;
import com.er706.backend.auth.JwtTokenUtil;
import com.er706.backend.captcha.SmsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api("登陆接口")
public class SigninController {

  @Autowired
  CustomJdbcUserDetailsManager userDetailsManager;

  @Autowired
  SmsService smsService;
  @Autowired
  private AuthenticationManager authenticationManager;
  @Autowired
  private JwtTokenUtil jwtTokenUtil;
  @Autowired
  private CustomJdbcUserDetailsManager userDetailsService;

  @RequestMapping(value = "/signin/phone", method = RequestMethod.POST)
  @ApiOperation("手机号验证码登陆接口")
  public JsonResult<String> signinPhone(@Valid @RequestBody SigninByPhoneInput signinInput)
      throws Exception {
    String phoneNumber = signinInput.getPhoneNumber();
    String code = signinInput.getCode();
    if (smsService.verifyCode(phoneNumber, code)) {
      try {
        UserDetails userDetails = userDetailsManager.loadUserByUsername(phoneNumber);
        String token = jwtTokenUtil.generateToken(userDetails);
        return JsonResult.ok(token);
      } catch (UsernameNotFoundException e) {
        return JsonResult.err("phoneNumber", "not_exist");
      }
    } else {
      return JsonResult.err("code", "error");
    }
  }

  @RequestMapping(value = "/signin", method = RequestMethod.POST)
  @ApiOperation("密码登陆接口")
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
