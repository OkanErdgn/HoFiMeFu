package com.hofimefu.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hofimefu.IntegrationTest;
import com.hofimefu.domain.EvetUser;
import com.hofimefu.repository.EvetUserRepository;
import com.hofimefu.service.EvetUserService;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EvetUserResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class EvetUserResourceIT {

    private static final String ENTITY_API_URL = "/api/evet-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EvetUserRepository evetUserRepository;

    @Mock
    private EvetUserRepository evetUserRepositoryMock;

    @Mock
    private EvetUserService evetUserServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEvetUserMockMvc;

    private EvetUser evetUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EvetUser createEntity(EntityManager em) {
        EvetUser evetUser = new EvetUser();
        return evetUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EvetUser createUpdatedEntity(EntityManager em) {
        EvetUser evetUser = new EvetUser();
        return evetUser;
    }

    @BeforeEach
    public void initTest() {
        evetUser = createEntity(em);
    }

    @Test
    @Transactional
    void createEvetUser() throws Exception {
        int databaseSizeBeforeCreate = evetUserRepository.findAll().size();
        // Create the EvetUser
        restEvetUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(evetUser)))
            .andExpect(status().isCreated());

        // Validate the EvetUser in the database
        List<EvetUser> evetUserList = evetUserRepository.findAll();
        assertThat(evetUserList).hasSize(databaseSizeBeforeCreate + 1);
        EvetUser testEvetUser = evetUserList.get(evetUserList.size() - 1);
    }

    @Test
    @Transactional
    void createEvetUserWithExistingId() throws Exception {
        // Create the EvetUser with an existing ID
        evetUser.setId(1L);

        int databaseSizeBeforeCreate = evetUserRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEvetUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(evetUser)))
            .andExpect(status().isBadRequest());

        // Validate the EvetUser in the database
        List<EvetUser> evetUserList = evetUserRepository.findAll();
        assertThat(evetUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEvetUsers() throws Exception {
        // Initialize the database
        evetUserRepository.saveAndFlush(evetUser);

        // Get all the evetUserList
        restEvetUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(evetUser.getId().intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEvetUsersWithEagerRelationshipsIsEnabled() throws Exception {
        when(evetUserServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEvetUserMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(evetUserServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEvetUsersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(evetUserServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEvetUserMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(evetUserRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getEvetUser() throws Exception {
        // Initialize the database
        evetUserRepository.saveAndFlush(evetUser);

        // Get the evetUser
        restEvetUserMockMvc
            .perform(get(ENTITY_API_URL_ID, evetUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(evetUser.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingEvetUser() throws Exception {
        // Get the evetUser
        restEvetUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEvetUser() throws Exception {
        // Initialize the database
        evetUserRepository.saveAndFlush(evetUser);

        int databaseSizeBeforeUpdate = evetUserRepository.findAll().size();

        // Update the evetUser
        EvetUser updatedEvetUser = evetUserRepository.findById(evetUser.getId()).get();
        // Disconnect from session so that the updates on updatedEvetUser are not directly saved in db
        em.detach(updatedEvetUser);

        restEvetUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEvetUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEvetUser))
            )
            .andExpect(status().isOk());

        // Validate the EvetUser in the database
        List<EvetUser> evetUserList = evetUserRepository.findAll();
        assertThat(evetUserList).hasSize(databaseSizeBeforeUpdate);
        EvetUser testEvetUser = evetUserList.get(evetUserList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingEvetUser() throws Exception {
        int databaseSizeBeforeUpdate = evetUserRepository.findAll().size();
        evetUser.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEvetUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, evetUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(evetUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the EvetUser in the database
        List<EvetUser> evetUserList = evetUserRepository.findAll();
        assertThat(evetUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEvetUser() throws Exception {
        int databaseSizeBeforeUpdate = evetUserRepository.findAll().size();
        evetUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEvetUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(evetUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the EvetUser in the database
        List<EvetUser> evetUserList = evetUserRepository.findAll();
        assertThat(evetUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEvetUser() throws Exception {
        int databaseSizeBeforeUpdate = evetUserRepository.findAll().size();
        evetUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEvetUserMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(evetUser)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EvetUser in the database
        List<EvetUser> evetUserList = evetUserRepository.findAll();
        assertThat(evetUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEvetUserWithPatch() throws Exception {
        // Initialize the database
        evetUserRepository.saveAndFlush(evetUser);

        int databaseSizeBeforeUpdate = evetUserRepository.findAll().size();

        // Update the evetUser using partial update
        EvetUser partialUpdatedEvetUser = new EvetUser();
        partialUpdatedEvetUser.setId(evetUser.getId());

        restEvetUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEvetUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEvetUser))
            )
            .andExpect(status().isOk());

        // Validate the EvetUser in the database
        List<EvetUser> evetUserList = evetUserRepository.findAll();
        assertThat(evetUserList).hasSize(databaseSizeBeforeUpdate);
        EvetUser testEvetUser = evetUserList.get(evetUserList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateEvetUserWithPatch() throws Exception {
        // Initialize the database
        evetUserRepository.saveAndFlush(evetUser);

        int databaseSizeBeforeUpdate = evetUserRepository.findAll().size();

        // Update the evetUser using partial update
        EvetUser partialUpdatedEvetUser = new EvetUser();
        partialUpdatedEvetUser.setId(evetUser.getId());

        restEvetUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEvetUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEvetUser))
            )
            .andExpect(status().isOk());

        // Validate the EvetUser in the database
        List<EvetUser> evetUserList = evetUserRepository.findAll();
        assertThat(evetUserList).hasSize(databaseSizeBeforeUpdate);
        EvetUser testEvetUser = evetUserList.get(evetUserList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingEvetUser() throws Exception {
        int databaseSizeBeforeUpdate = evetUserRepository.findAll().size();
        evetUser.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEvetUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, evetUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(evetUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the EvetUser in the database
        List<EvetUser> evetUserList = evetUserRepository.findAll();
        assertThat(evetUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEvetUser() throws Exception {
        int databaseSizeBeforeUpdate = evetUserRepository.findAll().size();
        evetUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEvetUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(evetUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the EvetUser in the database
        List<EvetUser> evetUserList = evetUserRepository.findAll();
        assertThat(evetUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEvetUser() throws Exception {
        int databaseSizeBeforeUpdate = evetUserRepository.findAll().size();
        evetUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEvetUserMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(evetUser)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EvetUser in the database
        List<EvetUser> evetUserList = evetUserRepository.findAll();
        assertThat(evetUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEvetUser() throws Exception {
        // Initialize the database
        evetUserRepository.saveAndFlush(evetUser);

        int databaseSizeBeforeDelete = evetUserRepository.findAll().size();

        // Delete the evetUser
        restEvetUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, evetUser.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EvetUser> evetUserList = evetUserRepository.findAll();
        assertThat(evetUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
