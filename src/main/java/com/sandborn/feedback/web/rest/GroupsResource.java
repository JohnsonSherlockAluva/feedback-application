package com.sandborn.feedback.web.rest;

import com.sandborn.feedback.domain.Groups;
import com.sandborn.feedback.repository.GroupsRepository;
import com.sandborn.feedback.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.sandborn.feedback.domain.Groups}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class GroupsResource {

    private final Logger log = LoggerFactory.getLogger(GroupsResource.class);

    private static final String ENTITY_NAME = "groups";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GroupsRepository groupsRepository;

    public GroupsResource(GroupsRepository groupsRepository) {
        this.groupsRepository = groupsRepository;
    }

    /**
     * {@code POST  /groups} : Create a new groups.
     *
     * @param groups the groups to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new groups, or with status {@code 400 (Bad Request)} if the groups has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/groups")
    public ResponseEntity<Groups> createGroups(@RequestBody Groups groups) throws URISyntaxException {
        log.debug("REST request to save Groups : {}", groups);
        if (groups.getId() != null) {
            throw new BadRequestAlertException("A new groups cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Groups result = groupsRepository.save(groups);
        return ResponseEntity
            .created(new URI("/api/groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /groups/:id} : Updates an existing groups.
     *
     * @param id the id of the groups to save.
     * @param groups the groups to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated groups,
     * or with status {@code 400 (Bad Request)} if the groups is not valid,
     * or with status {@code 500 (Internal Server Error)} if the groups couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/groups/{id}")
    public ResponseEntity<Groups> updateGroups(@PathVariable(value = "id", required = false) final Long id, @RequestBody Groups groups)
        throws URISyntaxException {
        log.debug("REST request to update Groups : {}, {}", id, groups);
        if (groups.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, groups.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!groupsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Groups result = groupsRepository.save(groups);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, groups.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /groups/:id} : Partial updates given fields of an existing groups, field will ignore if it is null
     *
     * @param id the id of the groups to save.
     * @param groups the groups to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated groups,
     * or with status {@code 400 (Bad Request)} if the groups is not valid,
     * or with status {@code 404 (Not Found)} if the groups is not found,
     * or with status {@code 500 (Internal Server Error)} if the groups couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/groups/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Groups> partialUpdateGroups(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Groups groups
    ) throws URISyntaxException {
        log.debug("REST request to partial update Groups partially : {}, {}", id, groups);
        if (groups.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, groups.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!groupsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Groups> result = groupsRepository
            .findById(groups.getId())
            .map(existingGroups -> {
                if (groups.getGroupName() != null) {
                    existingGroups.setGroupName(groups.getGroupName());
                }
                if (groups.getDiscription() != null) {
                    existingGroups.setDiscription(groups.getDiscription());
                }
                if (groups.getPicture() != null) {
                    existingGroups.setPicture(groups.getPicture());
                }
                if (groups.getPictureContentType() != null) {
                    existingGroups.setPictureContentType(groups.getPictureContentType());
                }
                if (groups.getStatus() != null) {
                    existingGroups.setStatus(groups.getStatus());
                }

                return existingGroups;
            })
            .map(groupsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, groups.getId().toString())
        );
    }

    /**
     * {@code GET  /groups} : get all the groups.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of groups in body.
     */
    @GetMapping("/groups")
    public ResponseEntity<List<Groups>> getAllGroups(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of Groups");
        Page<Groups> page;
        if (eagerload) {
            page = groupsRepository.findAllWithEagerRelationships(pageable);
        } else {
            page = groupsRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /groups/:id} : get the "id" groups.
     *
     * @param id the id of the groups to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the groups, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/groups/{id}")
    public ResponseEntity<Groups> getGroups(@PathVariable Long id) {
        log.debug("REST request to get Groups : {}", id);
        Optional<Groups> groups = groupsRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(groups);
    }

    /**
     * {@code DELETE  /groups/:id} : delete the "id" groups.
     *
     * @param id the id of the groups to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/groups/{id}")
    public ResponseEntity<Void> deleteGroups(@PathVariable Long id) {
        log.debug("REST request to delete Groups : {}", id);
        groupsRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
