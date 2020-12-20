package com.er706.backend.signin;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SigninByPhoneInput {

  @NotBlank
  @ApiModelProperty("手机号")
  private String phoneNumber;
  @NotBlank
  @ApiModelProperty("手机验证码")
  private String code;
}
