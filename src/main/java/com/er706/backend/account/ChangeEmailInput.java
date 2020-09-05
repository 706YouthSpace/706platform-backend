package com.er706.backend.account;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Api("修改邮箱表单")
public class ChangeEmailInput {

  @NotBlank
  @Email(regexp = AccountController.EMAIL_PATTERN)
  @ApiModelProperty("新邮箱")
  private String email;
}
