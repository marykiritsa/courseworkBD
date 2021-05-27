package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.TypeOfRelationship;
import com.mycompany.myapp.repository.TypeOfRelationshipRepository;
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

/**
 * Integration tests for the {@link TypeOfRelationshipResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TypeOfRelationshipResourceIT {

    private static final String DEFAULT_DEGREE_OF_KINSHIP = "AAAAAAAAAA";
    private static final String UPDATED_DEGREE_OF_KINSHIP = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/type-of-relationships";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TypeOfRelationshipRepository typeOfRelationshipRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTypeOfRelationshipMockMvc;

    private TypeOfRelationship typeOfRelationship;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeOfRelationship createEntity(EntityManager em) {
        TypeOfRelationship typeOfRelationship = new TypeOfRelationship().degreeOfKinship(DEFAULT_DEGREE_OF_KINSHIP);
        return typeOfRelationship;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeOfRelationship createUpdatedEntity(EntityManager em) {
        TypeOfRelationship typeOfRelationship = new TypeOfRelationship().degreeOfKinship(UPDATED_DEGREE_OF_KINSHIP);
        return typeOfRelationship;
    }

    @BeforeEach
    public void initTest() {
        typeOfRelationship = createEntity(em);
    }

    @Test
    @Transactional
    void createTypeOfRelationship() throws Exception {
        int databaseSizeBeforeCreate = typeOfRelationshipRepository.findAll().size();
        // Create the TypeOfRelationship
        restTypeOfRelationshipMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typeOfRelationship))
            )
            .andExpect(status().isCreated());

        // Validate the TypeOfRelationship in the database
        List<TypeOfRelationship> typeOfRelationshipList = typeOfRelationshipRepository.findAll();
        assertThat(typeOfRelationshipList).hasSize(databaseSizeBeforeCreate + 1);
        TypeOfRelationship testTypeOfRelationship = typeOfRelationshipList.get(typeOfRelationshipList.size() - 1);
        assertThat(testTypeOfRelationship.getDegreeOfKinship()).isEqualTo(DEFAULT_DEGREE_OF_KINSHIP);
    }

    @Test
    @Transactional
    void createTypeOfRelationshipWithExistingId() throws Exception {
        // Create the TypeOfRelationship with an existing ID
        typeOfRelationship.setId(1L);

        int databaseSizeBeforeCreate = typeOfRelationshipRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTypeOfRelationshipMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typeOfRelationship))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeOfRelationship in the database
        List<TypeOfRelationship> typeOfRelationshipList = typeOfRelationshipRepository.findAll();
        assertThat(typeOfRelationshipList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDegreeOfKinshipIsRequired() throws Exception {
        int databaseSizeBeforeTest = typeOfRelationshipRepository.findAll().size();
        // set the field null
        typeOfRelationship.setDegreeOfKinship(null);

        // Create the TypeOfRelationship, which fails.

        restTypeOfRelationshipMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typeOfRelationship))
            )
            .andExpect(status().isBadRequest());

        List<TypeOfRelationship> typeOfRelationshipList = typeOfRelationshipRepository.findAll();
        assertThat(typeOfRelationshipList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTypeOfRelationships() throws Exception {
        // Initialize the database
        typeOfRelationshipRepository.saveAndFlush(typeOfRelationship);

        // Get all the typeOfRelationshipList
        restTypeOfRelationshipMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeOfRelationship.getId().intValue())))
            .andExpect(jsonPath("$.[*].degreeOfKinship").value(hasItem(DEFAULT_DEGREE_OF_KINSHIP)));
    }

    @Test
    @Transactional
    void getTypeOfRelationship() throws Exception {
        // Initialize the database
        typeOfRelationshipRepository.saveAndFlush(typeOfRelationship);

        // Get the typeOfRelationship
        restTypeOfRelationshipMockMvc
            .perform(get(ENTITY_API_URL_ID, typeOfRelationship.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(typeOfRelationship.getId().intValue()))
            .andExpect(jsonPath("$.degreeOfKinship").value(DEFAULT_DEGREE_OF_KINSHIP));
    }

    @Test
    @Transactional
    void getNonExistingTypeOfRelationship() throws Exception {
        // Get the typeOfRelationship
        restTypeOfRelationshipMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTypeOfRelationship() throws Exception {
        // Initialize the database
        typeOfRelationshipRepository.saveAndFlush(typeOfRelationship);

        int databaseSizeBeforeUpdate = typeOfRelationshipRepository.findAll().size();

        // Update the typeOfRelationship
        TypeOfRelationship updatedTypeOfRelationship = typeOfRelationshipRepository.findById(typeOfRelationship.getId()).get();
        // Disconnect from session so that the updates on updatedTypeOfRelationship are not directly saved in db
        em.detach(updatedTypeOfRelationship);
        updatedTypeOfRelationship.degreeOfKinship(UPDATED_DEGREE_OF_KINSHIP);

        restTypeOfRelationshipMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTypeOfRelationship.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTypeOfRelationship))
            )
            .andExpect(status().isOk());

        // Validate the TypeOfRelationship in the database
        List<TypeOfRelationship> typeOfRelationshipList = typeOfRelationshipRepository.findAll();
        assertThat(typeOfRelationshipList).hasSize(databaseSizeBeforeUpdate);
        TypeOfRelationship testTypeOfRelationship = typeOfRelationshipList.get(typeOfRelationshipList.size() - 1);
        assertThat(testTypeOfRelationship.getDegreeOfKinship()).isEqualTo(UPDATED_DEGREE_OF_KINSHIP);
    }

    @Test
    @Transactional
    void putNonExistingTypeOfRelationship() throws Exception {
        int databaseSizeBeforeUpdate = typeOfRelationshipRepository.findAll().size();
        typeOfRelationship.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeOfRelationshipMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typeOfRelationship.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeOfRelationship))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeOfRelationship in the database
        List<TypeOfRelationship> typeOfRelationshipList = typeOfRelationshipRepository.findAll();
        assertThat(typeOfRelationshipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTypeOfRelationship() throws Exception {
        int databaseSizeBeforeUpdate = typeOfRelationshipRepository.findAll().size();
        typeOfRelationship.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeOfRelationshipMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeOfRelationship))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeOfRelationship in the database
        List<TypeOfRelationship> typeOfRelationshipList = typeOfRelationshipRepository.findAll();
        assertThat(typeOfRelationshipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTypeOfRelationship() throws Exception {
        int databaseSizeBeforeUpdate = typeOfRelationshipRepository.findAll().size();
        typeOfRelationship.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeOfRelationshipMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typeOfRelationship))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeOfRelationship in the database
        List<TypeOfRelationship> typeOfRelationshipList = typeOfRelationshipRepository.findAll();
        assertThat(typeOfRelationshipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTypeOfRelationshipWithPatch() throws Exception {
        // Initialize the database
        typeOfRelationshipRepository.saveAndFlush(typeOfRelationship);

        int databaseSizeBeforeUpdate = typeOfRelationshipRepository.findAll().size();

        // Update the typeOfRelationship using partial update
        TypeOfRelationship partialUpdatedTypeOfRelationship = new TypeOfRelationship();
        partialUpdatedTypeOfRelationship.setId(typeOfRelationship.getId());

        partialUpdatedTypeOfRelationship.degreeOfKinship(UPDATED_DEGREE_OF_KINSHIP);

        restTypeOfRelationshipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeOfRelationship.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypeOfRelationship))
            )
            .andExpect(status().isOk());

        // Validate the TypeOfRelationship in the database
        List<TypeOfRelationship> typeOfRelationshipList = typeOfRelationshipRepository.findAll();
        assertThat(typeOfRelationshipList).hasSize(databaseSizeBeforeUpdate);
        TypeOfRelationship testTypeOfRelationship = typeOfRelationshipList.get(typeOfRelationshipList.size() - 1);
        assertThat(testTypeOfRelationship.getDegreeOfKinship()).isEqualTo(UPDATED_DEGREE_OF_KINSHIP);
    }

    @Test
    @Transactional
    void fullUpdateTypeOfRelationshipWithPatch() throws Exception {
        // Initialize the database
        typeOfRelationshipRepository.saveAndFlush(typeOfRelationship);

        int databaseSizeBeforeUpdate = typeOfRelationshipRepository.findAll().size();

        // Update the typeOfRelationship using partial update
        TypeOfRelationship partialUpdatedTypeOfRelationship = new TypeOfRelationship();
        partialUpdatedTypeOfRelationship.setId(typeOfRelationship.getId());

        partialUpdatedTypeOfRelationship.degreeOfKinship(UPDATED_DEGREE_OF_KINSHIP);

        restTypeOfRelationshipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeOfRelationship.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypeOfRelationship))
            )
            .andExpect(status().isOk());

        // Validate the TypeOfRelationship in the database
        List<TypeOfRelationship> typeOfRelationshipList = typeOfRelationshipRepository.findAll();
        assertThat(typeOfRelationshipList).hasSize(databaseSizeBeforeUpdate);
        TypeOfRelationship testTypeOfRelationship = typeOfRelationshipList.get(typeOfRelationshipList.size() - 1);
        assertThat(testTypeOfRelationship.getDegreeOfKinship()).isEqualTo(UPDATED_DEGREE_OF_KINSHIP);
    }

    @Test
    @Transactional
    void patchNonExistingTypeOfRelationship() throws Exception {
        int databaseSizeBeforeUpdate = typeOfRelationshipRepository.findAll().size();
        typeOfRelationship.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeOfRelationshipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, typeOfRelationship.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeOfRelationship))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeOfRelationship in the database
        List<TypeOfRelationship> typeOfRelationshipList = typeOfRelationshipRepository.findAll();
        assertThat(typeOfRelationshipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTypeOfRelationship() throws Exception {
        int databaseSizeBeforeUpdate = typeOfRelationshipRepository.findAll().size();
        typeOfRelationship.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeOfRelationshipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeOfRelationship))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeOfRelationship in the database
        List<TypeOfRelationship> typeOfRelationshipList = typeOfRelationshipRepository.findAll();
        assertThat(typeOfRelationshipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTypeOfRelationship() throws Exception {
        int databaseSizeBeforeUpdate = typeOfRelationshipRepository.findAll().size();
        typeOfRelationship.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeOfRelationshipMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeOfRelationship))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeOfRelationship in the database
        List<TypeOfRelationship> typeOfRelationshipList = typeOfRelationshipRepository.findAll();
        assertThat(typeOfRelationshipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTypeOfRelationship() throws Exception {
        // Initialize the database
        typeOfRelationshipRepository.saveAndFlush(typeOfRelationship);

        int databaseSizeBeforeDelete = typeOfRelationshipRepository.findAll().size();

        // Delete the typeOfRelationship
        restTypeOfRelationshipMockMvc
            .perform(delete(ENTITY_API_URL_ID, typeOfRelationship.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TypeOfRelationship> typeOfRelationshipList = typeOfRelationshipRepository.findAll();
        assertThat(typeOfRelationshipList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
