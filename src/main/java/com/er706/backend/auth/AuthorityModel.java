package com.er706.backend.auth;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.Data;

@Entity(name = "authorities")
@Data
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"uid", "authority"}))
public class AuthorityModel {

  @Id
  @Column(name = "uid")
  private Long uid;

  @Column(nullable = false, name = "authority")
  private String authority;
}
