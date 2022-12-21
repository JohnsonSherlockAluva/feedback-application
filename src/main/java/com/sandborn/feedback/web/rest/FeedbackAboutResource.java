package com.sandborn.feedback.web.rest;

import com.sandborn.feedback.domain.FeedbackAbout;
import com.sandborn.feedback.repository.FeedbackAboutRepository;
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
 * REST controller for managing {@link com.sandborn.feedback.domain.FeedbackAbout}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class FeedbackAboutResource {

    private final Logger log = LoggerFactory.getLogger(FeedbackAboutResource.class);

    private static final String ENTITY_NAME = "feedbackAbout";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FeedbackAboutRepository feedbackAboutRepository;

    public FeedbackAboutResource(FeedbackAboutRepository feedbackAboutRepository) {
        this.feedbackAboutRepository = feedbackAboutRepository;
    }

    /**
     * {@code POST  /feedback-abouts} : Create a new feedbackAbout.
     *
     * @param feedbackAbout the feedbackAbout to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new feedbackAbout, or with status {@code 400 (Bad Request)} if the feedbackAbout has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/feedback-abouts")
    public ResponseEntity<FeedbackAbout> createFeedbackAbout(@RequestBody FeedbackAbout feedbackAbout) throws URISyntaxException {
        log.debug("REST request to save FeedbackAbout : {}", feedbackAbout);
        if (feedbackAbout.getId() != null) {
            throw new BadRequestAlertException("A new feedbackAbout cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FeedbackAbout result = feedbackAboutRepository.save(feedbackAbout);
        return ResponseEntity
            .created(new URI("/api/feedback-abouts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /feedback-abouts/:id} : Updates an existing feedbackAbout.
     *
     * @param id the id of the feedbackAbout to save.
     * @param feedbackAbout the feedbackAbout to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated feedbackAbout,
     * or with status {@code 400 (Bad Request)} if the feedbackAbout is not valid,
     * or with status {@code 500 (Internal Server Error)} if the feedbackAbout couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/feedback-abouts/{id}")
    public ResponseEntity<FeedbackAbout> updateFeedbackAbout(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FeedbackAbout feedbackAbout
    ) throws URISyntaxException {
        log.debug("REST request to update FeedbackAbout : {}, {}", id, feedbackAbout);
        if (feedbackAbout.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, feedbackAbout.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!feedbackAboutRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FeedbackAbout result = feedbackAboutRepository.save(feedbackAbout);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, feedbackAbout.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /feedback-abouts/:id} : Partial updates given fields of an existing feedbackAbout, field will ignore if it is null
     *
     * @param id the id of the feedbackAbout to save.
     * @param feedbackAbout the feedbackAbout to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated feedbackAbout,
     * or with status {@code 400 (Bad Request)} if the feedbackAbout is not valid,
     * or with status {@code 404 (Not Found)} if the feedbackAbout is not found,
     * or with status {@code 500 (Internal Server Error)} if the feedbackAbout couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/feedback-abouts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FeedbackAbout> partialUpdateFeedbackAbout(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FeedbackAbout feedbackAbout
    ) throws URISyntaxException {
        log.debug("REST request to partial update FeedbackAbout partially : {}, {}", id, feedbackAbout);
        if (feedbackAbout.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, feedbackAbout.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!feedbackAboutRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FeedbackAbout> result = feedbackAboutRepository
            .findById(feedbackAbout.getId())
            .map(existingFeedbackAbout -> {
                if (feedbackAbout.getFeedbackabout() != null) {
                    existingFeedbackAbout.setFeedbackabout(feedbackAbout.getFeedbackabout());
                }
                if (feedbackAbout.getDiscription() != null) {
                    existingFeedbackAbout.setDiscription(feedbackAbout.getDiscription());
                }
                if (feedbackAbout.getPicture() != null) {
                    existingFeedbackAbout.setPicture(feedbackAbout.getPicture());
                }
                if (feedbackAbout.getPictureContentType() != null) {
                    existingFeedbackAbout.setPictureContentType(feedbackAbout.getPictureContentType());
                }
                if (feedbackAbout.getStatus() != null) {
                    existingFeedbackAbout.setStatus(feedbackAbout.getStatus());
                }

                return existingFeedbackAbout;
            })
            .map(feedbackAboutRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, feedbackAbout.getId().toString())
        );
    }

    /**
     * {@code GET  /feedback-abouts} : get all the feedbackAbouts.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of feedbackAbouts in body.
     */
    @GetMapping("/feedback-abouts")
    public ResponseEntity<List<FeedbackAbout>> getAllFeedbackAbouts(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of FeedbackAbouts");
        Page<FeedbackAbout> page = feedbackAboutRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /feedback-abouts/:id} : get the "id" feedbackAbout.
     *
     * @param id the id of the feedbackAbout to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the feedbackAbout, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/feedback-abouts/{id}")
    public ResponseEntity<FeedbackAbout> getFeedbackAbout(@PathVariable Long id) {
        log.debug("REST request to get FeedbackAbout : {}", id);
        Optional<FeedbackAbout> feedbackAbout = feedbackAboutRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(feedbackAbout);
    }

    /**
     * {@code DELETE  /feedback-abouts/:id} : delete the "id" feedbackAbout.
     *
     * @param id the id of the feedbackAbout to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/feedback-abouts/{id}")
    public ResponseEntity<Void> deleteFeedbackAbout(@PathVariable Long id) {
        log.debug("REST request to delete FeedbackAbout : {}", id);
        feedbackAboutRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
