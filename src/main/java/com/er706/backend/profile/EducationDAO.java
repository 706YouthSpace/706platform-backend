package com.er706.backend.profile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EducationDAO extends JpaRepository<Education, Long> {

}
