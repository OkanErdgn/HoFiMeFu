package com.hofimefu.service;

import com.hofimefu.domain.FriendStatus;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link FriendStatus}.
 */
public interface FriendStatusService {
    /**
     * Save a friendStatus.
     *
     * @param friendStatus the entity to save.
     * @return the persisted entity.
     */
    FriendStatus save(FriendStatus friendStatus);

    /**
     * Updates a friendStatus.
     *
     * @param friendStatus the entity to update.
     * @return the persisted entity.
     */
    FriendStatus update(FriendStatus friendStatus);

    /**
     * Partially updates a friendStatus.
     *
     * @param friendStatus the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FriendStatus> partialUpdate(FriendStatus friendStatus);

    /**
     * Get all the friendStatuses.
     *
     * @return the list of entities.
     */
    List<FriendStatus> findAll();

    /**
     * Get the "id" friendStatus.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FriendStatus> findOne(Long id);

    /**
     * Delete the "id" friendStatus.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
