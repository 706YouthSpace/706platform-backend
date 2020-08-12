package com.er706.backend.index;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndexActivityDAO extends JpaRepository<IndexActivity, Long> {

}
