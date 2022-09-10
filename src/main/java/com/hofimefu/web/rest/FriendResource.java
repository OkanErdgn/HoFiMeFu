package com.hofimefu.web.rest;

import com.hofimefu.domain.Friend;
import com.hofimefu.repository.FriendRepository;
import com.hofimefu.service.FriendService;
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
 * REST controller for managing {@link com.hofimefu.domain.Friend}.
 */
@RestController
@RequestMapping("/api")
public class FriendResource {

    private final Logger log = LoggerFactory.getLogger(FriendResource.class);

    private static final String ENTITY_NAME = "friend";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FriendService friendService;

    private final FriendRepository friendRepository;

    public FriendResource(FriendService friendService, FriendRepository friendRepository) {
        this.friendService = friendService;
        this.friendRepository = friendRepository;
    }

    /**
     * {@code POST  /friends} : Create a new friend.
     *
     * @param friend the friend to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new friend, or with status {@code 400 (Bad Request)} if the friend has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/friends")
    public ResponseEntity<Friend> createFriend(@RequestBody Friend friend) throws URISyntaxException {
        log.debug("REST request to save Friend : {}", friend);
        if (friend.getId() != null) {
            throw new BadRequestAlertException("A new friend cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Friend result = friendService.save(friend);
        return ResponseEntity
            .created(new URI("/api/friends/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /friends/:id} : Updates an existing friend.
     *
     * @param id the id of the friend to save.
     * @param friend the friend to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated friend,
     * or with status {@code 400 (Bad Request)} if the friend is not valid,
     * or with status {@code 500 (Internal Server Error)} if the friend couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/friends/{id}")
    public ResponseEntity<Friend> updateFriend(@PathVariable(value = "id", required = false) final Long id, @RequestBody Friend friend)
        throws URISyntaxException {
        log.debug("REST request to update Friend : {}, {}", id, friend);
        if (friend.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, friend.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!friendRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Friend result = friendService.update(friend);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, friend.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /friends/:id} : Partial updates given fields of an existing friend, field will ignore if it is null
     *
     * @param id the id of the friend to save.
     * @param friend the friend to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated friend,
     * or with status {@code 400 (Bad Request)} if the friend is not valid,
     * or with status {@code 404 (Not Found)} if the friend is not found,
     * or with status {@code 500 (Internal Server Error)} if the friend couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/friends/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Friend> partialUpdateFriend(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Friend friend
    ) throws URISyntaxException {
        log.debug("REST request to partial update Friend partially : {}, {}", id, friend);
        if (friend.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, friend.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!friendRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Friend> result = friendService.partialUpdate(friend);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, friend.getId().toString())
        );
    }

    /**
     * {@code GET  /friends} : get all the friends.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of friends in body.
     */
    @GetMapping("/friends")
    public List<Friend> getAllFriends() {
        log.debug("REST request to get all Friends");
        return friendService.findAll();
    }

    /**
     * {@code GET  /friends/:id} : get the "id" friend.
     *
     * @param id the id of the friend to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the friend, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/friends/{id}")
    public ResponseEntity<Friend> getFriend(@PathVariable Long id) {
        log.debug("REST request to get Friend : {}", id);
        Optional<Friend> friend = friendService.findOne(id);
        return ResponseUtil.wrapOrNotFound(friend);
    }

    /**
     * {@code DELETE  /friends/:id} : delete the "id" friend.
     *
     * @param id the id of the friend to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/friends/{id}")
    public ResponseEntity<Void> deleteFriend(@PathVariable Long id) {
        log.debug("REST request to delete Friend : {}", id);
        friendService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
