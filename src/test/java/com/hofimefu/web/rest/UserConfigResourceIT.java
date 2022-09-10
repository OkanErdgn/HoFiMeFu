package com.hofimefu.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hofimefu.IntegrationTest;
import com.hofimefu.domain.UserConfig;
import com.hofimefu.domain.enumeration.Language;
import com.hofimefu.repository.UserConfigRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link UserConfigResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserConfigResourceIT {

    private static final Boolean DEFAULT_SHARE_LOCATION = false;
    private static final Boolean UPDATED_SHARE_LOCATION = true;

    private static final Language DEFAULT_LANGUAGE = Language.FRENCH;
    private static final Language UPDATED_LANGUAGE = Language.ENGLISH;

    private static final String ENTITY_API_URL = "/api/user-configs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserConfigRepository userConfigRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserConfigMockMvc;

    private UserConfig userConfig;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserConfig createEntity(EntityManager em) {
        UserConfig userConfig = new UserConfig().shareLocation(DEFAULT_SHARE_LOCATION).language(DEFAULT_LANGUAGE);
        return userConfig;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserConfig createUpdatedEntity(EntityManager em) {
        UserConfig userConfig = new UserConfig().shareLocation(UPDATED_SHARE_LOCATION).language(UPDATED_LANGUAGE);
        return userConfig;
    }

    @BeforeEach
    public void initTest() {
        userConfig = createEntity(em);
    }

    @Test
    @Transactional
    void createUserConfig() throws Exception {
        int databaseSizeBeforeCreate = userConfigRepository.findAll().size();
        // Create the UserConfig
        restUserConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userConfig)))
            .andExpect(status().isCreated());

        // Validate the UserConfig in the database
        List<UserConfig> userConfigList = userConfigRepository.findAll();
        assertThat(userConfigList).hasSize(databaseSizeBeforeCreate + 1);
        UserConfig testUserConfig = userConfigList.get(userConfigList.size() - 1);
        assertThat(testUserConfig.getShareLocation()).isEqualTo(DEFAULT_SHARE_LOCATION);
        assertThat(testUserConfig.getLanguage()).isEqualTo(DEFAULT_LANGUAGE);
    }

    @Test
    @Transactional
    void createUserConfigWithExistingId() throws Exception {
        // Create the UserConfig with an existing ID
        userConfig.setId(1L);

        int databaseSizeBeforeCreate = userConfigRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userConfig)))
            .andExpect(status().isBadRequest());

        // Validate the UserConfig in the database
        List<UserConfig> userConfigList = userConfigRepository.findAll();
        assertThat(userConfigList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUserConfigs() throws Exception {
        // Initialize the database
        userConfigRepository.saveAndFlush(userConfig);

        // Get all the userConfigList
        restUserConfigMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].shareLocation").value(hasItem(DEFAULT_SHARE_LOCATION.booleanValue())))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())));
    }

    @Test
    @Transactional
    void getUserConfig() throws Exception {
        // Initialize the database
        userConfigRepository.saveAndFlush(userConfig);

        // Get the userConfig
        restUserConfigMockMvc
            .perform(get(ENTITY_API_URL_ID, userConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userConfig.getId().intValue()))
            .andExpect(jsonPath("$.shareLocation").value(DEFAULT_SHARE_LOCATION.booleanValue()))
            .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingUserConfig() throws Exception {
        // Get the userConfig
        restUserConfigMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUserConfig() throws Exception {
        // Initialize the database
        userConfigRepository.saveAndFlush(userConfig);

        int databaseSizeBeforeUpdate = userConfigRepository.findAll().size();

        // Update the userConfig
        UserConfig updatedUserConfig = userConfigRepository.findById(userConfig.getId()).get();
        // Disconnect from session so that the updates on updatedUserConfig are not directly saved in db
        em.detach(updatedUserConfig);
        updatedUserConfig.shareLocation(UPDATED_SHARE_LOCATION).language(UPDATED_LANGUAGE);

        restUserConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUserConfig.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedUserConfig))
            )
            .andExpect(status().isOk());

        // Validate the UserConfig in the database
        List<UserConfig> userConfigList = userConfigRepository.findAll();
        assertThat(userConfigList).hasSize(databaseSizeBeforeUpdate);
        UserConfig testUserConfig = userConfigList.get(userConfigList.size() - 1);
        assertThat(testUserConfig.getShareLocation()).isEqualTo(UPDATED_SHARE_LOCATION);
        assertThat(testUserConfig.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void putNonExistingUserConfig() throws Exception {
        int databaseSizeBeforeUpdate = userConfigRepository.findAll().size();
        userConfig.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userConfig.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userConfig))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserConfig in the database
        List<UserConfig> userConfigList = userConfigRepository.findAll();
        assertThat(userConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserConfig() throws Exception {
        int databaseSizeBeforeUpdate = userConfigRepository.findAll().size();
        userConfig.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userConfig))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserConfig in the database
        List<UserConfig> userConfigList = userConfigRepository.findAll();
        assertThat(userConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserConfig() throws Exception {
        int databaseSizeBeforeUpdate = userConfigRepository.findAll().size();
        userConfig.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserConfigMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userConfig)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserConfig in the database
        List<UserConfig> userConfigList = userConfigRepository.findAll();
        assertThat(userConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserConfigWithPatch() throws Exception {
        // Initialize the database
        userConfigRepository.saveAndFlush(userConfig);

        int databaseSizeBeforeUpdate = userConfigRepository.findAll().size();

        // Update the userConfig using partial update
        UserConfig partialUpdatedUserConfig = new UserConfig();
        partialUpdatedUserConfig.setId(userConfig.getId());

        restUserConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserConfig.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserConfig))
            )
            .andExpect(status().isOk());

        // Validate the UserConfig in the database
        List<UserConfig> userConfigList = userConfigRepository.findAll();
        assertThat(userConfigList).hasSize(databaseSizeBeforeUpdate);
        UserConfig testUserConfig = userConfigList.get(userConfigList.size() - 1);
        assertThat(testUserConfig.getShareLocation()).isEqualTo(DEFAULT_SHARE_LOCATION);
        assertThat(testUserConfig.getLanguage()).isEqualTo(DEFAULT_LANGUAGE);
    }

    @Test
    @Transactional
    void fullUpdateUserConfigWithPatch() throws Exception {
        // Initialize the database
        userConfigRepository.saveAndFlush(userConfig);

        int databaseSizeBeforeUpdate = userConfigRepository.findAll().size();

        // Update the userConfig using partial update
        UserConfig partialUpdatedUserConfig = new UserConfig();
        partialUpdatedUserConfig.setId(userConfig.getId());

        partialUpdatedUserConfig.shareLocation(UPDATED_SHARE_LOCATION).language(UPDATED_LANGUAGE);

        restUserConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserConfig.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserConfig))
            )
            .andExpect(status().isOk());

        // Validate the UserConfig in the database
        List<UserConfig> userConfigList = userConfigRepository.findAll();
        assertThat(userConfigList).hasSize(databaseSizeBeforeUpdate);
        UserConfig testUserConfig = userConfigList.get(userConfigList.size() - 1);
        assertThat(testUserConfig.getShareLocation()).isEqualTo(UPDATED_SHARE_LOCATION);
        assertThat(testUserConfig.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void patchNonExistingUserConfig() throws Exception {
        int databaseSizeBeforeUpdate = userConfigRepository.findAll().size();
        userConfig.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userConfig.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userConfig))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserConfig in the database
        List<UserConfig> userConfigList = userConfigRepository.findAll();
        assertThat(userConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserConfig() throws Exception {
        int databaseSizeBeforeUpdate = userConfigRepository.findAll().size();
        userConfig.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userConfig))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserConfig in the database
        List<UserConfig> userConfigList = userConfigRepository.findAll();
        assertThat(userConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserConfig() throws Exception {
        int databaseSizeBeforeUpdate = userConfigRepository.findAll().size();
        userConfig.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserConfigMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(userConfig))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserConfig in the database
        List<UserConfig> userConfigList = userConfigRepository.findAll();
        assertThat(userConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserConfig() throws Exception {
        // Initialize the database
        userConfigRepository.saveAndFlush(userConfig);

        int databaseSizeBeforeDelete = userConfigRepository.findAll().size();

        // Delete the userConfig
        restUserConfigMockMvc
            .perform(delete(ENTITY_API_URL_ID, userConfig.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserConfig> userConfigList = userConfigRepository.findAll();
        assertThat(userConfigList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
