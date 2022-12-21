package com.sandborn.feedback.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sandborn.feedback.IntegrationTest;
import com.sandborn.feedback.domain.Designation;
import com.sandborn.feedback.repository.DesignationRepository;
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
 * Integration tests for the {@link DesignationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DesignationResourceIT {

    private static final String DEFAULT_DESIGNATION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DESIGNATION_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DISCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DISCRIPTION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_PICTURE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PICTURE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PICTURE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PICTURE_CONTENT_TYPE = "image/png";

    private static final Boolean DEFAULT_STATUS = false;
    private static final Boolean UPDATED_STATUS = true;

    private static final String ENTITY_API_URL = "/api/designations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DesignationRepository designationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDesignationMockMvc;

    private Designation designation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Designation createEntity(EntityManager em) {
        Designation designation = new Designation()
            .designationName(DEFAULT_DESIGNATION_NAME)
            .discription(DEFAULT_DISCRIPTION)
            .picture(DEFAULT_PICTURE)
            .pictureContentType(DEFAULT_PICTURE_CONTENT_TYPE)
            .status(DEFAULT_STATUS);
        return designation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Designation createUpdatedEntity(EntityManager em) {
        Designation designation = new Designation()
            .designationName(UPDATED_DESIGNATION_NAME)
            .discription(UPDATED_DISCRIPTION)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .status(UPDATED_STATUS);
        return designation;
    }

    @BeforeEach
    public void initTest() {
        designation = createEntity(em);
    }

    @Test
    @Transactional
    void createDesignation() throws Exception {
        int databaseSizeBeforeCreate = designationRepository.findAll().size();
        // Create the Designation
        restDesignationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(designation)))
            .andExpect(status().isCreated());

        // Validate the Designation in the database
        List<Designation> designationList = designationRepository.findAll();
        assertThat(designationList).hasSize(databaseSizeBeforeCreate + 1);
        Designation testDesignation = designationList.get(designationList.size() - 1);
        assertThat(testDesignation.getDesignationName()).isEqualTo(DEFAULT_DESIGNATION_NAME);
        assertThat(testDesignation.getDiscription()).isEqualTo(DEFAULT_DISCRIPTION);
        assertThat(testDesignation.getPicture()).isEqualTo(DEFAULT_PICTURE);
        assertThat(testDesignation.getPictureContentType()).isEqualTo(DEFAULT_PICTURE_CONTENT_TYPE);
        assertThat(testDesignation.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createDesignationWithExistingId() throws Exception {
        // Create the Designation with an existing ID
        designation.setId(1L);

        int databaseSizeBeforeCreate = designationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDesignationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(designation)))
            .andExpect(status().isBadRequest());

        // Validate the Designation in the database
        List<Designation> designationList = designationRepository.findAll();
        assertThat(designationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDesignations() throws Exception {
        // Initialize the database
        designationRepository.saveAndFlush(designation);

        // Get all the designationList
        restDesignationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(designation.getId().intValue())))
            .andExpect(jsonPath("$.[*].designationName").value(hasItem(DEFAULT_DESIGNATION_NAME)))
            .andExpect(jsonPath("$.[*].discription").value(hasItem(DEFAULT_DISCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].pictureContentType").value(hasItem(DEFAULT_PICTURE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].picture").value(hasItem(Base64Utils.encodeToString(DEFAULT_PICTURE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())));
    }

    @Test
    @Transactional
    void getDesignation() throws Exception {
        // Initialize the database
        designationRepository.saveAndFlush(designation);

        // Get the designation
        restDesignationMockMvc
            .perform(get(ENTITY_API_URL_ID, designation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(designation.getId().intValue()))
            .andExpect(jsonPath("$.designationName").value(DEFAULT_DESIGNATION_NAME))
            .andExpect(jsonPath("$.discription").value(DEFAULT_DISCRIPTION.toString()))
            .andExpect(jsonPath("$.pictureContentType").value(DEFAULT_PICTURE_CONTENT_TYPE))
            .andExpect(jsonPath("$.picture").value(Base64Utils.encodeToString(DEFAULT_PICTURE)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingDesignation() throws Exception {
        // Get the designation
        restDesignationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDesignation() throws Exception {
        // Initialize the database
        designationRepository.saveAndFlush(designation);

        int databaseSizeBeforeUpdate = designationRepository.findAll().size();

        // Update the designation
        Designation updatedDesignation = designationRepository.findById(designation.getId()).get();
        // Disconnect from session so that the updates on updatedDesignation are not directly saved in db
        em.detach(updatedDesignation);
        updatedDesignation
            .designationName(UPDATED_DESIGNATION_NAME)
            .discription(UPDATED_DISCRIPTION)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .status(UPDATED_STATUS);

        restDesignationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDesignation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDesignation))
            )
            .andExpect(status().isOk());

        // Validate the Designation in the database
        List<Designation> designationList = designationRepository.findAll();
        assertThat(designationList).hasSize(databaseSizeBeforeUpdate);
        Designation testDesignation = designationList.get(designationList.size() - 1);
        assertThat(testDesignation.getDesignationName()).isEqualTo(UPDATED_DESIGNATION_NAME);
        assertThat(testDesignation.getDiscription()).isEqualTo(UPDATED_DISCRIPTION);
        assertThat(testDesignation.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testDesignation.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
        assertThat(testDesignation.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingDesignation() throws Exception {
        int databaseSizeBeforeUpdate = designationRepository.findAll().size();
        designation.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDesignationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, designation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(designation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Designation in the database
        List<Designation> designationList = designationRepository.findAll();
        assertThat(designationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDesignation() throws Exception {
        int databaseSizeBeforeUpdate = designationRepository.findAll().size();
        designation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDesignationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(designation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Designation in the database
        List<Designation> designationList = designationRepository.findAll();
        assertThat(designationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDesignation() throws Exception {
        int databaseSizeBeforeUpdate = designationRepository.findAll().size();
        designation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDesignationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(designation)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Designation in the database
        List<Designation> designationList = designationRepository.findAll();
        assertThat(designationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDesignationWithPatch() throws Exception {
        // Initialize the database
        designationRepository.saveAndFlush(designation);

        int databaseSizeBeforeUpdate = designationRepository.findAll().size();

        // Update the designation using partial update
        Designation partialUpdatedDesignation = new Designation();
        partialUpdatedDesignation.setId(designation.getId());

        partialUpdatedDesignation
            .designationName(UPDATED_DESIGNATION_NAME)
            .discription(UPDATED_DISCRIPTION)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .status(UPDATED_STATUS);

        restDesignationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDesignation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDesignation))
            )
            .andExpect(status().isOk());

        // Validate the Designation in the database
        List<Designation> designationList = designationRepository.findAll();
        assertThat(designationList).hasSize(databaseSizeBeforeUpdate);
        Designation testDesignation = designationList.get(designationList.size() - 1);
        assertThat(testDesignation.getDesignationName()).isEqualTo(UPDATED_DESIGNATION_NAME);
        assertThat(testDesignation.getDiscription()).isEqualTo(UPDATED_DISCRIPTION);
        assertThat(testDesignation.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testDesignation.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
        assertThat(testDesignation.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateDesignationWithPatch() throws Exception {
        // Initialize the database
        designationRepository.saveAndFlush(designation);

        int databaseSizeBeforeUpdate = designationRepository.findAll().size();

        // Update the designation using partial update
        Designation partialUpdatedDesignation = new Designation();
        partialUpdatedDesignation.setId(designation.getId());

        partialUpdatedDesignation
            .designationName(UPDATED_DESIGNATION_NAME)
            .discription(UPDATED_DISCRIPTION)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .status(UPDATED_STATUS);

        restDesignationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDesignation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDesignation))
            )
            .andExpect(status().isOk());

        // Validate the Designation in the database
        List<Designation> designationList = designationRepository.findAll();
        assertThat(designationList).hasSize(databaseSizeBeforeUpdate);
        Designation testDesignation = designationList.get(designationList.size() - 1);
        assertThat(testDesignation.getDesignationName()).isEqualTo(UPDATED_DESIGNATION_NAME);
        assertThat(testDesignation.getDiscription()).isEqualTo(UPDATED_DISCRIPTION);
        assertThat(testDesignation.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testDesignation.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
        assertThat(testDesignation.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingDesignation() throws Exception {
        int databaseSizeBeforeUpdate = designationRepository.findAll().size();
        designation.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDesignationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, designation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(designation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Designation in the database
        List<Designation> designationList = designationRepository.findAll();
        assertThat(designationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDesignation() throws Exception {
        int databaseSizeBeforeUpdate = designationRepository.findAll().size();
        designation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDesignationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(designation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Designation in the database
        List<Designation> designationList = designationRepository.findAll();
        assertThat(designationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDesignation() throws Exception {
        int databaseSizeBeforeUpdate = designationRepository.findAll().size();
        designation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDesignationMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(designation))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Designation in the database
        List<Designation> designationList = designationRepository.findAll();
        assertThat(designationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDesignation() throws Exception {
        // Initialize the database
        designationRepository.saveAndFlush(designation);

        int databaseSizeBeforeDelete = designationRepository.findAll().size();

        // Delete the designation
        restDesignationMockMvc
            .perform(delete(ENTITY_API_URL_ID, designation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Designation> designationList = designationRepository.findAll();
        assertThat(designationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
