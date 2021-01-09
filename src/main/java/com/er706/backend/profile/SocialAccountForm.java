package com.er706.backend.profile;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SocialAccountForm {

  private String platform;
  private String account;
  private Boolean privacy;
}
