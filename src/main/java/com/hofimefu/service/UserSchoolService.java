package com.hofimefu.service;

import com.hofimefu.domain.UserSchool;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link UserSchool}.
 */
public interface UserSchoolService {
    /**
     * Save a userSchool.
     *
     * @param userSchool the entity to save.
     * @return the persisted entity.
     */
    UserSchool save(UserSchool userSchool);

    /**
     * Updates a userSchool.
     *
     * @param userSchool the entity to update.
     * @return the persisted entity.
     */
    UserSchool update(UserSchool userSchool);

    /**
     * Partially updates a userSchool.
     *
     * @param userSchool the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UserSchool> partialUpdate(UserSchool userSchool);

    /**
     * Get all the userSchools.
     *
     * @return the list of entities.
     */
    List<UserSchool> findAll();

    /**
     * Get the "id" userSchool.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UserSchool> findOne(Long id);

    /**
     * Delete the "id" userSchool.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
