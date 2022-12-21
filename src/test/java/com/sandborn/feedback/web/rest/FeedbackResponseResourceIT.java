package com.sandborn.feedback.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sandborn.feedback.IntegrationTest;
import com.sandborn.feedback.domain.FeedbackResponse;
import com.sandborn.feedback.repository.FeedbackResponseRepository;
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
 * Integration tests for the {@link FeedbackResponseResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FeedbackResponseResourceIT {

    private static final String DEFAULT_RESPONSE = "AAAAAAAAAA";
    private static final String UPDATED_RESPONSE = "BBBBBBBBBB";

    private static final String DEFAULT_DISCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DISCRIPTION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_PICTURE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PICTURE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PICTURE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PICTURE_CONTENT_TYPE = "image/png";

    private static final Boolean DEFAULT_STATUS = false;
    private static final Boolean UPDATED_STATUS = true;

    private static final String ENTITY_API_URL = "/api/feedback-responses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FeedbackResponseRepository feedbackResponseRepository;

    @Mock
    private FeedbackResponseRepository feedbackResponseRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFeedbackResponseMockMvc;

    private FeedbackResponse feedbackResponse;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FeedbackResponse createEntity(EntityManager em) {
        FeedbackResponse feedbackResponse = new FeedbackResponse()
            .response(DEFAULT_RESPONSE)
            .discription(DEFAULT_DISCRIPTION)
            .picture(DEFAULT_PICTURE)
            .pictureContentType(DEFAULT_PICTURE_CONTENT_TYPE)
            .status(DEFAULT_STATUS);
        return feedbackResponse;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FeedbackResponse createUpdatedEntity(EntityManager em) {
        FeedbackResponse feedbackResponse = new FeedbackResponse()
            .response(UPDATED_RESPONSE)
            .discription(UPDATED_DISCRIPTION)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .status(UPDATED_STATUS);
        return feedbackResponse;
    }

    @BeforeEach
    public void initTest() {
        feedbackResponse = createEntity(em);
    }

    @Test
    @Transactional
    void createFeedbackResponse() throws Exception {
        int databaseSizeBeforeCreate = feedbackResponseRepository.findAll().size();
        // Create the FeedbackResponse
        restFeedbackResponseMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(feedbackResponse))
            )
            .andExpect(status().isCreated());

        // Validate the FeedbackResponse in the database
        List<FeedbackResponse> feedbackResponseList = feedbackResponseRepository.findAll();
        assertThat(feedbackResponseList).hasSize(databaseSizeBeforeCreate + 1);
        FeedbackResponse testFeedbackResponse = feedbackResponseList.get(feedbackResponseList.size() - 1);
        assertThat(testFeedbackResponse.getResponse()).isEqualTo(DEFAULT_RESPONSE);
        assertThat(testFeedbackResponse.getDiscription()).isEqualTo(DEFAULT_DISCRIPTION);
        assertThat(testFeedbackResponse.getPicture()).isEqualTo(DEFAULT_PICTURE);
        assertThat(testFeedbackResponse.getPictureContentType()).isEqualTo(DEFAULT_PICTURE_CONTENT_TYPE);
        assertThat(testFeedbackResponse.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createFeedbackResponseWithExistingId() throws Exception {
        // Create the FeedbackResponse with an existing ID
        feedbackResponse.setId(1L);

        int databaseSizeBeforeCreate = feedbackResponseRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFeedbackResponseMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(feedbackResponse))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeedbackResponse in the database
        List<FeedbackResponse> feedbackResponseList = feedbackResponseRepository.findAll();
        assertThat(feedbackResponseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFeedbackResponses() throws Exception {
        // Initialize the database
        feedbackResponseRepository.saveAndFlush(feedbackResponse);

        // Get all the feedbackResponseList
        restFeedbackResponseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(feedbackResponse.getId().intValue())))
            .andExpect(jsonPath("$.[*].response").value(hasItem(DEFAULT_RESPONSE)))
            .andExpect(jsonPath("$.[*].discription").value(hasItem(DEFAULT_DISCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].pictureContentType").value(hasItem(DEFAULT_PICTURE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].picture").value(hasItem(Base64Utils.encodeToString(DEFAULT_PICTURE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFeedbackResponsesWithEagerRelationshipsIsEnabled() throws Exception {
        when(feedbackResponseRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFeedbackResponseMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(feedbackResponseRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFeedbackResponsesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(feedbackResponseRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFeedbackResponseMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(feedbackResponseRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getFeedbackResponse() throws Exception {
        // Initialize the database
        feedbackResponseRepository.saveAndFlush(feedbackResponse);

        // Get the feedbackResponse
        restFeedbackResponseMockMvc
            .perform(get(ENTITY_API_URL_ID, feedbackResponse.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(feedbackResponse.getId().intValue()))
            .andExpect(jsonPath("$.response").value(DEFAULT_RESPONSE))
            .andExpect(jsonPath("$.discription").value(DEFAULT_DISCRIPTION.toString()))
            .andExpect(jsonPath("$.pictureContentType").value(DEFAULT_PICTURE_CONTENT_TYPE))
            .andExpect(jsonPath("$.picture").value(Base64Utils.encodeToString(DEFAULT_PICTURE)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingFeedbackResponse() throws Exception {
        // Get the feedbackResponse
        restFeedbackResponseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFeedbackResponse() throws Exception {
        // Initialize the database
        feedbackResponseRepository.saveAndFlush(feedbackResponse);

        int databaseSizeBeforeUpdate = feedbackResponseRepository.findAll().size();

        // Update the feedbackResponse
        FeedbackResponse updatedFeedbackResponse = feedbackResponseRepository.findById(feedbackResponse.getId()).get();
        // Disconnect from session so that the updates on updatedFeedbackResponse are not directly saved in db
        em.detach(updatedFeedbackResponse);
        updatedFeedbackResponse
            .response(UPDATED_RESPONSE)
            .discription(UPDATED_DISCRIPTION)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .status(UPDATED_STATUS);

        restFeedbackResponseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFeedbackResponse.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFeedbackResponse))
            )
            .andExpect(status().isOk());

        // Validate the FeedbackResponse in the database
        List<FeedbackResponse> feedbackResponseList = feedbackResponseRepository.findAll();
        assertThat(feedbackResponseList).hasSize(databaseSizeBeforeUpdate);
        FeedbackResponse testFeedbackResponse = feedbackResponseList.get(feedbackResponseList.size() - 1);
        assertThat(testFeedbackResponse.getResponse()).isEqualTo(UPDATED_RESPONSE);
        assertThat(testFeedbackResponse.getDiscription()).isEqualTo(UPDATED_DISCRIPTION);
        assertThat(testFeedbackResponse.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testFeedbackResponse.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
        assertThat(testFeedbackResponse.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingFeedbackResponse() throws Exception {
        int databaseSizeBeforeUpdate = feedbackResponseRepository.findAll().size();
        feedbackResponse.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFeedbackResponseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, feedbackResponse.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(feedbackResponse))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeedbackResponse in the database
        List<FeedbackResponse> feedbackResponseList = feedbackResponseRepository.findAll();
        assertThat(feedbackResponseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFeedbackResponse() throws Exception {
        int databaseSizeBeforeUpdate = feedbackResponseRepository.findAll().size();
        feedbackResponse.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeedbackResponseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(feedbackResponse))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeedbackResponse in the database
        List<FeedbackResponse> feedbackResponseList = feedbackResponseRepository.findAll();
        assertThat(feedbackResponseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFeedbackResponse() throws Exception {
        int databaseSizeBeforeUpdate = feedbackResponseRepository.findAll().size();
        feedbackResponse.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeedbackResponseMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(feedbackResponse))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FeedbackResponse in the database
        List<FeedbackResponse> feedbackResponseList = feedbackResponseRepository.findAll();
        assertThat(feedbackResponseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFeedbackResponseWithPatch() throws Exception {
        // Initialize the database
        feedbackResponseRepository.saveAndFlush(feedbackResponse);

        int databaseSizeBeforeUpdate = feedbackResponseRepository.findAll().size();

        // Update the feedbackResponse using partial update
        FeedbackResponse partialUpdatedFeedbackResponse = new FeedbackResponse();
        partialUpdatedFeedbackResponse.setId(feedbackResponse.getId());

        partialUpdatedFeedbackResponse.response(UPDATED_RESPONSE).discription(UPDATED_DISCRIPTION).status(UPDATED_STATUS);

        restFeedbackResponseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFeedbackResponse.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFeedbackResponse))
            )
            .andExpect(status().isOk());

        // Validate the FeedbackResponse in the database
        List<FeedbackResponse> feedbackResponseList = feedbackResponseRepository.findAll();
        assertThat(feedbackResponseList).hasSize(databaseSizeBeforeUpdate);
        FeedbackResponse testFeedbackResponse = feedbackResponseList.get(feedbackResponseList.size() - 1);
        assertThat(testFeedbackResponse.getResponse()).isEqualTo(UPDATED_RESPONSE);
        assertThat(testFeedbackResponse.getDiscription()).isEqualTo(UPDATED_DISCRIPTION);
        assertThat(testFeedbackResponse.getPicture()).isEqualTo(DEFAULT_PICTURE);
        assertThat(testFeedbackResponse.getPictureContentType()).isEqualTo(DEFAULT_PICTURE_CONTENT_TYPE);
        assertThat(testFeedbackResponse.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateFeedbackResponseWithPatch() throws Exception {
        // Initialize the database
        feedbackResponseRepository.saveAndFlush(feedbackResponse);

        int databaseSizeBeforeUpdate = feedbackResponseRepository.findAll().size();

        // Update the feedbackResponse using partial update
        FeedbackResponse partialUpdatedFeedbackResponse = new FeedbackResponse();
        partialUpdatedFeedbackResponse.setId(feedbackResponse.getId());

        partialUpdatedFeedbackResponse
            .response(UPDATED_RESPONSE)
            .discription(UPDATED_DISCRIPTION)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .status(UPDATED_STATUS);

        restFeedbackResponseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFeedbackResponse.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFeedbackResponse))
            )
            .andExpect(status().isOk());

        // Validate the FeedbackResponse in the database
        List<FeedbackResponse> feedbackResponseList = feedbackResponseRepository.findAll();
        assertThat(feedbackResponseList).hasSize(databaseSizeBeforeUpdate);
        FeedbackResponse testFeedbackResponse = feedbackResponseList.get(feedbackResponseList.size() - 1);
        assertThat(testFeedbackResponse.getResponse()).isEqualTo(UPDATED_RESPONSE);
        assertThat(testFeedbackResponse.getDiscription()).isEqualTo(UPDATED_DISCRIPTION);
        assertThat(testFeedbackResponse.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testFeedbackResponse.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
        assertThat(testFeedbackResponse.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingFeedbackResponse() throws Exception {
        int databaseSizeBeforeUpdate = feedbackResponseRepository.findAll().size();
        feedbackResponse.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFeedbackResponseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, feedbackResponse.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(feedbackResponse))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeedbackResponse in the database
        List<FeedbackResponse> feedbackResponseList = feedbackResponseRepository.findAll();
        assertThat(feedbackResponseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFeedbackResponse() throws Exception {
        int databaseSizeBeforeUpdate = feedbackResponseRepository.findAll().size();
        feedbackResponse.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeedbackResponseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(feedbackResponse))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeedbackResponse in the database
        List<FeedbackResponse> feedbackResponseList = feedbackResponseRepository.findAll();
        assertThat(feedbackResponseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFeedbackResponse() throws Exception {
        int databaseSizeBeforeUpdate = feedbackResponseRepository.findAll().size();
        feedbackResponse.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeedbackResponseMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(feedbackResponse))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FeedbackResponse in the database
        List<FeedbackResponse> feedbackResponseList = feedbackResponseRepository.findAll();
        assertThat(feedbackResponseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFeedbackResponse() throws Exception {
        // Initialize the database
        feedbackResponseRepository.saveAndFlush(feedbackResponse);

        int databaseSizeBeforeDelete = feedbackResponseRepository.findAll().size();

        // Delete the feedbackResponse
        restFeedbackResponseMockMvc
            .perform(delete(ENTITY_API_URL_ID, feedbackResponse.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FeedbackResponse> feedbackResponseList = feedbackResponseRepository.findAll();
        assertThat(feedbackResponseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
