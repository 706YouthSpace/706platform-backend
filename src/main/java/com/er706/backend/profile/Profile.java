package com.er706.backend.profile;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "profiles")
@Data
@NoArgsConstructor
public class Profile {

  @Id
  @Column(nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false)
  private String lastName;
  @Column(nullable = false)
  private String firstName;

  public Profile(String lastName, String firstName) {
    this.lastName = lastName;
    this.firstName = firstName;
  }
}
