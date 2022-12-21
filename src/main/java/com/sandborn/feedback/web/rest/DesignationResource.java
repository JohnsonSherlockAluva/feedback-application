package com.sandborn.feedback.web.rest;

import com.sandborn.feedback.domain.Designation;
import com.sandborn.feedback.repository.DesignationRepository;
import com.sandborn.feedback.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.sandborn.feedback.domain.Designation}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DesignationResource {

    private final Logger log = LoggerFactory.getLogger(DesignationResource.class);

    private static final String ENTITY_NAME = "designation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DesignationRepository designationRepository;

    public DesignationResource(DesignationRepository designationRepository) {
        this.designationRepository = designationRepository;
    }

    /**
     * {@code POST  /designations} : Create a new designation.
     *
     * @param designation the designation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new designation, or with status {@code 400 (Bad Request)} if the designation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/designations")
    public ResponseEntity<Designation> createDesignation(@RequestBody Designation designation) throws URISyntaxException {
        log.debug("REST request to save Designation : {}", designation);
        if (designation.getId() != null) {
            throw new BadRequestAlertException("A new designation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Designation result = designationRepository.save(designation);
        return ResponseEntity
            .created(new URI("/api/designations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /designations/:id} : Updates an existing designation.
     *
     * @param id the id of the designation to save.
     * @param designation the designation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated designation,
     * or with status {@code 400 (Bad Request)} if the designation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the designation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/designations/{id}")
    public ResponseEntity<Designation> updateDesignation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Designation designation
    ) throws URISyntaxException {
        log.debug("REST request to update Designation : {}, {}", id, designation);
        if (designation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, designation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!designationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Designation result = designationRepository.save(designation);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, designation.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /designations/:id} : Partial updates given fields of an existing designation, field will ignore if it is null
     *
     * @param id the id of the designation to save.
     * @param designation the designation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated designation,
     * or with status {@code 400 (Bad Request)} if the designation is not valid,
     * or with status {@code 404 (Not Found)} if the designation is not found,
     * or with status {@code 500 (Internal Server Error)} if the designation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/designations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Designation> partialUpdateDesignation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Designation designation
    ) throws URISyntaxException {
        log.debug("REST request to partial update Designation partially : {}, {}", id, designation);
        if (designation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, designation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!designationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Designation> result = designationRepository
            .findById(designation.getId())
            .map(existingDesignation -> {
                if (designation.getDesignationName() != null) {
                    existingDesignation.setDesignationName(designation.getDesignationName());
                }
                if (designation.getDiscription() != null) {
                    existingDesignation.setDiscription(designation.getDiscription());
                }
                if (designation.getPicture() != null) {
                    existingDesignation.setPicture(designation.getPicture());
                }
                if (designation.getPictureContentType() != null) {
                    existingDesignation.setPictureContentType(designation.getPictureContentType());
                }
                if (designation.getStatus() != null) {
                    existingDesignation.setStatus(designation.getStatus());
                }

                return existingDesignation;
            })
            .map(designationRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, designation.getId().toString())
        );
    }

    /**
     * {@code GET  /designations} : get all the designations.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of designations in body.
     */
    @GetMapping("/designations")
    public List<Designation> getAllDesignations() {
        log.debug("REST request to get all Designations");
        return designationRepository.findAll();
    }

    /**
     * {@code GET  /designations/:id} : get the "id" designation.
     *
     * @param id the id of the designation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the designation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/designations/{id}")
    public ResponseEntity<Designation> getDesignation(@PathVariable Long id) {
        log.debug("REST request to get Designation : {}", id);
        Optional<Designation> designation = designationRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(designation);
    }

    /**
     * {@code DELETE  /designations/:id} : delete the "id" designation.
     *
     * @param id the id of the designation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/designations/{id}")
    public ResponseEntity<Void> deleteDesignation(@PathVariable Long id) {
        log.debug("REST request to delete Designation : {}", id);
        designationRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
