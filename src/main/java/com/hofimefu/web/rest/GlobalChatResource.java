package com.hofimefu.web.rest;

import com.hofimefu.domain.GlobalChat;
import com.hofimefu.repository.GlobalChatRepository;
import com.hofimefu.service.GlobalChatService;
import com.hofimefu.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.hofimefu.domain.GlobalChat}.
 */
@RestController
@RequestMapping("/api")
public class GlobalChatResource {

    private final Logger log = LoggerFactory.getLogger(GlobalChatResource.class);

    private static final String ENTITY_NAME = "globalChat";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GlobalChatService globalChatService;

    private final GlobalChatRepository globalChatRepository;

    public GlobalChatResource(GlobalChatService globalChatService, GlobalChatRepository globalChatRepository) {
        this.globalChatService = globalChatService;
        this.globalChatRepository = globalChatRepository;
    }

    /**
     * {@code POST  /global-chats} : Create a new globalChat.
     *
     * @param globalChat the globalChat to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new globalChat, or with status {@code 400 (Bad Request)} if the globalChat has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/global-chats")
    public ResponseEntity<GlobalChat> createGlobalChat(@RequestBody GlobalChat globalChat) throws URISyntaxException {
        log.debug("REST request to save GlobalChat : {}", globalChat);
        if (globalChat.getId() != null) {
            throw new BadRequestAlertException("A new globalChat cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GlobalChat result = globalChatService.save(globalChat);
        return ResponseEntity
            .created(new URI("/api/global-chats/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /global-chats/:id} : Updates an existing globalChat.
     *
     * @param id the id of the globalChat to save.
     * @param globalChat the globalChat to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated globalChat,
     * or with status {@code 400 (Bad Request)} if the globalChat is not valid,
     * or with status {@code 500 (Internal Server Error)} if the globalChat couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/global-chats/{id}")
    public ResponseEntity<GlobalChat> updateGlobalChat(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody GlobalChat globalChat
    ) throws URISyntaxException {
        log.debug("REST request to update GlobalChat : {}, {}", id, globalChat);
        if (globalChat.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, globalChat.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!globalChatRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        GlobalChat result = globalChatService.update(globalChat);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, globalChat.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /global-chats/:id} : Partial updates given fields of an existing globalChat, field will ignore if it is null
     *
     * @param id the id of the globalChat to save.
     * @param globalChat the globalChat to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated globalChat,
     * or with status {@code 400 (Bad Request)} if the globalChat is not valid,
     * or with status {@code 404 (Not Found)} if the globalChat is not found,
     * or with status {@code 500 (Internal Server Error)} if the globalChat couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/global-chats/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GlobalChat> partialUpdateGlobalChat(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody GlobalChat globalChat
    ) throws URISyntaxException {
        log.debug("REST request to partial update GlobalChat partially : {}, {}", id, globalChat);
        if (globalChat.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, globalChat.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!globalChatRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GlobalChat> result = globalChatService.partialUpdate(globalChat);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, globalChat.getId().toString())
        );
    }

    /**
     * {@code GET  /global-chats} : get all the globalChats.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of globalChats in body.
     */
    @GetMapping("/global-chats")
    public List<GlobalChat> getAllGlobalChats() {
        log.debug("REST request to get all GlobalChats");
        return globalChatService.findAll();
    }

    /**
     * {@code GET  /global-chats/:id} : get the "id" globalChat.
     *
     * @param id the id of the globalChat to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the globalChat, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/global-chats/{id}")
    public ResponseEntity<GlobalChat> getGlobalChat(@PathVariable Long id) {
        log.debug("REST request to get GlobalChat : {}", id);
        Optional<GlobalChat> globalChat = globalChatService.findOne(id);
        return ResponseUtil.wrapOrNotFound(globalChat);
    }

    /**
     * {@code DELETE  /global-chats/:id} : delete the "id" globalChat.
     *
     * @param id the id of the globalChat to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/global-chats/{id}")
    public ResponseEntity<Void> deleteGlobalChat(@PathVariable Long id) {
        log.debug("REST request to delete GlobalChat : {}", id);
        globalChatService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
