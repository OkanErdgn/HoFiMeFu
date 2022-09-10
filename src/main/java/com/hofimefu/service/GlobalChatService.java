package com.hofimefu.service;

import com.hofimefu.domain.GlobalChat;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link GlobalChat}.
 */
public interface GlobalChatService {
    /**
     * Save a globalChat.
     *
     * @param globalChat the entity to save.
     * @return the persisted entity.
     */
    GlobalChat save(GlobalChat globalChat);

    /**
     * Updates a globalChat.
     *
     * @param globalChat the entity to update.
     * @return the persisted entity.
     */
    GlobalChat update(GlobalChat globalChat);

    /**
     * Partially updates a globalChat.
     *
     * @param globalChat the entity to update partially.
     * @return the persisted entity.
     */
    Optional<GlobalChat> partialUpdate(GlobalChat globalChat);

    /**
     * Get all the globalChats.
     *
     * @return the list of entities.
     */
    List<GlobalChat> findAll();

    /**
     * Get the "id" globalChat.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<GlobalChat> findOne(Long id);

    /**
     * Delete the "id" globalChat.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
