package com.er706.backend.profile;

import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EducationForm {

  private String school;
  private String major;
  private Date begin;
  private Date end;
  private String describtion;
}
