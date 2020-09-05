package com.er706.backend.account;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Api("修改密码表单")
public class ChangePasswordInput {

  @NotBlank
  @ApiModelProperty("新密码")
  private String password;
}
