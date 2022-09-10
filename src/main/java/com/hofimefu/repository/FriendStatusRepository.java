package com.hofimefu.repository;

import com.hofimefu.domain.FriendStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FriendStatus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FriendStatusRepository extends JpaRepository<FriendStatus, Long> {}
