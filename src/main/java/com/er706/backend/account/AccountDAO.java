package com.er706.backend.account;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountDAO extends JpaRepository<AccountModel, Long> {

  Optional<AccountModel> findByAccount(String account);

  List<AccountModel> findAllByUid(Long uid);
}
