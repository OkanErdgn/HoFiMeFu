package com.hofimefu.web.rest;

import com.hofimefu.domain.FriendStatus;
import com.hofimefu.repository.FriendStatusRepository;
import com.hofimefu.service.FriendStatusService;
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
 * REST controller for managing {@link com.hofimefu.domain.FriendStatus}.
 */
@RestController
@RequestMapping("/api")
public class FriendStatusResource {

    private final Logger log = LoggerFactory.getLogger(FriendStatusResource.class);

    private static final String ENTITY_NAME = "friendStatus";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FriendStatusService friendStatusService;

    private final FriendStatusRepository friendStatusRepository;

    public FriendStatusResource(FriendStatusService friendStatusService, FriendStatusRepository friendStatusRepository) {
        this.friendStatusService = friendStatusService;
        this.friendStatusRepository = friendStatusRepository;
    }

    /**
     * {@code POST  /friend-statuses} : Create a new friendStatus.
     *
     * @param friendStatus the friendStatus to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new friendStatus, or with status {@code 400 (Bad Request)} if the friendStatus has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/friend-statuses")
    public ResponseEntity<FriendStatus> createFriendStatus(@RequestBody FriendStatus friendStatus) throws URISyntaxException {
        log.debug("REST request to save FriendStatus : {}", friendStatus);
        if (friendStatus.getId() != null) {
            throw new BadRequestAlertException("A new friendStatus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FriendStatus result = friendStatusService.save(friendStatus);
        return ResponseEntity
            .created(new URI("/api/friend-statuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /friend-statuses/:id} : Updates an existing friendStatus.
     *
     * @param id the id of the friendStatus to save.
     * @param friendStatus the friendStatus to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated friendStatus,
     * or with status {@code 400 (Bad Request)} if the friendStatus is not valid,
     * or with status {@code 500 (Internal Server Error)} if the friendStatus couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/friend-statuses/{id}")
    public ResponseEntity<FriendStatus> updateFriendStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FriendStatus friendStatus
    ) throws URISyntaxException {
        log.debug("REST request to update FriendStatus : {}, {}", id, friendStatus);
        if (friendStatus.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, friendStatus.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!friendStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FriendStatus result = friendStatusService.update(friendStatus);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, friendStatus.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /friend-statuses/:id} : Partial updates given fields of an existing friendStatus, field will ignore if it is null
     *
     * @param id the id of the friendStatus to save.
     * @param friendStatus the friendStatus to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated friendStatus,
     * or with status {@code 400 (Bad Request)} if the friendStatus is not valid,
     * or with status {@code 404 (Not Found)} if the friendStatus is not found,
     * or with status {@code 500 (Internal Server Error)} if the friendStatus couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/friend-statuses/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FriendStatus> partialUpdateFriendStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FriendStatus friendStatus
    ) throws URISyntaxException {
        log.debug("REST request to partial update FriendStatus partially : {}, {}", id, friendStatus);
        if (friendStatus.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, friendStatus.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!friendStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FriendStatus> result = friendStatusService.partialUpdate(friendStatus);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, friendStatus.getId().toString())
        );
    }

    /**
     * {@code GET  /friend-statuses} : get all the friendStatuses.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of friendStatuses in body.
     */
    @GetMapping("/friend-statuses")
    public List<FriendStatus> getAllFriendStatuses() {
        log.debug("REST request to get all FriendStatuses");
        return friendStatusService.findAll();
    }

    /**
     * {@code GET  /friend-statuses/:id} : get the "id" friendStatus.
     *
     * @param id the id of the friendStatus to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the friendStatus, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/friend-statuses/{id}")
    public ResponseEntity<FriendStatus> getFriendStatus(@PathVariable Long id) {
        log.debug("REST request to get FriendStatus : {}", id);
        Optional<FriendStatus> friendStatus = friendStatusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(friendStatus);
    }

    /**
     * {@code DELETE  /friend-statuses/:id} : delete the "id" friendStatus.
     *
     * @param id the id of the friendStatus to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/friend-statuses/{id}")
    public ResponseEntity<Void> deleteFriendStatus(@PathVariable Long id) {
        log.debug("REST request to delete FriendStatus : {}", id);
        friendStatusService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
