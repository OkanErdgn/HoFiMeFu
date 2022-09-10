package com.hofimefu.service;

import com.hofimefu.domain.UserConfig;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link UserConfig}.
 */
public interface UserConfigService {
    /**
     * Save a userConfig.
     *
     * @param userConfig the entity to save.
     * @return the persisted entity.
     */
    UserConfig save(UserConfig userConfig);

    /**
     * Updates a userConfig.
     *
     * @param userConfig the entity to update.
     * @return the persisted entity.
     */
    UserConfig update(UserConfig userConfig);

    /**
     * Partially updates a userConfig.
     *
     * @param userConfig the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UserConfig> partialUpdate(UserConfig userConfig);

    /**
     * Get all the userConfigs.
     *
     * @return the list of entities.
     */
    List<UserConfig> findAll();

    /**
     * Get the "id" userConfig.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UserConfig> findOne(Long id);

    /**
     * Delete the "id" userConfig.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
