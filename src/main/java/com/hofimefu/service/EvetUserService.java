package com.hofimefu.service;

import com.hofimefu.domain.EvetUser;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link EvetUser}.
 */
public interface EvetUserService {
    /**
     * Save a evetUser.
     *
     * @param evetUser the entity to save.
     * @return the persisted entity.
     */
    EvetUser save(EvetUser evetUser);

    /**
     * Updates a evetUser.
     *
     * @param evetUser the entity to update.
     * @return the persisted entity.
     */
    EvetUser update(EvetUser evetUser);

    /**
     * Partially updates a evetUser.
     *
     * @param evetUser the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EvetUser> partialUpdate(EvetUser evetUser);

    /**
     * Get all the evetUsers.
     *
     * @return the list of entities.
     */
    List<EvetUser> findAll();

    /**
     * Get all the evetUsers with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EvetUser> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" evetUser.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EvetUser> findOne(Long id);

    /**
     * Delete the "id" evetUser.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
