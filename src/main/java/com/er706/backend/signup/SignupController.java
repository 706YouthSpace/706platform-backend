package com.er706.backend.signup;

import com.er706.backend.JsonResult;
import com.er706.backend.auth.Account;
import com.er706.backend.auth.CustomJdbcUserDetailsManager;
import com.er706.backend.captcha.SmsService;
import com.er706.backend.profile.Profile;
import com.er706.backend.profile.ProfileDAO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api("注册接口")
public class SignupController {

  @Autowired
  CustomJdbcUserDetailsManager userDetailsManager;

  @Autowired
  SmsService smsService;

  @Autowired
  ProfileDAO profileDAO;

  @PostMapping("/signup")
  @ApiOperation("注册接口")
  public JsonResult signup(@Valid @RequestBody SignupInput signupInput) {

    String phoneNumber = signupInput.getPhoneNumber();
    try {
      userDetailsManager.loadUserByUsername(phoneNumber);
      return JsonResult.err("phoneNumber", "duplicate");
    } catch (UsernameNotFoundException e) {
      String code = signupInput.getCode();
      if (smsService.verifyCode(phoneNumber, code)) {

        Profile profile = profileDAO
            .save(new Profile(signupInput.getLastName(), signupInput.getFirstName()));
        userDetailsManager
            .createUser(
                new Account(profile.getId(), phoneNumber, signupInput.getPassword(),
                    Collections.singleton(new SimpleGrantedAuthority("USER"))));
        return JsonResult.ok();
      } else {
        return JsonResult.err("code", "error");
      }
    }
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public JsonResult handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    HashMap<String, List<String>> errs = new HashMap<String, List<String>>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      errs.put(fieldName, Arrays.asList(error.getCodes()));
    });
    return JsonResult.err(errs);
  }

}
