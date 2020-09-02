package com.er706.backend.signup;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 注册表单
 */
@Data
public class SignupInput {

  @ApiModelProperty("邀请码")
  private String inviteCode;
  @NotBlank
  @ApiModelProperty("姓")
  private String lastName;
  @NotBlank
  @ApiModelProperty("名")
  private String firstName;
  @NotBlank
  @ApiModelProperty("手机号")
  private String phoneNumber;
  @NotBlank
  @ApiModelProperty("手机验证码")
  private String code;
  @NotBlank
  @ApiModelProperty("密码")
  private String password;
}
