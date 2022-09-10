package com.hofimefu.repository;

import com.hofimefu.domain.UserSchool;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserSchool entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserSchoolRepository extends JpaRepository<UserSchool, Long> {
    @Query("select userSchool from UserSchool userSchool where userSchool.user.login = ?#{principal.username}")
    List<UserSchool> findByUserIsCurrentUser();
}
