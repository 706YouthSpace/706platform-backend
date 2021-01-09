package com.er706.backend.profile;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProfileForm {

  private String lastName;

  private String firstName;

  private String avatar;

  private String background;

  private String description;

  private String bio;

  private String country;

  private String province;

  private String city;
}
