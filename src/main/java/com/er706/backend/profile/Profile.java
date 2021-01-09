package com.er706.backend.profile;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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

  @Column
  private String avatar;
  @Column
  private String background;
  @Column
  private String description;
  @Column
  private String bio;
  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Job> jobs;
  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Education> educations;

  @Column
  private String country;
  @Column
  private String province;
  @Column
  private String city;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  private List<SocialAccount> socials;

  public Profile(String lastName, String firstName) {
    this.lastName = lastName;
    this.firstName = firstName;
  }
}
