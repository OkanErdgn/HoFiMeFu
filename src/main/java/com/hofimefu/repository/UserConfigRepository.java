package com.hofimefu.repository;

import com.hofimefu.domain.UserConfig;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserConfig entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserConfigRepository extends JpaRepository<UserConfig, Long> {}
