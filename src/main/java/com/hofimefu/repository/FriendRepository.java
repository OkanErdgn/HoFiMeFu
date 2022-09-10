package com.hofimefu.repository;

import com.hofimefu.domain.Friend;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Friend entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {
    @Query("select friend from Friend friend where friend.user1.login = ?#{principal.username}")
    List<Friend> findByUser1IsCurrentUser();

    @Query("select friend from Friend friend where friend.user2.login = ?#{principal.username}")
    List<Friend> findByUser2IsCurrentUser();
}
