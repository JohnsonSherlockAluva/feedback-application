package com.sandborn.feedback.web.rest;

import com.sandborn.feedback.domain.FeedbackResponse;
import com.sandborn.feedback.repository.FeedbackResponseRepository;
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
 * REST controller for managing {@link com.sandborn.feedback.domain.FeedbackResponse}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class FeedbackResponseResource {

    private final Logger log = LoggerFactory.getLogger(FeedbackResponseResource.class);

    private static final String ENTITY_NAME = "feedbackResponse";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FeedbackResponseRepository feedbackResponseRepository;

    public FeedbackResponseResource(FeedbackResponseRepository feedbackResponseRepository) {
        this.feedbackResponseRepository = feedbackResponseRepository;
    }

    /**
     * {@code POST  /feedback-responses} : Create a new feedbackResponse.
     *
     * @param feedbackResponse the feedbackResponse to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new feedbackResponse, or with status {@code 400 (Bad Request)} if the feedbackResponse has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/feedback-responses")
    public ResponseEntity<FeedbackResponse> createFeedbackResponse(@RequestBody FeedbackResponse feedbackResponse)
        throws URISyntaxException {
        log.debug("REST request to save FeedbackResponse : {}", feedbackResponse);
        if (feedbackResponse.getId() != null) {
            throw new BadRequestAlertException("A new feedbackResponse cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FeedbackResponse result = feedbackResponseRepository.save(feedbackResponse);
        return ResponseEntity
            .created(new URI("/api/feedback-responses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /feedback-responses/:id} : Updates an existing feedbackResponse.
     *
     * @param id the id of the feedbackResponse to save.
     * @param feedbackResponse the feedbackResponse to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated feedbackResponse,
     * or with status {@code 400 (Bad Request)} if the feedbackResponse is not valid,
     * or with status {@code 500 (Internal Server Error)} if the feedbackResponse couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/feedback-responses/{id}")
    public ResponseEntity<FeedbackResponse> updateFeedbackResponse(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FeedbackResponse feedbackResponse
    ) throws URISyntaxException {
        log.debug("REST request to update FeedbackResponse : {}, {}", id, feedbackResponse);
        if (feedbackResponse.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, feedbackResponse.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!feedbackResponseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FeedbackResponse result = feedbackResponseRepository.save(feedbackResponse);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, feedbackResponse.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /feedback-responses/:id} : Partial updates given fields of an existing feedbackResponse, field will ignore if it is null
     *
     * @param id the id of the feedbackResponse to save.
     * @param feedbackResponse the feedbackResponse to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated feedbackResponse,
     * or with status {@code 400 (Bad Request)} if the feedbackResponse is not valid,
     * or with status {@code 404 (Not Found)} if the feedbackResponse is not found,
     * or with status {@code 500 (Internal Server Error)} if the feedbackResponse couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/feedback-responses/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FeedbackResponse> partialUpdateFeedbackResponse(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FeedbackResponse feedbackResponse
    ) throws URISyntaxException {
        log.debug("REST request to partial update FeedbackResponse partially : {}, {}", id, feedbackResponse);
        if (feedbackResponse.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, feedbackResponse.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!feedbackResponseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FeedbackResponse> result = feedbackResponseRepository
            .findById(feedbackResponse.getId())
            .map(existingFeedbackResponse -> {
                if (feedbackResponse.getResponse() != null) {
                    existingFeedbackResponse.setResponse(feedbackResponse.getResponse());
                }
                if (feedbackResponse.getDiscription() != null) {
                    existingFeedbackResponse.setDiscription(feedbackResponse.getDiscription());
                }
                if (feedbackResponse.getPicture() != null) {
                    existingFeedbackResponse.setPicture(feedbackResponse.getPicture());
                }
                if (feedbackResponse.getPictureContentType() != null) {
                    existingFeedbackResponse.setPictureContentType(feedbackResponse.getPictureContentType());
                }
                if (feedbackResponse.getStatus() != null) {
                    existingFeedbackResponse.setStatus(feedbackResponse.getStatus());
                }

                return existingFeedbackResponse;
            })
            .map(feedbackResponseRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, feedbackResponse.getId().toString())
        );
    }

    /**
     * {@code GET  /feedback-responses} : get all the feedbackResponses.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of feedbackResponses in body.
     */
    @GetMapping("/feedback-responses")
    public List<FeedbackResponse> getAllFeedbackResponses(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all FeedbackResponses");
        if (eagerload) {
            return feedbackResponseRepository.findAllWithEagerRelationships();
        } else {
            return feedbackResponseRepository.findAll();
        }
    }

    /**
     * {@code GET  /feedback-responses/:id} : get the "id" feedbackResponse.
     *
     * @param id the id of the feedbackResponse to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the feedbackResponse, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/feedback-responses/{id}")
    public ResponseEntity<FeedbackResponse> getFeedbackResponse(@PathVariable Long id) {
        log.debug("REST request to get FeedbackResponse : {}", id);
        Optional<FeedbackResponse> feedbackResponse = feedbackResponseRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(feedbackResponse);
    }

    /**
     * {@code DELETE  /feedback-responses/:id} : delete the "id" feedbackResponse.
     *
     * @param id the id of the feedbackResponse to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/feedback-responses/{id}")
    public ResponseEntity<Void> deleteFeedbackResponse(@PathVariable Long id) {
        log.debug("REST request to delete FeedbackResponse : {}", id);
        feedbackResponseRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
