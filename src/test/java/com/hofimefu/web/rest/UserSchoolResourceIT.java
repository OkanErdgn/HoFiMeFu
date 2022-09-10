package com.hofimefu.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hofimefu.IntegrationTest;
import com.hofimefu.domain.UserSchool;
import com.hofimefu.domain.enumeration.SchoolStatus;
import com.hofimefu.repository.UserSchoolRepository;
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
 * Integration tests for the {@link UserSchoolResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserSchoolResourceIT {

    private static final SchoolStatus DEFAULT_STATUS = SchoolStatus.ACTIVE;
    private static final SchoolStatus UPDATED_STATUS = SchoolStatus.INACTIVE;

    private static final String ENTITY_API_URL = "/api/user-schools";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserSchoolRepository userSchoolRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserSchoolMockMvc;

    private UserSchool userSchool;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserSchool createEntity(EntityManager em) {
        UserSchool userSchool = new UserSchool().status(DEFAULT_STATUS);
        return userSchool;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserSchool createUpdatedEntity(EntityManager em) {
        UserSchool userSchool = new UserSchool().status(UPDATED_STATUS);
        return userSchool;
    }

    @BeforeEach
    public void initTest() {
        userSchool = createEntity(em);
    }

    @Test
    @Transactional
    void createUserSchool() throws Exception {
        int databaseSizeBeforeCreate = userSchoolRepository.findAll().size();
        // Create the UserSchool
        restUserSchoolMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userSchool)))
            .andExpect(status().isCreated());

        // Validate the UserSchool in the database
        List<UserSchool> userSchoolList = userSchoolRepository.findAll();
        assertThat(userSchoolList).hasSize(databaseSizeBeforeCreate + 1);
        UserSchool testUserSchool = userSchoolList.get(userSchoolList.size() - 1);
        assertThat(testUserSchool.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createUserSchoolWithExistingId() throws Exception {
        // Create the UserSchool with an existing ID
        userSchool.setId(1L);

        int databaseSizeBeforeCreate = userSchoolRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserSchoolMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userSchool)))
            .andExpect(status().isBadRequest());

        // Validate the UserSchool in the database
        List<UserSchool> userSchoolList = userSchoolRepository.findAll();
        assertThat(userSchoolList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUserSchools() throws Exception {
        // Initialize the database
        userSchoolRepository.saveAndFlush(userSchool);

        // Get all the userSchoolList
        restUserSchoolMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userSchool.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getUserSchool() throws Exception {
        // Initialize the database
        userSchoolRepository.saveAndFlush(userSchool);

        // Get the userSchool
        restUserSchoolMockMvc
            .perform(get(ENTITY_API_URL_ID, userSchool.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userSchool.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingUserSchool() throws Exception {
        // Get the userSchool
        restUserSchoolMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUserSchool() throws Exception {
        // Initialize the database
        userSchoolRepository.saveAndFlush(userSchool);

        int databaseSizeBeforeUpdate = userSchoolRepository.findAll().size();

        // Update the userSchool
        UserSchool updatedUserSchool = userSchoolRepository.findById(userSchool.getId()).get();
        // Disconnect from session so that the updates on updatedUserSchool are not directly saved in db
        em.detach(updatedUserSchool);
        updatedUserSchool.status(UPDATED_STATUS);

        restUserSchoolMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUserSchool.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedUserSchool))
            )
            .andExpect(status().isOk());

        // Validate the UserSchool in the database
        List<UserSchool> userSchoolList = userSchoolRepository.findAll();
        assertThat(userSchoolList).hasSize(databaseSizeBeforeUpdate);
        UserSchool testUserSchool = userSchoolList.get(userSchoolList.size() - 1);
        assertThat(testUserSchool.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingUserSchool() throws Exception {
        int databaseSizeBeforeUpdate = userSchoolRepository.findAll().size();
        userSchool.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserSchoolMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userSchool.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userSchool))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserSchool in the database
        List<UserSchool> userSchoolList = userSchoolRepository.findAll();
        assertThat(userSchoolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserSchool() throws Exception {
        int databaseSizeBeforeUpdate = userSchoolRepository.findAll().size();
        userSchool.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserSchoolMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userSchool))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserSchool in the database
        List<UserSchool> userSchoolList = userSchoolRepository.findAll();
        assertThat(userSchoolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserSchool() throws Exception {
        int databaseSizeBeforeUpdate = userSchoolRepository.findAll().size();
        userSchool.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserSchoolMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userSchool)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserSchool in the database
        List<UserSchool> userSchoolList = userSchoolRepository.findAll();
        assertThat(userSchoolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserSchoolWithPatch() throws Exception {
        // Initialize the database
        userSchoolRepository.saveAndFlush(userSchool);

        int databaseSizeBeforeUpdate = userSchoolRepository.findAll().size();

        // Update the userSchool using partial update
        UserSchool partialUpdatedUserSchool = new UserSchool();
        partialUpdatedUserSchool.setId(userSchool.getId());

        restUserSchoolMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserSchool.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserSchool))
            )
            .andExpect(status().isOk());

        // Validate the UserSchool in the database
        List<UserSchool> userSchoolList = userSchoolRepository.findAll();
        assertThat(userSchoolList).hasSize(databaseSizeBeforeUpdate);
        UserSchool testUserSchool = userSchoolList.get(userSchoolList.size() - 1);
        assertThat(testUserSchool.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateUserSchoolWithPatch() throws Exception {
        // Initialize the database
        userSchoolRepository.saveAndFlush(userSchool);

        int databaseSizeBeforeUpdate = userSchoolRepository.findAll().size();

        // Update the userSchool using partial update
        UserSchool partialUpdatedUserSchool = new UserSchool();
        partialUpdatedUserSchool.setId(userSchool.getId());

        partialUpdatedUserSchool.status(UPDATED_STATUS);

        restUserSchoolMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserSchool.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserSchool))
            )
            .andExpect(status().isOk());

        // Validate the UserSchool in the database
        List<UserSchool> userSchoolList = userSchoolRepository.findAll();
        assertThat(userSchoolList).hasSize(databaseSizeBeforeUpdate);
        UserSchool testUserSchool = userSchoolList.get(userSchoolList.size() - 1);
        assertThat(testUserSchool.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingUserSchool() throws Exception {
        int databaseSizeBeforeUpdate = userSchoolRepository.findAll().size();
        userSchool.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserSchoolMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userSchool.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userSchool))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserSchool in the database
        List<UserSchool> userSchoolList = userSchoolRepository.findAll();
        assertThat(userSchoolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserSchool() throws Exception {
        int databaseSizeBeforeUpdate = userSchoolRepository.findAll().size();
        userSchool.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserSchoolMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userSchool))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserSchool in the database
        List<UserSchool> userSchoolList = userSchoolRepository.findAll();
        assertThat(userSchoolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserSchool() throws Exception {
        int databaseSizeBeforeUpdate = userSchoolRepository.findAll().size();
        userSchool.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserSchoolMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(userSchool))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserSchool in the database
        List<UserSchool> userSchoolList = userSchoolRepository.findAll();
        assertThat(userSchoolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserSchool() throws Exception {
        // Initialize the database
        userSchoolRepository.saveAndFlush(userSchool);

        int databaseSizeBeforeDelete = userSchoolRepository.findAll().size();

        // Delete the userSchool
        restUserSchoolMockMvc
            .perform(delete(ENTITY_API_URL_ID, userSchool.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserSchool> userSchoolList = userSchoolRepository.findAll();
        assertThat(userSchoolList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
