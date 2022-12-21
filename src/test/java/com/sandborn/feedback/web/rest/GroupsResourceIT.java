package com.sandborn.feedback.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sandborn.feedback.IntegrationTest;
import com.sandborn.feedback.domain.Groups;
import com.sandborn.feedback.repository.GroupsRepository;
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
 * Integration tests for the {@link GroupsResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class GroupsResourceIT {

    private static final String DEFAULT_GROUP_NAME = "AAAAAAAAAA";
    private static final String UPDATED_GROUP_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DISCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DISCRIPTION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_PICTURE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PICTURE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PICTURE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PICTURE_CONTENT_TYPE = "image/png";

    private static final Boolean DEFAULT_STATUS = false;
    private static final Boolean UPDATED_STATUS = true;

    private static final String ENTITY_API_URL = "/api/groups";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GroupsRepository groupsRepository;

    @Mock
    private GroupsRepository groupsRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGroupsMockMvc;

    private Groups groups;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Groups createEntity(EntityManager em) {
        Groups groups = new Groups()
            .groupName(DEFAULT_GROUP_NAME)
            .discription(DEFAULT_DISCRIPTION)
            .picture(DEFAULT_PICTURE)
            .pictureContentType(DEFAULT_PICTURE_CONTENT_TYPE)
            .status(DEFAULT_STATUS);
        return groups;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Groups createUpdatedEntity(EntityManager em) {
        Groups groups = new Groups()
            .groupName(UPDATED_GROUP_NAME)
            .discription(UPDATED_DISCRIPTION)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .status(UPDATED_STATUS);
        return groups;
    }

    @BeforeEach
    public void initTest() {
        groups = createEntity(em);
    }

    @Test
    @Transactional
    void createGroups() throws Exception {
        int databaseSizeBeforeCreate = groupsRepository.findAll().size();
        // Create the Groups
        restGroupsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(groups)))
            .andExpect(status().isCreated());

        // Validate the Groups in the database
        List<Groups> groupsList = groupsRepository.findAll();
        assertThat(groupsList).hasSize(databaseSizeBeforeCreate + 1);
        Groups testGroups = groupsList.get(groupsList.size() - 1);
        assertThat(testGroups.getGroupName()).isEqualTo(DEFAULT_GROUP_NAME);
        assertThat(testGroups.getDiscription()).isEqualTo(DEFAULT_DISCRIPTION);
        assertThat(testGroups.getPicture()).isEqualTo(DEFAULT_PICTURE);
        assertThat(testGroups.getPictureContentType()).isEqualTo(DEFAULT_PICTURE_CONTENT_TYPE);
        assertThat(testGroups.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createGroupsWithExistingId() throws Exception {
        // Create the Groups with an existing ID
        groups.setId(1L);

        int databaseSizeBeforeCreate = groupsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGroupsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(groups)))
            .andExpect(status().isBadRequest());

        // Validate the Groups in the database
        List<Groups> groupsList = groupsRepository.findAll();
        assertThat(groupsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllGroups() throws Exception {
        // Initialize the database
        groupsRepository.saveAndFlush(groups);

        // Get all the groupsList
        restGroupsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(groups.getId().intValue())))
            .andExpect(jsonPath("$.[*].groupName").value(hasItem(DEFAULT_GROUP_NAME)))
            .andExpect(jsonPath("$.[*].discription").value(hasItem(DEFAULT_DISCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].pictureContentType").value(hasItem(DEFAULT_PICTURE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].picture").value(hasItem(Base64Utils.encodeToString(DEFAULT_PICTURE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllGroupsWithEagerRelationshipsIsEnabled() throws Exception {
        when(groupsRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restGroupsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(groupsRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllGroupsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(groupsRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restGroupsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(groupsRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getGroups() throws Exception {
        // Initialize the database
        groupsRepository.saveAndFlush(groups);

        // Get the groups
        restGroupsMockMvc
            .perform(get(ENTITY_API_URL_ID, groups.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(groups.getId().intValue()))
            .andExpect(jsonPath("$.groupName").value(DEFAULT_GROUP_NAME))
            .andExpect(jsonPath("$.discription").value(DEFAULT_DISCRIPTION.toString()))
            .andExpect(jsonPath("$.pictureContentType").value(DEFAULT_PICTURE_CONTENT_TYPE))
            .andExpect(jsonPath("$.picture").value(Base64Utils.encodeToString(DEFAULT_PICTURE)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingGroups() throws Exception {
        // Get the groups
        restGroupsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingGroups() throws Exception {
        // Initialize the database
        groupsRepository.saveAndFlush(groups);

        int databaseSizeBeforeUpdate = groupsRepository.findAll().size();

        // Update the groups
        Groups updatedGroups = groupsRepository.findById(groups.getId()).get();
        // Disconnect from session so that the updates on updatedGroups are not directly saved in db
        em.detach(updatedGroups);
        updatedGroups
            .groupName(UPDATED_GROUP_NAME)
            .discription(UPDATED_DISCRIPTION)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .status(UPDATED_STATUS);

        restGroupsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedGroups.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedGroups))
            )
            .andExpect(status().isOk());

        // Validate the Groups in the database
        List<Groups> groupsList = groupsRepository.findAll();
        assertThat(groupsList).hasSize(databaseSizeBeforeUpdate);
        Groups testGroups = groupsList.get(groupsList.size() - 1);
        assertThat(testGroups.getGroupName()).isEqualTo(UPDATED_GROUP_NAME);
        assertThat(testGroups.getDiscription()).isEqualTo(UPDATED_DISCRIPTION);
        assertThat(testGroups.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testGroups.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
        assertThat(testGroups.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingGroups() throws Exception {
        int databaseSizeBeforeUpdate = groupsRepository.findAll().size();
        groups.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGroupsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, groups.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(groups))
            )
            .andExpect(status().isBadRequest());

        // Validate the Groups in the database
        List<Groups> groupsList = groupsRepository.findAll();
        assertThat(groupsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGroups() throws Exception {
        int databaseSizeBeforeUpdate = groupsRepository.findAll().size();
        groups.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGroupsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(groups))
            )
            .andExpect(status().isBadRequest());

        // Validate the Groups in the database
        List<Groups> groupsList = groupsRepository.findAll();
        assertThat(groupsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGroups() throws Exception {
        int databaseSizeBeforeUpdate = groupsRepository.findAll().size();
        groups.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGroupsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(groups)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Groups in the database
        List<Groups> groupsList = groupsRepository.findAll();
        assertThat(groupsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGroupsWithPatch() throws Exception {
        // Initialize the database
        groupsRepository.saveAndFlush(groups);

        int databaseSizeBeforeUpdate = groupsRepository.findAll().size();

        // Update the groups using partial update
        Groups partialUpdatedGroups = new Groups();
        partialUpdatedGroups.setId(groups.getId());

        partialUpdatedGroups.discription(UPDATED_DISCRIPTION).picture(UPDATED_PICTURE).pictureContentType(UPDATED_PICTURE_CONTENT_TYPE);

        restGroupsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGroups.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGroups))
            )
            .andExpect(status().isOk());

        // Validate the Groups in the database
        List<Groups> groupsList = groupsRepository.findAll();
        assertThat(groupsList).hasSize(databaseSizeBeforeUpdate);
        Groups testGroups = groupsList.get(groupsList.size() - 1);
        assertThat(testGroups.getGroupName()).isEqualTo(DEFAULT_GROUP_NAME);
        assertThat(testGroups.getDiscription()).isEqualTo(UPDATED_DISCRIPTION);
        assertThat(testGroups.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testGroups.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
        assertThat(testGroups.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateGroupsWithPatch() throws Exception {
        // Initialize the database
        groupsRepository.saveAndFlush(groups);

        int databaseSizeBeforeUpdate = groupsRepository.findAll().size();

        // Update the groups using partial update
        Groups partialUpdatedGroups = new Groups();
        partialUpdatedGroups.setId(groups.getId());

        partialUpdatedGroups
            .groupName(UPDATED_GROUP_NAME)
            .discription(UPDATED_DISCRIPTION)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .status(UPDATED_STATUS);

        restGroupsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGroups.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGroups))
            )
            .andExpect(status().isOk());

        // Validate the Groups in the database
        List<Groups> groupsList = groupsRepository.findAll();
        assertThat(groupsList).hasSize(databaseSizeBeforeUpdate);
        Groups testGroups = groupsList.get(groupsList.size() - 1);
        assertThat(testGroups.getGroupName()).isEqualTo(UPDATED_GROUP_NAME);
        assertThat(testGroups.getDiscription()).isEqualTo(UPDATED_DISCRIPTION);
        assertThat(testGroups.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testGroups.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
        assertThat(testGroups.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingGroups() throws Exception {
        int databaseSizeBeforeUpdate = groupsRepository.findAll().size();
        groups.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGroupsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, groups.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(groups))
            )
            .andExpect(status().isBadRequest());

        // Validate the Groups in the database
        List<Groups> groupsList = groupsRepository.findAll();
        assertThat(groupsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGroups() throws Exception {
        int databaseSizeBeforeUpdate = groupsRepository.findAll().size();
        groups.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGroupsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(groups))
            )
            .andExpect(status().isBadRequest());

        // Validate the Groups in the database
        List<Groups> groupsList = groupsRepository.findAll();
        assertThat(groupsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGroups() throws Exception {
        int databaseSizeBeforeUpdate = groupsRepository.findAll().size();
        groups.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGroupsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(groups)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Groups in the database
        List<Groups> groupsList = groupsRepository.findAll();
        assertThat(groupsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGroups() throws Exception {
        // Initialize the database
        groupsRepository.saveAndFlush(groups);

        int databaseSizeBeforeDelete = groupsRepository.findAll().size();

        // Delete the groups
        restGroupsMockMvc
            .perform(delete(ENTITY_API_URL_ID, groups.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Groups> groupsList = groupsRepository.findAll();
        assertThat(groupsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
