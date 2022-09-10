package com.hofimefu.web.rest;

import static com.hofimefu.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hofimefu.IntegrationTest;
import com.hofimefu.domain.GlobalChat;
import com.hofimefu.repository.GlobalChatRepository;
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
 * Integration tests for the {@link GlobalChatResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GlobalChatResourceIT {

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/global-chats";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GlobalChatRepository globalChatRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGlobalChatMockMvc;

    private GlobalChat globalChat;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GlobalChat createEntity(EntityManager em) {
        GlobalChat globalChat = new GlobalChat().message(DEFAULT_MESSAGE).created(DEFAULT_CREATED);
        return globalChat;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GlobalChat createUpdatedEntity(EntityManager em) {
        GlobalChat globalChat = new GlobalChat().message(UPDATED_MESSAGE).created(UPDATED_CREATED);
        return globalChat;
    }

    @BeforeEach
    public void initTest() {
        globalChat = createEntity(em);
    }

    @Test
    @Transactional
    void createGlobalChat() throws Exception {
        int databaseSizeBeforeCreate = globalChatRepository.findAll().size();
        // Create the GlobalChat
        restGlobalChatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(globalChat)))
            .andExpect(status().isCreated());

        // Validate the GlobalChat in the database
        List<GlobalChat> globalChatList = globalChatRepository.findAll();
        assertThat(globalChatList).hasSize(databaseSizeBeforeCreate + 1);
        GlobalChat testGlobalChat = globalChatList.get(globalChatList.size() - 1);
        assertThat(testGlobalChat.getMessage()).isEqualTo(DEFAULT_MESSAGE);
        assertThat(testGlobalChat.getCreated()).isEqualTo(DEFAULT_CREATED);
    }

    @Test
    @Transactional
    void createGlobalChatWithExistingId() throws Exception {
        // Create the GlobalChat with an existing ID
        globalChat.setId(1L);

        int databaseSizeBeforeCreate = globalChatRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGlobalChatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(globalChat)))
            .andExpect(status().isBadRequest());

        // Validate the GlobalChat in the database
        List<GlobalChat> globalChatList = globalChatRepository.findAll();
        assertThat(globalChatList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllGlobalChats() throws Exception {
        // Initialize the database
        globalChatRepository.saveAndFlush(globalChat);

        // Get all the globalChatList
        restGlobalChatMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(globalChat.getId().intValue())))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(sameInstant(DEFAULT_CREATED))));
    }

    @Test
    @Transactional
    void getGlobalChat() throws Exception {
        // Initialize the database
        globalChatRepository.saveAndFlush(globalChat);

        // Get the globalChat
        restGlobalChatMockMvc
            .perform(get(ENTITY_API_URL_ID, globalChat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(globalChat.getId().intValue()))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE))
            .andExpect(jsonPath("$.created").value(sameInstant(DEFAULT_CREATED)));
    }

    @Test
    @Transactional
    void getNonExistingGlobalChat() throws Exception {
        // Get the globalChat
        restGlobalChatMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewGlobalChat() throws Exception {
        // Initialize the database
        globalChatRepository.saveAndFlush(globalChat);

        int databaseSizeBeforeUpdate = globalChatRepository.findAll().size();

        // Update the globalChat
        GlobalChat updatedGlobalChat = globalChatRepository.findById(globalChat.getId()).get();
        // Disconnect from session so that the updates on updatedGlobalChat are not directly saved in db
        em.detach(updatedGlobalChat);
        updatedGlobalChat.message(UPDATED_MESSAGE).created(UPDATED_CREATED);

        restGlobalChatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedGlobalChat.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedGlobalChat))
            )
            .andExpect(status().isOk());

        // Validate the GlobalChat in the database
        List<GlobalChat> globalChatList = globalChatRepository.findAll();
        assertThat(globalChatList).hasSize(databaseSizeBeforeUpdate);
        GlobalChat testGlobalChat = globalChatList.get(globalChatList.size() - 1);
        assertThat(testGlobalChat.getMessage()).isEqualTo(UPDATED_MESSAGE);
        assertThat(testGlobalChat.getCreated()).isEqualTo(UPDATED_CREATED);
    }

    @Test
    @Transactional
    void putNonExistingGlobalChat() throws Exception {
        int databaseSizeBeforeUpdate = globalChatRepository.findAll().size();
        globalChat.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGlobalChatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, globalChat.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(globalChat))
            )
            .andExpect(status().isBadRequest());

        // Validate the GlobalChat in the database
        List<GlobalChat> globalChatList = globalChatRepository.findAll();
        assertThat(globalChatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGlobalChat() throws Exception {
        int databaseSizeBeforeUpdate = globalChatRepository.findAll().size();
        globalChat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGlobalChatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(globalChat))
            )
            .andExpect(status().isBadRequest());

        // Validate the GlobalChat in the database
        List<GlobalChat> globalChatList = globalChatRepository.findAll();
        assertThat(globalChatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGlobalChat() throws Exception {
        int databaseSizeBeforeUpdate = globalChatRepository.findAll().size();
        globalChat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGlobalChatMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(globalChat)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the GlobalChat in the database
        List<GlobalChat> globalChatList = globalChatRepository.findAll();
        assertThat(globalChatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGlobalChatWithPatch() throws Exception {
        // Initialize the database
        globalChatRepository.saveAndFlush(globalChat);

        int databaseSizeBeforeUpdate = globalChatRepository.findAll().size();

        // Update the globalChat using partial update
        GlobalChat partialUpdatedGlobalChat = new GlobalChat();
        partialUpdatedGlobalChat.setId(globalChat.getId());

        restGlobalChatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGlobalChat.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGlobalChat))
            )
            .andExpect(status().isOk());

        // Validate the GlobalChat in the database
        List<GlobalChat> globalChatList = globalChatRepository.findAll();
        assertThat(globalChatList).hasSize(databaseSizeBeforeUpdate);
        GlobalChat testGlobalChat = globalChatList.get(globalChatList.size() - 1);
        assertThat(testGlobalChat.getMessage()).isEqualTo(DEFAULT_MESSAGE);
        assertThat(testGlobalChat.getCreated()).isEqualTo(DEFAULT_CREATED);
    }

    @Test
    @Transactional
    void fullUpdateGlobalChatWithPatch() throws Exception {
        // Initialize the database
        globalChatRepository.saveAndFlush(globalChat);

        int databaseSizeBeforeUpdate = globalChatRepository.findAll().size();

        // Update the globalChat using partial update
        GlobalChat partialUpdatedGlobalChat = new GlobalChat();
        partialUpdatedGlobalChat.setId(globalChat.getId());

        partialUpdatedGlobalChat.message(UPDATED_MESSAGE).created(UPDATED_CREATED);

        restGlobalChatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGlobalChat.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGlobalChat))
            )
            .andExpect(status().isOk());

        // Validate the GlobalChat in the database
        List<GlobalChat> globalChatList = globalChatRepository.findAll();
        assertThat(globalChatList).hasSize(databaseSizeBeforeUpdate);
        GlobalChat testGlobalChat = globalChatList.get(globalChatList.size() - 1);
        assertThat(testGlobalChat.getMessage()).isEqualTo(UPDATED_MESSAGE);
        assertThat(testGlobalChat.getCreated()).isEqualTo(UPDATED_CREATED);
    }

    @Test
    @Transactional
    void patchNonExistingGlobalChat() throws Exception {
        int databaseSizeBeforeUpdate = globalChatRepository.findAll().size();
        globalChat.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGlobalChatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, globalChat.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(globalChat))
            )
            .andExpect(status().isBadRequest());

        // Validate the GlobalChat in the database
        List<GlobalChat> globalChatList = globalChatRepository.findAll();
        assertThat(globalChatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGlobalChat() throws Exception {
        int databaseSizeBeforeUpdate = globalChatRepository.findAll().size();
        globalChat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGlobalChatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(globalChat))
            )
            .andExpect(status().isBadRequest());

        // Validate the GlobalChat in the database
        List<GlobalChat> globalChatList = globalChatRepository.findAll();
        assertThat(globalChatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGlobalChat() throws Exception {
        int databaseSizeBeforeUpdate = globalChatRepository.findAll().size();
        globalChat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGlobalChatMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(globalChat))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GlobalChat in the database
        List<GlobalChat> globalChatList = globalChatRepository.findAll();
        assertThat(globalChatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGlobalChat() throws Exception {
        // Initialize the database
        globalChatRepository.saveAndFlush(globalChat);

        int databaseSizeBeforeDelete = globalChatRepository.findAll().size();

        // Delete the globalChat
        restGlobalChatMockMvc
            .perform(delete(ENTITY_API_URL_ID, globalChat.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GlobalChat> globalChatList = globalChatRepository.findAll();
        assertThat(globalChatList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
