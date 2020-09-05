package com.er706.backend.account;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 邮件验证表单
 */
@Data
@NoArgsConstructor
@Api("邮件验证表单")
public class EmailVerificationInput {

  @NotBlank
  @Email(regexp = AccountController.EMAIL_PATTERN)
  @ApiModelProperty("邮箱地址")
  private String email;
  @NotBlank
  @ApiModelProperty("验证码")
  private String verificationCode;

  @NotBlank
  @ApiModelProperty("对应用户ID")
  private Long uid;
}
