package com.er706.backend.signin;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SigninInput {

  @NotBlank
  @ApiModelProperty("用户名")
  private String username;
  @NotBlank
  @ApiModelProperty("密码")
  private String password;
}
