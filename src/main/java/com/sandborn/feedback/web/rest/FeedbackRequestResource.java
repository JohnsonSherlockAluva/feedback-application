package com.sandborn.feedback.web.rest;

import com.sandborn.feedback.domain.FeedbackRequest;
import com.sandborn.feedback.repository.FeedbackRequestRepository;
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
 * REST controller for managing {@link com.sandborn.feedback.domain.FeedbackRequest}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class FeedbackRequestResource {

    private final Logger log = LoggerFactory.getLogger(FeedbackRequestResource.class);

    private static final String ENTITY_NAME = "feedbackRequest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FeedbackRequestRepository feedbackRequestRepository;

    public FeedbackRequestResource(FeedbackRequestRepository feedbackRequestRepository) {
        this.feedbackRequestRepository = feedbackRequestRepository;
    }

    /**
     * {@code POST  /feedback-requests} : Create a new feedbackRequest.
     *
     * @param feedbackRequest the feedbackRequest to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new feedbackRequest, or with status {@code 400 (Bad Request)} if the feedbackRequest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/feedback-requests")
    public ResponseEntity<FeedbackRequest> createFeedbackRequest(@RequestBody FeedbackRequest feedbackRequest) throws URISyntaxException {
        log.debug("REST request to save FeedbackRequest : {}", feedbackRequest);
        if (feedbackRequest.getId() != null) {
            throw new BadRequestAlertException("A new feedbackRequest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FeedbackRequest result = feedbackRequestRepository.save(feedbackRequest);
        return ResponseEntity
            .created(new URI("/api/feedback-requests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /feedback-requests/:id} : Updates an existing feedbackRequest.
     *
     * @param id the id of the feedbackRequest to save.
     * @param feedbackRequest the feedbackRequest to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated feedbackRequest,
     * or with status {@code 400 (Bad Request)} if the feedbackRequest is not valid,
     * or with status {@code 500 (Internal Server Error)} if the feedbackRequest couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/feedback-requests/{id}")
    public ResponseEntity<FeedbackRequest> updateFeedbackRequest(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FeedbackRequest feedbackRequest
    ) throws URISyntaxException {
        log.debug("REST request to update FeedbackRequest : {}, {}", id, feedbackRequest);
        if (feedbackRequest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, feedbackRequest.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!feedbackRequestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FeedbackRequest result = feedbackRequestRepository.save(feedbackRequest);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, feedbackRequest.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /feedback-requests/:id} : Partial updates given fields of an existing feedbackRequest, field will ignore if it is null
     *
     * @param id the id of the feedbackRequest to save.
     * @param feedbackRequest the feedbackRequest to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated feedbackRequest,
     * or with status {@code 400 (Bad Request)} if the feedbackRequest is not valid,
     * or with status {@code 404 (Not Found)} if the feedbackRequest is not found,
     * or with status {@code 500 (Internal Server Error)} if the feedbackRequest couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/feedback-requests/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FeedbackRequest> partialUpdateFeedbackRequest(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FeedbackRequest feedbackRequest
    ) throws URISyntaxException {
        log.debug("REST request to partial update FeedbackRequest partially : {}, {}", id, feedbackRequest);
        if (feedbackRequest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, feedbackRequest.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!feedbackRequestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FeedbackRequest> result = feedbackRequestRepository
            .findById(feedbackRequest.getId())
            .map(existingFeedbackRequest -> {
                if (feedbackRequest.getSubject() != null) {
                    existingFeedbackRequest.setSubject(feedbackRequest.getSubject());
                }
                if (feedbackRequest.getDiscription() != null) {
                    existingFeedbackRequest.setDiscription(feedbackRequest.getDiscription());
                }
                if (feedbackRequest.getPicture() != null) {
                    existingFeedbackRequest.setPicture(feedbackRequest.getPicture());
                }
                if (feedbackRequest.getPictureContentType() != null) {
                    existingFeedbackRequest.setPictureContentType(feedbackRequest.getPictureContentType());
                }
                if (feedbackRequest.getStatus() != null) {
                    existingFeedbackRequest.setStatus(feedbackRequest.getStatus());
                }
                if (feedbackRequest.getStartDate() != null) {
                    existingFeedbackRequest.setStartDate(feedbackRequest.getStartDate());
                }
                if (feedbackRequest.getEndDate() != null) {
                    existingFeedbackRequest.setEndDate(feedbackRequest.getEndDate());
                }
                if (feedbackRequest.getCreatedBy() != null) {
                    existingFeedbackRequest.setCreatedBy(feedbackRequest.getCreatedBy());
                }

                return existingFeedbackRequest;
            })
            .map(feedbackRequestRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, feedbackRequest.getId().toString())
        );
    }

    /**
     * {@code GET  /feedback-requests} : get all the feedbackRequests.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of feedbackRequests in body.
     */
    @GetMapping("/feedback-requests")
    public ResponseEntity<List<FeedbackRequest>> getAllFeedbackRequests(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of FeedbackRequests");
        Page<FeedbackRequest> page;
        if (eagerload) {
            page = feedbackRequestRepository.findAllWithEagerRelationships(pageable);
        } else {
            page = feedbackRequestRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /feedback-requests/:id} : get the "id" feedbackRequest.
     *
     * @param id the id of the feedbackRequest to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the feedbackRequest, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/feedback-requests/{id}")
    public ResponseEntity<FeedbackRequest> getFeedbackRequest(@PathVariable Long id) {
        log.debug("REST request to get FeedbackRequest : {}", id);
        Optional<FeedbackRequest> feedbackRequest = feedbackRequestRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(feedbackRequest);
    }

    /**
     * {@code DELETE  /feedback-requests/:id} : delete the "id" feedbackRequest.
     *
     * @param id the id of the feedbackRequest to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/feedback-requests/{id}")
    public ResponseEntity<Void> deleteFeedbackRequest(@PathVariable Long id) {
        log.debug("REST request to delete FeedbackRequest : {}", id);
        feedbackRequestRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
