package com.hofimefu.web.rest;

import com.hofimefu.domain.EvetUser;
import com.hofimefu.repository.EvetUserRepository;
import com.hofimefu.service.EvetUserService;
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
 * REST controller for managing {@link com.hofimefu.domain.EvetUser}.
 */
@RestController
@RequestMapping("/api")
public class EvetUserResource {

    private final Logger log = LoggerFactory.getLogger(EvetUserResource.class);

    private static final String ENTITY_NAME = "evetUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EvetUserService evetUserService;

    private final EvetUserRepository evetUserRepository;

    public EvetUserResource(EvetUserService evetUserService, EvetUserRepository evetUserRepository) {
        this.evetUserService = evetUserService;
        this.evetUserRepository = evetUserRepository;
    }

    /**
     * {@code POST  /evet-users} : Create a new evetUser.
     *
     * @param evetUser the evetUser to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new evetUser, or with status {@code 400 (Bad Request)} if the evetUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/evet-users")
    public ResponseEntity<EvetUser> createEvetUser(@RequestBody EvetUser evetUser) throws URISyntaxException {
        log.debug("REST request to save EvetUser : {}", evetUser);
        if (evetUser.getId() != null) {
            throw new BadRequestAlertException("A new evetUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EvetUser result = evetUserService.save(evetUser);
        return ResponseEntity
            .created(new URI("/api/evet-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /evet-users/:id} : Updates an existing evetUser.
     *
     * @param id the id of the evetUser to save.
     * @param evetUser the evetUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated evetUser,
     * or with status {@code 400 (Bad Request)} if the evetUser is not valid,
     * or with status {@code 500 (Internal Server Error)} if the evetUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/evet-users/{id}")
    public ResponseEntity<EvetUser> updateEvetUser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EvetUser evetUser
    ) throws URISyntaxException {
        log.debug("REST request to update EvetUser : {}, {}", id, evetUser);
        if (evetUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, evetUser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!evetUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EvetUser result = evetUserService.update(evetUser);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, evetUser.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /evet-users/:id} : Partial updates given fields of an existing evetUser, field will ignore if it is null
     *
     * @param id the id of the evetUser to save.
     * @param evetUser the evetUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated evetUser,
     * or with status {@code 400 (Bad Request)} if the evetUser is not valid,
     * or with status {@code 404 (Not Found)} if the evetUser is not found,
     * or with status {@code 500 (Internal Server Error)} if the evetUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/evet-users/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EvetUser> partialUpdateEvetUser(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EvetUser evetUser
    ) throws URISyntaxException {
        log.debug("REST request to partial update EvetUser partially : {}, {}", id, evetUser);
        if (evetUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, evetUser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!evetUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EvetUser> result = evetUserService.partialUpdate(evetUser);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, evetUser.getId().toString())
        );
    }

    /**
     * {@code GET  /evet-users} : get all the evetUsers.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of evetUsers in body.
     */
    @GetMapping("/evet-users")
    public List<EvetUser> getAllEvetUsers(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all EvetUsers");
        return evetUserService.findAll();
    }

    /**
     * {@code GET  /evet-users/:id} : get the "id" evetUser.
     *
     * @param id the id of the evetUser to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the evetUser, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/evet-users/{id}")
    public ResponseEntity<EvetUser> getEvetUser(@PathVariable Long id) {
        log.debug("REST request to get EvetUser : {}", id);
        Optional<EvetUser> evetUser = evetUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(evetUser);
    }

    /**
     * {@code DELETE  /evet-users/:id} : delete the "id" evetUser.
     *
     * @param id the id of the evetUser to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/evet-users/{id}")
    public ResponseEntity<Void> deleteEvetUser(@PathVariable Long id) {
        log.debug("REST request to delete EvetUser : {}", id);
        evetUserService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
