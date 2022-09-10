package com.hofimefu.service;

import com.hofimefu.domain.Friend;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Friend}.
 */
public interface FriendService {
    /**
     * Save a friend.
     *
     * @param friend the entity to save.
     * @return the persisted entity.
     */
    Friend save(Friend friend);

    /**
     * Updates a friend.
     *
     * @param friend the entity to update.
     * @return the persisted entity.
     */
    Friend update(Friend friend);

    /**
     * Partially updates a friend.
     *
     * @param friend the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Friend> partialUpdate(Friend friend);

    /**
     * Get all the friends.
     *
     * @return the list of entities.
     */
    List<Friend> findAll();

    /**
     * Get the "id" friend.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Friend> findOne(Long id);

    /**
     * Delete the "id" friend.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
