package com.er706.backend.auth;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;

/**
 * 这个对象仅用于数据库创建和查询
 */

@Entity(name = "accounts")
@Data
public class AccountModel {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Long uid;

  @Column(nullable = false, unique = true)
  private String account;
  @Column(nullable = false, length = 500)
  private String password;

}