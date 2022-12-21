package com.sandborn.feedback.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sandborn.feedback.IntegrationTest;
import com.sandborn.feedback.domain.FeedbackAbout;
import com.sandborn.feedback.repository.FeedbackAboutRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link FeedbackAboutResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FeedbackAboutResourceIT {

    private static final String DEFAULT_FEEDBACKABOUT = "AAAAAAAAAA";
    private static final String UPDATED_FEEDBACKABOUT = "BBBBBBBBBB";

    private static final String DEFAULT_DISCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DISCRIPTION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_PICTURE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PICTURE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PICTURE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PICTURE_CONTENT_TYPE = "image/png";

    private static final Boolean DEFAULT_STATUS = false;
    private static final Boolean UPDATED_STATUS = true;

    private static final String ENTITY_API_URL = "/api/feedback-abouts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FeedbackAboutRepository feedbackAboutRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFeedbackAboutMockMvc;

    private FeedbackAbout feedbackAbout;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FeedbackAbout createEntity(EntityManager em) {
        FeedbackAbout feedbackAbout = new FeedbackAbout()
            .feedbackabout(DEFAULT_FEEDBACKABOUT)
            .discription(DEFAULT_DISCRIPTION)
            .picture(DEFAULT_PICTURE)
            .pictureContentType(DEFAULT_PICTURE_CONTENT_TYPE)
            .status(DEFAULT_STATUS);
        return feedbackAbout;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FeedbackAbout createUpdatedEntity(EntityManager em) {
        FeedbackAbout feedbackAbout = new FeedbackAbout()
            .feedbackabout(UPDATED_FEEDBACKABOUT)
            .discription(UPDATED_DISCRIPTION)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .status(UPDATED_STATUS);
        return feedbackAbout;
    }

    @BeforeEach
    public void initTest() {
        feedbackAbout = createEntity(em);
    }

    @Test
    @Transactional
    void createFeedbackAbout() throws Exception {
        int databaseSizeBeforeCreate = feedbackAboutRepository.findAll().size();
        // Create the FeedbackAbout
        restFeedbackAboutMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(feedbackAbout)))
            .andExpect(status().isCreated());

        // Validate the FeedbackAbout in the database
        List<FeedbackAbout> feedbackAboutList = feedbackAboutRepository.findAll();
        assertThat(feedbackAboutList).hasSize(databaseSizeBeforeCreate + 1);
        FeedbackAbout testFeedbackAbout = feedbackAboutList.get(feedbackAboutList.size() - 1);
        assertThat(testFeedbackAbout.getFeedbackabout()).isEqualTo(DEFAULT_FEEDBACKABOUT);
        assertThat(testFeedbackAbout.getDiscription()).isEqualTo(DEFAULT_DISCRIPTION);
        assertThat(testFeedbackAbout.getPicture()).isEqualTo(DEFAULT_PICTURE);
        assertThat(testFeedbackAbout.getPictureContentType()).isEqualTo(DEFAULT_PICTURE_CONTENT_TYPE);
        assertThat(testFeedbackAbout.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createFeedbackAboutWithExistingId() throws Exception {
        // Create the FeedbackAbout with an existing ID
        feedbackAbout.setId(1L);

        int databaseSizeBeforeCreate = feedbackAboutRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFeedbackAboutMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(feedbackAbout)))
            .andExpect(status().isBadRequest());

        // Validate the FeedbackAbout in the database
        List<FeedbackAbout> feedbackAboutList = feedbackAboutRepository.findAll();
        assertThat(feedbackAboutList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFeedbackAbouts() throws Exception {
        // Initialize the database
        feedbackAboutRepository.saveAndFlush(feedbackAbout);

        // Get all the feedbackAboutList
        restFeedbackAboutMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(feedbackAbout.getId().intValue())))
            .andExpect(jsonPath("$.[*].feedbackabout").value(hasItem(DEFAULT_FEEDBACKABOUT)))
            .andExpect(jsonPath("$.[*].discription").value(hasItem(DEFAULT_DISCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].pictureContentType").value(hasItem(DEFAULT_PICTURE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].picture").value(hasItem(Base64Utils.encodeToString(DEFAULT_PICTURE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())));
    }

    @Test
    @Transactional
    void getFeedbackAbout() throws Exception {
        // Initialize the database
        feedbackAboutRepository.saveAndFlush(feedbackAbout);

        // Get the feedbackAbout
        restFeedbackAboutMockMvc
            .perform(get(ENTITY_API_URL_ID, feedbackAbout.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(feedbackAbout.getId().intValue()))
            .andExpect(jsonPath("$.feedbackabout").value(DEFAULT_FEEDBACKABOUT))
            .andExpect(jsonPath("$.discription").value(DEFAULT_DISCRIPTION.toString()))
            .andExpect(jsonPath("$.pictureContentType").value(DEFAULT_PICTURE_CONTENT_TYPE))
            .andExpect(jsonPath("$.picture").value(Base64Utils.encodeToString(DEFAULT_PICTURE)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingFeedbackAbout() throws Exception {
        // Get the feedbackAbout
        restFeedbackAboutMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFeedbackAbout() throws Exception {
        // Initialize the database
        feedbackAboutRepository.saveAndFlush(feedbackAbout);

        int databaseSizeBeforeUpdate = feedbackAboutRepository.findAll().size();

        // Update the feedbackAbout
        FeedbackAbout updatedFeedbackAbout = feedbackAboutRepository.findById(feedbackAbout.getId()).get();
        // Disconnect from session so that the updates on updatedFeedbackAbout are not directly saved in db
        em.detach(updatedFeedbackAbout);
        updatedFeedbackAbout
            .feedbackabout(UPDATED_FEEDBACKABOUT)
            .discription(UPDATED_DISCRIPTION)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .status(UPDATED_STATUS);

        restFeedbackAboutMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFeedbackAbout.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFeedbackAbout))
            )
            .andExpect(status().isOk());

        // Validate the FeedbackAbout in the database
        List<FeedbackAbout> feedbackAboutList = feedbackAboutRepository.findAll();
        assertThat(feedbackAboutList).hasSize(databaseSizeBeforeUpdate);
        FeedbackAbout testFeedbackAbout = feedbackAboutList.get(feedbackAboutList.size() - 1);
        assertThat(testFeedbackAbout.getFeedbackabout()).isEqualTo(UPDATED_FEEDBACKABOUT);
        assertThat(testFeedbackAbout.getDiscription()).isEqualTo(UPDATED_DISCRIPTION);
        assertThat(testFeedbackAbout.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testFeedbackAbout.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
        assertThat(testFeedbackAbout.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingFeedbackAbout() throws Exception {
        int databaseSizeBeforeUpdate = feedbackAboutRepository.findAll().size();
        feedbackAbout.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFeedbackAboutMockMvc
            .perform(
                put(ENTITY_API_URL_ID, feedbackAbout.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(feedbackAbout))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeedbackAbout in the database
        List<FeedbackAbout> feedbackAboutList = feedbackAboutRepository.findAll();
        assertThat(feedbackAboutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFeedbackAbout() throws Exception {
        int databaseSizeBeforeUpdate = feedbackAboutRepository.findAll().size();
        feedbackAbout.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeedbackAboutMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(feedbackAbout))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeedbackAbout in the database
        List<FeedbackAbout> feedbackAboutList = feedbackAboutRepository.findAll();
        assertThat(feedbackAboutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFeedbackAbout() throws Exception {
        int databaseSizeBeforeUpdate = feedbackAboutRepository.findAll().size();
        feedbackAbout.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeedbackAboutMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(feedbackAbout)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FeedbackAbout in the database
        List<FeedbackAbout> feedbackAboutList = feedbackAboutRepository.findAll();
        assertThat(feedbackAboutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFeedbackAboutWithPatch() throws Exception {
        // Initialize the database
        feedbackAboutRepository.saveAndFlush(feedbackAbout);

        int databaseSizeBeforeUpdate = feedbackAboutRepository.findAll().size();

        // Update the feedbackAbout using partial update
        FeedbackAbout partialUpdatedFeedbackAbout = new FeedbackAbout();
        partialUpdatedFeedbackAbout.setId(feedbackAbout.getId());

        partialUpdatedFeedbackAbout.picture(UPDATED_PICTURE).pictureContentType(UPDATED_PICTURE_CONTENT_TYPE);

        restFeedbackAboutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFeedbackAbout.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFeedbackAbout))
            )
            .andExpect(status().isOk());

        // Validate the FeedbackAbout in the database
        List<FeedbackAbout> feedbackAboutList = feedbackAboutRepository.findAll();
        assertThat(feedbackAboutList).hasSize(databaseSizeBeforeUpdate);
        FeedbackAbout testFeedbackAbout = feedbackAboutList.get(feedbackAboutList.size() - 1);
        assertThat(testFeedbackAbout.getFeedbackabout()).isEqualTo(DEFAULT_FEEDBACKABOUT);
        assertThat(testFeedbackAbout.getDiscription()).isEqualTo(DEFAULT_DISCRIPTION);
        assertThat(testFeedbackAbout.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testFeedbackAbout.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
        assertThat(testFeedbackAbout.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateFeedbackAboutWithPatch() throws Exception {
        // Initialize the database
        feedbackAboutRepository.saveAndFlush(feedbackAbout);

        int databaseSizeBeforeUpdate = feedbackAboutRepository.findAll().size();

        // Update the feedbackAbout using partial update
        FeedbackAbout partialUpdatedFeedbackAbout = new FeedbackAbout();
        partialUpdatedFeedbackAbout.setId(feedbackAbout.getId());

        partialUpdatedFeedbackAbout
            .feedbackabout(UPDATED_FEEDBACKABOUT)
            .discription(UPDATED_DISCRIPTION)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .status(UPDATED_STATUS);

        restFeedbackAboutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFeedbackAbout.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFeedbackAbout))
            )
            .andExpect(status().isOk());

        // Validate the FeedbackAbout in the database
        List<FeedbackAbout> feedbackAboutList = feedbackAboutRepository.findAll();
        assertThat(feedbackAboutList).hasSize(databaseSizeBeforeUpdate);
        FeedbackAbout testFeedbackAbout = feedbackAboutList.get(feedbackAboutList.size() - 1);
        assertThat(testFeedbackAbout.getFeedbackabout()).isEqualTo(UPDATED_FEEDBACKABOUT);
        assertThat(testFeedbackAbout.getDiscription()).isEqualTo(UPDATED_DISCRIPTION);
        assertThat(testFeedbackAbout.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testFeedbackAbout.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
        assertThat(testFeedbackAbout.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingFeedbackAbout() throws Exception {
        int databaseSizeBeforeUpdate = feedbackAboutRepository.findAll().size();
        feedbackAbout.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFeedbackAboutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, feedbackAbout.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(feedbackAbout))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeedbackAbout in the database
        List<FeedbackAbout> feedbackAboutList = feedbackAboutRepository.findAll();
        assertThat(feedbackAboutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFeedbackAbout() throws Exception {
        int databaseSizeBeforeUpdate = feedbackAboutRepository.findAll().size();
        feedbackAbout.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeedbackAboutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(feedbackAbout))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeedbackAbout in the database
        List<FeedbackAbout> feedbackAboutList = feedbackAboutRepository.findAll();
        assertThat(feedbackAboutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFeedbackAbout() throws Exception {
        int databaseSizeBeforeUpdate = feedbackAboutRepository.findAll().size();
        feedbackAbout.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeedbackAboutMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(feedbackAbout))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FeedbackAbout in the database
        List<FeedbackAbout> feedbackAboutList = feedbackAboutRepository.findAll();
        assertThat(feedbackAboutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFeedbackAbout() throws Exception {
        // Initialize the database
        feedbackAboutRepository.saveAndFlush(feedbackAbout);

        int databaseSizeBeforeDelete = feedbackAboutRepository.findAll().size();

        // Delete the feedbackAbout
        restFeedbackAboutMockMvc
            .perform(delete(ENTITY_API_URL_ID, feedbackAbout.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FeedbackAbout> feedbackAboutList = feedbackAboutRepository.findAll();
        assertThat(feedbackAboutList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
