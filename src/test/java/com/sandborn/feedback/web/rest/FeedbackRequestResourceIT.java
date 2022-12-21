package com.sandborn.feedback.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sandborn.feedback.IntegrationTest;
import com.sandborn.feedback.domain.FeedbackRequest;
import com.sandborn.feedback.repository.FeedbackRequestRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link FeedbackRequestResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FeedbackRequestResourceIT {

    private static final String DEFAULT_SUBJECT = "AAAAAAAAAA";
    private static final String UPDATED_SUBJECT = "BBBBBBBBBB";

    private static final String DEFAULT_DISCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DISCRIPTION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_PICTURE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PICTURE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PICTURE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PICTURE_CONTENT_TYPE = "image/png";

    private static final Boolean DEFAULT_STATUS = false;
    private static final Boolean UPDATED_STATUS = true;

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_CREATED_BY = 1L;
    private static final Long UPDATED_CREATED_BY = 2L;

    private static final String ENTITY_API_URL = "/api/feedback-requests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FeedbackRequestRepository feedbackRequestRepository;

    @Mock
    private FeedbackRequestRepository feedbackRequestRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFeedbackRequestMockMvc;

    private FeedbackRequest feedbackRequest;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FeedbackRequest createEntity(EntityManager em) {
        FeedbackRequest feedbackRequest = new FeedbackRequest()
            .subject(DEFAULT_SUBJECT)
            .discription(DEFAULT_DISCRIPTION)
            .picture(DEFAULT_PICTURE)
            .pictureContentType(DEFAULT_PICTURE_CONTENT_TYPE)
            .status(DEFAULT_STATUS)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .createdBy(DEFAULT_CREATED_BY);
        return feedbackRequest;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FeedbackRequest createUpdatedEntity(EntityManager em) {
        FeedbackRequest feedbackRequest = new FeedbackRequest()
            .subject(UPDATED_SUBJECT)
            .discription(UPDATED_DISCRIPTION)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .status(UPDATED_STATUS)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .createdBy(UPDATED_CREATED_BY);
        return feedbackRequest;
    }

    @BeforeEach
    public void initTest() {
        feedbackRequest = createEntity(em);
    }

    @Test
    @Transactional
    void createFeedbackRequest() throws Exception {
        int databaseSizeBeforeCreate = feedbackRequestRepository.findAll().size();
        // Create the FeedbackRequest
        restFeedbackRequestMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(feedbackRequest))
            )
            .andExpect(status().isCreated());

        // Validate the FeedbackRequest in the database
        List<FeedbackRequest> feedbackRequestList = feedbackRequestRepository.findAll();
        assertThat(feedbackRequestList).hasSize(databaseSizeBeforeCreate + 1);
        FeedbackRequest testFeedbackRequest = feedbackRequestList.get(feedbackRequestList.size() - 1);
        assertThat(testFeedbackRequest.getSubject()).isEqualTo(DEFAULT_SUBJECT);
        assertThat(testFeedbackRequest.getDiscription()).isEqualTo(DEFAULT_DISCRIPTION);
        assertThat(testFeedbackRequest.getPicture()).isEqualTo(DEFAULT_PICTURE);
        assertThat(testFeedbackRequest.getPictureContentType()).isEqualTo(DEFAULT_PICTURE_CONTENT_TYPE);
        assertThat(testFeedbackRequest.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testFeedbackRequest.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testFeedbackRequest.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testFeedbackRequest.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void createFeedbackRequestWithExistingId() throws Exception {
        // Create the FeedbackRequest with an existing ID
        feedbackRequest.setId(1L);

        int databaseSizeBeforeCreate = feedbackRequestRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFeedbackRequestMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(feedbackRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeedbackRequest in the database
        List<FeedbackRequest> feedbackRequestList = feedbackRequestRepository.findAll();
        assertThat(feedbackRequestList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFeedbackRequests() throws Exception {
        // Initialize the database
        feedbackRequestRepository.saveAndFlush(feedbackRequest);

        // Get all the feedbackRequestList
        restFeedbackRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(feedbackRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT)))
            .andExpect(jsonPath("$.[*].discription").value(hasItem(DEFAULT_DISCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].pictureContentType").value(hasItem(DEFAULT_PICTURE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].picture").value(hasItem(Base64Utils.encodeToString(DEFAULT_PICTURE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFeedbackRequestsWithEagerRelationshipsIsEnabled() throws Exception {
        when(feedbackRequestRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFeedbackRequestMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(feedbackRequestRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFeedbackRequestsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(feedbackRequestRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFeedbackRequestMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(feedbackRequestRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getFeedbackRequest() throws Exception {
        // Initialize the database
        feedbackRequestRepository.saveAndFlush(feedbackRequest);

        // Get the feedbackRequest
        restFeedbackRequestMockMvc
            .perform(get(ENTITY_API_URL_ID, feedbackRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(feedbackRequest.getId().intValue()))
            .andExpect(jsonPath("$.subject").value(DEFAULT_SUBJECT))
            .andExpect(jsonPath("$.discription").value(DEFAULT_DISCRIPTION.toString()))
            .andExpect(jsonPath("$.pictureContentType").value(DEFAULT_PICTURE_CONTENT_TYPE))
            .andExpect(jsonPath("$.picture").value(Base64Utils.encodeToString(DEFAULT_PICTURE)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.booleanValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingFeedbackRequest() throws Exception {
        // Get the feedbackRequest
        restFeedbackRequestMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFeedbackRequest() throws Exception {
        // Initialize the database
        feedbackRequestRepository.saveAndFlush(feedbackRequest);

        int databaseSizeBeforeUpdate = feedbackRequestRepository.findAll().size();

        // Update the feedbackRequest
        FeedbackRequest updatedFeedbackRequest = feedbackRequestRepository.findById(feedbackRequest.getId()).get();
        // Disconnect from session so that the updates on updatedFeedbackRequest are not directly saved in db
        em.detach(updatedFeedbackRequest);
        updatedFeedbackRequest
            .subject(UPDATED_SUBJECT)
            .discription(UPDATED_DISCRIPTION)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .status(UPDATED_STATUS)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .createdBy(UPDATED_CREATED_BY);

        restFeedbackRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFeedbackRequest.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFeedbackRequest))
            )
            .andExpect(status().isOk());

        // Validate the FeedbackRequest in the database
        List<FeedbackRequest> feedbackRequestList = feedbackRequestRepository.findAll();
        assertThat(feedbackRequestList).hasSize(databaseSizeBeforeUpdate);
        FeedbackRequest testFeedbackRequest = feedbackRequestList.get(feedbackRequestList.size() - 1);
        assertThat(testFeedbackRequest.getSubject()).isEqualTo(UPDATED_SUBJECT);
        assertThat(testFeedbackRequest.getDiscription()).isEqualTo(UPDATED_DISCRIPTION);
        assertThat(testFeedbackRequest.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testFeedbackRequest.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
        assertThat(testFeedbackRequest.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testFeedbackRequest.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testFeedbackRequest.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testFeedbackRequest.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void putNonExistingFeedbackRequest() throws Exception {
        int databaseSizeBeforeUpdate = feedbackRequestRepository.findAll().size();
        feedbackRequest.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFeedbackRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, feedbackRequest.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(feedbackRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeedbackRequest in the database
        List<FeedbackRequest> feedbackRequestList = feedbackRequestRepository.findAll();
        assertThat(feedbackRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFeedbackRequest() throws Exception {
        int databaseSizeBeforeUpdate = feedbackRequestRepository.findAll().size();
        feedbackRequest.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeedbackRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(feedbackRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeedbackRequest in the database
        List<FeedbackRequest> feedbackRequestList = feedbackRequestRepository.findAll();
        assertThat(feedbackRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFeedbackRequest() throws Exception {
        int databaseSizeBeforeUpdate = feedbackRequestRepository.findAll().size();
        feedbackRequest.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeedbackRequestMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(feedbackRequest))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FeedbackRequest in the database
        List<FeedbackRequest> feedbackRequestList = feedbackRequestRepository.findAll();
        assertThat(feedbackRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFeedbackRequestWithPatch() throws Exception {
        // Initialize the database
        feedbackRequestRepository.saveAndFlush(feedbackRequest);

        int databaseSizeBeforeUpdate = feedbackRequestRepository.findAll().size();

        // Update the feedbackRequest using partial update
        FeedbackRequest partialUpdatedFeedbackRequest = new FeedbackRequest();
        partialUpdatedFeedbackRequest.setId(feedbackRequest.getId());

        partialUpdatedFeedbackRequest
            .discription(UPDATED_DISCRIPTION)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .status(UPDATED_STATUS)
            .startDate(UPDATED_START_DATE);

        restFeedbackRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFeedbackRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFeedbackRequest))
            )
            .andExpect(status().isOk());

        // Validate the FeedbackRequest in the database
        List<FeedbackRequest> feedbackRequestList = feedbackRequestRepository.findAll();
        assertThat(feedbackRequestList).hasSize(databaseSizeBeforeUpdate);
        FeedbackRequest testFeedbackRequest = feedbackRequestList.get(feedbackRequestList.size() - 1);
        assertThat(testFeedbackRequest.getSubject()).isEqualTo(DEFAULT_SUBJECT);
        assertThat(testFeedbackRequest.getDiscription()).isEqualTo(UPDATED_DISCRIPTION);
        assertThat(testFeedbackRequest.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testFeedbackRequest.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
        assertThat(testFeedbackRequest.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testFeedbackRequest.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testFeedbackRequest.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testFeedbackRequest.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void fullUpdateFeedbackRequestWithPatch() throws Exception {
        // Initialize the database
        feedbackRequestRepository.saveAndFlush(feedbackRequest);

        int databaseSizeBeforeUpdate = feedbackRequestRepository.findAll().size();

        // Update the feedbackRequest using partial update
        FeedbackRequest partialUpdatedFeedbackRequest = new FeedbackRequest();
        partialUpdatedFeedbackRequest.setId(feedbackRequest.getId());

        partialUpdatedFeedbackRequest
            .subject(UPDATED_SUBJECT)
            .discription(UPDATED_DISCRIPTION)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .status(UPDATED_STATUS)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .createdBy(UPDATED_CREATED_BY);

        restFeedbackRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFeedbackRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFeedbackRequest))
            )
            .andExpect(status().isOk());

        // Validate the FeedbackRequest in the database
        List<FeedbackRequest> feedbackRequestList = feedbackRequestRepository.findAll();
        assertThat(feedbackRequestList).hasSize(databaseSizeBeforeUpdate);
        FeedbackRequest testFeedbackRequest = feedbackRequestList.get(feedbackRequestList.size() - 1);
        assertThat(testFeedbackRequest.getSubject()).isEqualTo(UPDATED_SUBJECT);
        assertThat(testFeedbackRequest.getDiscription()).isEqualTo(UPDATED_DISCRIPTION);
        assertThat(testFeedbackRequest.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testFeedbackRequest.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
        assertThat(testFeedbackRequest.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testFeedbackRequest.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testFeedbackRequest.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testFeedbackRequest.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void patchNonExistingFeedbackRequest() throws Exception {
        int databaseSizeBeforeUpdate = feedbackRequestRepository.findAll().size();
        feedbackRequest.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFeedbackRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, feedbackRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(feedbackRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeedbackRequest in the database
        List<FeedbackRequest> feedbackRequestList = feedbackRequestRepository.findAll();
        assertThat(feedbackRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFeedbackRequest() throws Exception {
        int databaseSizeBeforeUpdate = feedbackRequestRepository.findAll().size();
        feedbackRequest.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeedbackRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(feedbackRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeedbackRequest in the database
        List<FeedbackRequest> feedbackRequestList = feedbackRequestRepository.findAll();
        assertThat(feedbackRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFeedbackRequest() throws Exception {
        int databaseSizeBeforeUpdate = feedbackRequestRepository.findAll().size();
        feedbackRequest.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeedbackRequestMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(feedbackRequest))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FeedbackRequest in the database
        List<FeedbackRequest> feedbackRequestList = feedbackRequestRepository.findAll();
        assertThat(feedbackRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFeedbackRequest() throws Exception {
        // Initialize the database
        feedbackRequestRepository.saveAndFlush(feedbackRequest);

        int databaseSizeBeforeDelete = feedbackRequestRepository.findAll().size();

        // Delete the feedbackRequest
        restFeedbackRequestMockMvc
            .perform(delete(ENTITY_API_URL_ID, feedbackRequest.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FeedbackRequest> feedbackRequestList = feedbackRequestRepository.findAll();
        assertThat(feedbackRequestList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
