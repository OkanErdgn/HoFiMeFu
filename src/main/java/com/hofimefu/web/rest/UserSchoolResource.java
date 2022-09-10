package com.hofimefu.web.rest;

import com.hofimefu.domain.UserSchool;
import com.hofimefu.repository.UserSchoolRepository;
import com.hofimefu.service.UserSchoolService;
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
 * REST controller for managing {@link com.hofimefu.domain.UserSchool}.
 */
@RestController
@RequestMapping("/api")
public class UserSchoolResource {

    private final Logger log = LoggerFactory.getLogger(UserSchoolResource.class);

    private static final String ENTITY_NAME = "userSchool";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserSchoolService userSchoolService;

    private final UserSchoolRepository userSchoolRepository;

    public UserSchoolResource(UserSchoolService userSchoolService, UserSchoolRepository userSchoolRepository) {
        this.userSchoolService = userSchoolService;
        this.userSchoolRepository = userSchoolRepository;
    }

    /**
     * {@code POST  /user-schools} : Create a new userSchool.
     *
     * @param userSchool the userSchool to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userSchool, or with status {@code 400 (Bad Request)} if the userSchool has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-schools")
    public ResponseEntity<UserSchool> createUserSchool(@RequestBody UserSchool userSchool) throws URISyntaxException {
        log.debug("REST request to save UserSchool : {}", userSchool);
        if (userSchool.getId() != null) {
            throw new BadRequestAlertException("A new userSchool cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserSchool result = userSchoolService.save(userSchool);
        return ResponseEntity
            .created(new URI("/api/user-schools/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-schools/:id} : Updates an existing userSchool.
     *
     * @param id the id of the userSchool to save.
     * @param userSchool the userSchool to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userSchool,
     * or with status {@code 400 (Bad Request)} if the userSchool is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userSchool couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-schools/{id}")
    public ResponseEntity<UserSchool> updateUserSchool(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserSchool userSchool
    ) throws URISyntaxException {
        log.debug("REST request to update UserSchool : {}, {}", id, userSchool);
        if (userSchool.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userSchool.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userSchoolRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UserSchool result = userSchoolService.update(userSchool);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userSchool.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /user-schools/:id} : Partial updates given fields of an existing userSchool, field will ignore if it is null
     *
     * @param id the id of the userSchool to save.
     * @param userSchool the userSchool to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userSchool,
     * or with status {@code 400 (Bad Request)} if the userSchool is not valid,
     * or with status {@code 404 (Not Found)} if the userSchool is not found,
     * or with status {@code 500 (Internal Server Error)} if the userSchool couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/user-schools/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserSchool> partialUpdateUserSchool(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserSchool userSchool
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserSchool partially : {}, {}", id, userSchool);
        if (userSchool.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userSchool.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userSchoolRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserSchool> result = userSchoolService.partialUpdate(userSchool);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userSchool.getId().toString())
        );
    }

    /**
     * {@code GET  /user-schools} : get all the userSchools.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userSchools in body.
     */
    @GetMapping("/user-schools")
    public List<UserSchool> getAllUserSchools() {
        log.debug("REST request to get all UserSchools");
        return userSchoolService.findAll();
    }

    /**
     * {@code GET  /user-schools/:id} : get the "id" userSchool.
     *
     * @param id the id of the userSchool to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userSchool, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-schools/{id}")
    public ResponseEntity<UserSchool> getUserSchool(@PathVariable Long id) {
        log.debug("REST request to get UserSchool : {}", id);
        Optional<UserSchool> userSchool = userSchoolService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userSchool);
    }

    /**
     * {@code DELETE  /user-schools/:id} : delete the "id" userSchool.
     *
     * @param id the id of the userSchool to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-schools/{id}")
    public ResponseEntity<Void> deleteUserSchool(@PathVariable Long id) {
        log.debug("REST request to delete UserSchool : {}", id);
        userSchoolService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
