package com.hofimefu.repository;

import com.hofimefu.domain.Event;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Event entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("select event from Event event where event.createdBy.login = ?#{principal.username}")
    List<Event> findByCreatedByIsCurrentUser();
}
