package com.hofimefu.web.rest;

import static com.hofimefu.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hofimefu.IntegrationTest;
import com.hofimefu.domain.FriendStatus;
import com.hofimefu.domain.enumeration.FriendshipStatus;
import com.hofimefu.repository.FriendStatusRepository;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link FriendStatusResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FriendStatusResourceIT {

    private static final ZonedDateTime DEFAULT_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_LAST_CHANGED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_CHANGED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final FriendshipStatus DEFAULT_STATUS = FriendshipStatus.ACTIVE;
    private static final FriendshipStatus UPDATED_STATUS = FriendshipStatus.BLOCKED;

    private static final String ENTITY_API_URL = "/api/friend-statuses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FriendStatusRepository friendStatusRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFriendStatusMockMvc;

    private FriendStatus friendStatus;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FriendStatus createEntity(EntityManager em) {
        FriendStatus friendStatus = new FriendStatus().created(DEFAULT_CREATED).lastChanged(DEFAULT_LAST_CHANGED).status(DEFAULT_STATUS);
        return friendStatus;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FriendStatus createUpdatedEntity(EntityManager em) {
        FriendStatus friendStatus = new FriendStatus().created(UPDATED_CREATED).lastChanged(UPDATED_LAST_CHANGED).status(UPDATED_STATUS);
        return friendStatus;
    }

    @BeforeEach
    public void initTest() {
        friendStatus = createEntity(em);
    }

    @Test
    @Transactional
    void createFriendStatus() throws Exception {
        int databaseSizeBeforeCreate = friendStatusRepository.findAll().size();
        // Create the FriendStatus
        restFriendStatusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(friendStatus)))
            .andExpect(status().isCreated());

        // Validate the FriendStatus in the database
        List<FriendStatus> friendStatusList = friendStatusRepository.findAll();
        assertThat(friendStatusList).hasSize(databaseSizeBeforeCreate + 1);
        FriendStatus testFriendStatus = friendStatusList.get(friendStatusList.size() - 1);
        assertThat(testFriendStatus.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testFriendStatus.getLastChanged()).isEqualTo(DEFAULT_LAST_CHANGED);
        assertThat(testFriendStatus.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createFriendStatusWithExistingId() throws Exception {
        // Create the FriendStatus with an existing ID
        friendStatus.setId(1L);

        int databaseSizeBeforeCreate = friendStatusRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFriendStatusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(friendStatus)))
            .andExpect(status().isBadRequest());

        // Validate the FriendStatus in the database
        List<FriendStatus> friendStatusList = friendStatusRepository.findAll();
        assertThat(friendStatusList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFriendStatuses() throws Exception {
        // Initialize the database
        friendStatusRepository.saveAndFlush(friendStatus);

        // Get all the friendStatusList
        restFriendStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(friendStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(sameInstant(DEFAULT_CREATED))))
            .andExpect(jsonPath("$.[*].lastChanged").value(hasItem(sameInstant(DEFAULT_LAST_CHANGED))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getFriendStatus() throws Exception {
        // Initialize the database
        friendStatusRepository.saveAndFlush(friendStatus);

        // Get the friendStatus
        restFriendStatusMockMvc
            .perform(get(ENTITY_API_URL_ID, friendStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(friendStatus.getId().intValue()))
            .andExpect(jsonPath("$.created").value(sameInstant(DEFAULT_CREATED)))
            .andExpect(jsonPath("$.lastChanged").value(sameInstant(DEFAULT_LAST_CHANGED)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingFriendStatus() throws Exception {
        // Get the friendStatus
        restFriendStatusMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFriendStatus() throws Exception {
        // Initialize the database
        friendStatusRepository.saveAndFlush(friendStatus);

        int databaseSizeBeforeUpdate = friendStatusRepository.findAll().size();

        // Update the friendStatus
        FriendStatus updatedFriendStatus = friendStatusRepository.findById(friendStatus.getId()).get();
        // Disconnect from session so that the updates on updatedFriendStatus are not directly saved in db
        em.detach(updatedFriendStatus);
        updatedFriendStatus.created(UPDATED_CREATED).lastChanged(UPDATED_LAST_CHANGED).status(UPDATED_STATUS);

        restFriendStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFriendStatus.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFriendStatus))
            )
            .andExpect(status().isOk());

        // Validate the FriendStatus in the database
        List<FriendStatus> friendStatusList = friendStatusRepository.findAll();
        assertThat(friendStatusList).hasSize(databaseSizeBeforeUpdate);
        FriendStatus testFriendStatus = friendStatusList.get(friendStatusList.size() - 1);
        assertThat(testFriendStatus.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testFriendStatus.getLastChanged()).isEqualTo(UPDATED_LAST_CHANGED);
        assertThat(testFriendStatus.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingFriendStatus() throws Exception {
        int databaseSizeBeforeUpdate = friendStatusRepository.findAll().size();
        friendStatus.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFriendStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, friendStatus.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(friendStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the FriendStatus in the database
        List<FriendStatus> friendStatusList = friendStatusRepository.findAll();
        assertThat(friendStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFriendStatus() throws Exception {
        int databaseSizeBeforeUpdate = friendStatusRepository.findAll().size();
        friendStatus.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFriendStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(friendStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the FriendStatus in the database
        List<FriendStatus> friendStatusList = friendStatusRepository.findAll();
        assertThat(friendStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFriendStatus() throws Exception {
        int databaseSizeBeforeUpdate = friendStatusRepository.findAll().size();
        friendStatus.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFriendStatusMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(friendStatus)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FriendStatus in the database
        List<FriendStatus> friendStatusList = friendStatusRepository.findAll();
        assertThat(friendStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFriendStatusWithPatch() throws Exception {
        // Initialize the database
        friendStatusRepository.saveAndFlush(friendStatus);

        int databaseSizeBeforeUpdate = friendStatusRepository.findAll().size();

        // Update the friendStatus using partial update
        FriendStatus partialUpdatedFriendStatus = new FriendStatus();
        partialUpdatedFriendStatus.setId(friendStatus.getId());

        partialUpdatedFriendStatus.lastChanged(UPDATED_LAST_CHANGED);

        restFriendStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFriendStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFriendStatus))
            )
            .andExpect(status().isOk());

        // Validate the FriendStatus in the database
        List<FriendStatus> friendStatusList = friendStatusRepository.findAll();
        assertThat(friendStatusList).hasSize(databaseSizeBeforeUpdate);
        FriendStatus testFriendStatus = friendStatusList.get(friendStatusList.size() - 1);
        assertThat(testFriendStatus.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testFriendStatus.getLastChanged()).isEqualTo(UPDATED_LAST_CHANGED);
        assertThat(testFriendStatus.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateFriendStatusWithPatch() throws Exception {
        // Initialize the database
        friendStatusRepository.saveAndFlush(friendStatus);

        int databaseSizeBeforeUpdate = friendStatusRepository.findAll().size();

        // Update the friendStatus using partial update
        FriendStatus partialUpdatedFriendStatus = new FriendStatus();
        partialUpdatedFriendStatus.setId(friendStatus.getId());

        partialUpdatedFriendStatus.created(UPDATED_CREATED).lastChanged(UPDATED_LAST_CHANGED).status(UPDATED_STATUS);

        restFriendStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFriendStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFriendStatus))
            )
            .andExpect(status().isOk());

        // Validate the FriendStatus in the database
        List<FriendStatus> friendStatusList = friendStatusRepository.findAll();
        assertThat(friendStatusList).hasSize(databaseSizeBeforeUpdate);
        FriendStatus testFriendStatus = friendStatusList.get(friendStatusList.size() - 1);
        assertThat(testFriendStatus.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testFriendStatus.getLastChanged()).isEqualTo(UPDATED_LAST_CHANGED);
        assertThat(testFriendStatus.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingFriendStatus() throws Exception {
        int databaseSizeBeforeUpdate = friendStatusRepository.findAll().size();
        friendStatus.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFriendStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, friendStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(friendStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the FriendStatus in the database
        List<FriendStatus> friendStatusList = friendStatusRepository.findAll();
        assertThat(friendStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFriendStatus() throws Exception {
        int databaseSizeBeforeUpdate = friendStatusRepository.findAll().size();
        friendStatus.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFriendStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(friendStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the FriendStatus in the database
        List<FriendStatus> friendStatusList = friendStatusRepository.findAll();
        assertThat(friendStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFriendStatus() throws Exception {
        int databaseSizeBeforeUpdate = friendStatusRepository.findAll().size();
        friendStatus.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFriendStatusMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(friendStatus))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FriendStatus in the database
        List<FriendStatus> friendStatusList = friendStatusRepository.findAll();
        assertThat(friendStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFriendStatus() throws Exception {
        // Initialize the database
        friendStatusRepository.saveAndFlush(friendStatus);

        int databaseSizeBeforeDelete = friendStatusRepository.findAll().size();

        // Delete the friendStatus
        restFriendStatusMockMvc
            .perform(delete(ENTITY_API_URL_ID, friendStatus.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FriendStatus> friendStatusList = friendStatusRepository.findAll();
        assertThat(friendStatusList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
