package com.er706.backend.profile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobDAO extends JpaRepository<Job, Long> {

}