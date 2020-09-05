package com.er706.backend.account;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Api("修改手机号表单")
public class ChangePhoneInput {

  @NotBlank
  @Pattern(regexp = AccountController.PHONE_PATTERN)
  @ApiModelProperty("新手机号")
  private String phoneNumber;

  @NotBlank
  @ApiModelProperty("验证码")
  private String code;
}
