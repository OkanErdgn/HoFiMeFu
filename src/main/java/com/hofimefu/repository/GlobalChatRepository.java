package com.hofimefu.repository;

import com.hofimefu.domain.GlobalChat;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the GlobalChat entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GlobalChatRepository extends JpaRepository<GlobalChat, Long> {
    @Query("select globalChat from GlobalChat globalChat where globalChat.user.login = ?#{principal.username}")
    List<GlobalChat> findByUserIsCurrentUser();
}
