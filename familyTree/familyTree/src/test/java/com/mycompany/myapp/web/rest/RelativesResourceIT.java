package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Relatives;
import com.mycompany.myapp.repository.RelativesRepository;
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
 * Integration tests for the {@link RelativesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RelativesResourceIT {

    private static final String DEFAULT_SURNAME = "AAAAAAAAAA";
    private static final String UPDATED_SURNAME = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PATRONYMIC = "AAAAAAAAAA";
    private static final String UPDATED_PATRONYMIC = "BBBBBBBBBB";

    private static final Integer DEFAULT_TYPE_OF_RELATIONSHIP = 0;
    private static final Integer UPDATED_TYPE_OF_RELATIONSHIP = 1;

    private static final String ENTITY_API_URL = "/api/relatives";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RelativesRepository relativesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRelativesMockMvc;

    private Relatives relatives;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Relatives createEntity(EntityManager em) {
        Relatives relatives = new Relatives()
            .surname(DEFAULT_SURNAME)
            .name(DEFAULT_NAME)
            .patronymic(DEFAULT_PATRONYMIC)
            .typeOfRelationship(DEFAULT_TYPE_OF_RELATIONSHIP);
        return relatives;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Relatives createUpdatedEntity(EntityManager em) {
        Relatives relatives = new Relatives()
            .surname(UPDATED_SURNAME)
            .name(UPDATED_NAME)
            .patronymic(UPDATED_PATRONYMIC)
            .typeOfRelationship(UPDATED_TYPE_OF_RELATIONSHIP);
        return relatives;
    }

    @BeforeEach
    public void initTest() {
        relatives = createEntity(em);
    }

    @Test
    @Transactional
    void createRelatives() throws Exception {
        int databaseSizeBeforeCreate = relativesRepository.findAll().size();
        // Create the Relatives
        restRelativesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(relatives)))
            .andExpect(status().isCreated());

        // Validate the Relatives in the database
        List<Relatives> relativesList = relativesRepository.findAll();
        assertThat(relativesList).hasSize(databaseSizeBeforeCreate + 1);
        Relatives testRelatives = relativesList.get(relativesList.size() - 1);
        assertThat(testRelatives.getSurname()).isEqualTo(DEFAULT_SURNAME);
        assertThat(testRelatives.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRelatives.getPatronymic()).isEqualTo(DEFAULT_PATRONYMIC);
        assertThat(testRelatives.getTypeOfRelationship()).isEqualTo(DEFAULT_TYPE_OF_RELATIONSHIP);
    }

    @Test
    @Transactional
    void createRelativesWithExistingId() throws Exception {
        // Create the Relatives with an existing ID
        relatives.setId(1L);

        int databaseSizeBeforeCreate = relativesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRelativesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(relatives)))
            .andExpect(status().isBadRequest());

        // Validate the Relatives in the database
        List<Relatives> relativesList = relativesRepository.findAll();
        assertThat(relativesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSurnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = relativesRepository.findAll().size();
        // set the field null
        relatives.setSurname(null);

        // Create the Relatives, which fails.

        restRelativesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(relatives)))
            .andExpect(status().isBadRequest());

        List<Relatives> relativesList = relativesRepository.findAll();
        assertThat(relativesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = relativesRepository.findAll().size();
        // set the field null
        relatives.setName(null);

        // Create the Relatives, which fails.

        restRelativesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(relatives)))
            .andExpect(status().isBadRequest());

        List<Relatives> relativesList = relativesRepository.findAll();
        assertThat(relativesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPatronymicIsRequired() throws Exception {
        int databaseSizeBeforeTest = relativesRepository.findAll().size();
        // set the field null
        relatives.setPatronymic(null);

        // Create the Relatives, which fails.

        restRelativesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(relatives)))
            .andExpect(status().isBadRequest());

        List<Relatives> relativesList = relativesRepository.findAll();
        assertThat(relativesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeOfRelationshipIsRequired() throws Exception {
        int databaseSizeBeforeTest = relativesRepository.findAll().size();
        // set the field null
        relatives.setTypeOfRelationship(null);

        // Create the Relatives, which fails.

        restRelativesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(relatives)))
            .andExpect(status().isBadRequest());

        List<Relatives> relativesList = relativesRepository.findAll();
        assertThat(relativesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRelatives() throws Exception {
        // Initialize the database
        relativesRepository.saveAndFlush(relatives);

        // Get all the relativesList
        restRelativesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(relatives.getId().intValue())))
            .andExpect(jsonPath("$.[*].surname").value(hasItem(DEFAULT_SURNAME)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].patronymic").value(hasItem(DEFAULT_PATRONYMIC)))
            .andExpect(jsonPath("$.[*].typeOfRelationship").value(hasItem(DEFAULT_TYPE_OF_RELATIONSHIP)));
    }

    @Test
    @Transactional
    void getRelatives() throws Exception {
        // Initialize the database
        relativesRepository.saveAndFlush(relatives);

        // Get the relatives
        restRelativesMockMvc
            .perform(get(ENTITY_API_URL_ID, relatives.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(relatives.getId().intValue()))
            .andExpect(jsonPath("$.surname").value(DEFAULT_SURNAME))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.patronymic").value(DEFAULT_PATRONYMIC))
            .andExpect(jsonPath("$.typeOfRelationship").value(DEFAULT_TYPE_OF_RELATIONSHIP));
    }

    @Test
    @Transactional
    void getNonExistingRelatives() throws Exception {
        // Get the relatives
        restRelativesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRelatives() throws Exception {
        // Initialize the database
        relativesRepository.saveAndFlush(relatives);

        int databaseSizeBeforeUpdate = relativesRepository.findAll().size();

        // Update the relatives
        Relatives updatedRelatives = relativesRepository.findById(relatives.getId()).get();
        // Disconnect from session so that the updates on updatedRelatives are not directly saved in db
        em.detach(updatedRelatives);
        updatedRelatives
            .surname(UPDATED_SURNAME)
            .name(UPDATED_NAME)
            .patronymic(UPDATED_PATRONYMIC)
            .typeOfRelationship(UPDATED_TYPE_OF_RELATIONSHIP);

        restRelativesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRelatives.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRelatives))
            )
            .andExpect(status().isOk());

        // Validate the Relatives in the database
        List<Relatives> relativesList = relativesRepository.findAll();
        assertThat(relativesList).hasSize(databaseSizeBeforeUpdate);
        Relatives testRelatives = relativesList.get(relativesList.size() - 1);
        assertThat(testRelatives.getSurname()).isEqualTo(UPDATED_SURNAME);
        assertThat(testRelatives.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRelatives.getPatronymic()).isEqualTo(UPDATED_PATRONYMIC);
        assertThat(testRelatives.getTypeOfRelationship()).isEqualTo(UPDATED_TYPE_OF_RELATIONSHIP);
    }

    @Test
    @Transactional
    void putNonExistingRelatives() throws Exception {
        int databaseSizeBeforeUpdate = relativesRepository.findAll().size();
        relatives.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRelativesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, relatives.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(relatives))
            )
            .andExpect(status().isBadRequest());

        // Validate the Relatives in the database
        List<Relatives> relativesList = relativesRepository.findAll();
        assertThat(relativesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRelatives() throws Exception {
        int databaseSizeBeforeUpdate = relativesRepository.findAll().size();
        relatives.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRelativesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(relatives))
            )
            .andExpect(status().isBadRequest());

        // Validate the Relatives in the database
        List<Relatives> relativesList = relativesRepository.findAll();
        assertThat(relativesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRelatives() throws Exception {
        int databaseSizeBeforeUpdate = relativesRepository.findAll().size();
        relatives.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRelativesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(relatives)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Relatives in the database
        List<Relatives> relativesList = relativesRepository.findAll();
        assertThat(relativesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRelativesWithPatch() throws Exception {
        // Initialize the database
        relativesRepository.saveAndFlush(relatives);

        int databaseSizeBeforeUpdate = relativesRepository.findAll().size();

        // Update the relatives using partial update
        Relatives partialUpdatedRelatives = new Relatives();
        partialUpdatedRelatives.setId(relatives.getId());

        partialUpdatedRelatives.surname(UPDATED_SURNAME).patronymic(UPDATED_PATRONYMIC);

        restRelativesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRelatives.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRelatives))
            )
            .andExpect(status().isOk());

        // Validate the Relatives in the database
        List<Relatives> relativesList = relativesRepository.findAll();
        assertThat(relativesList).hasSize(databaseSizeBeforeUpdate);
        Relatives testRelatives = relativesList.get(relativesList.size() - 1);
        assertThat(testRelatives.getSurname()).isEqualTo(UPDATED_SURNAME);
        assertThat(testRelatives.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRelatives.getPatronymic()).isEqualTo(UPDATED_PATRONYMIC);
        assertThat(testRelatives.getTypeOfRelationship()).isEqualTo(DEFAULT_TYPE_OF_RELATIONSHIP);
    }

    @Test
    @Transactional
    void fullUpdateRelativesWithPatch() throws Exception {
        // Initialize the database
        relativesRepository.saveAndFlush(relatives);

        int databaseSizeBeforeUpdate = relativesRepository.findAll().size();

        // Update the relatives using partial update
        Relatives partialUpdatedRelatives = new Relatives();
        partialUpdatedRelatives.setId(relatives.getId());

        partialUpdatedRelatives
            .surname(UPDATED_SURNAME)
            .name(UPDATED_NAME)
            .patronymic(UPDATED_PATRONYMIC)
            .typeOfRelationship(UPDATED_TYPE_OF_RELATIONSHIP);

        restRelativesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRelatives.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRelatives))
            )
            .andExpect(status().isOk());

        // Validate the Relatives in the database
        List<Relatives> relativesList = relativesRepository.findAll();
        assertThat(relativesList).hasSize(databaseSizeBeforeUpdate);
        Relatives testRelatives = relativesList.get(relativesList.size() - 1);
        assertThat(testRelatives.getSurname()).isEqualTo(UPDATED_SURNAME);
        assertThat(testRelatives.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRelatives.getPatronymic()).isEqualTo(UPDATED_PATRONYMIC);
        assertThat(testRelatives.getTypeOfRelationship()).isEqualTo(UPDATED_TYPE_OF_RELATIONSHIP);
    }

    @Test
    @Transactional
    void patchNonExistingRelatives() throws Exception {
        int databaseSizeBeforeUpdate = relativesRepository.findAll().size();
        relatives.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRelativesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, relatives.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(relatives))
            )
            .andExpect(status().isBadRequest());

        // Validate the Relatives in the database
        List<Relatives> relativesList = relativesRepository.findAll();
        assertThat(relativesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRelatives() throws Exception {
        int databaseSizeBeforeUpdate = relativesRepository.findAll().size();
        relatives.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRelativesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(relatives))
            )
            .andExpect(status().isBadRequest());

        // Validate the Relatives in the database
        List<Relatives> relativesList = relativesRepository.findAll();
        assertThat(relativesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRelatives() throws Exception {
        int databaseSizeBeforeUpdate = relativesRepository.findAll().size();
        relatives.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRelativesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(relatives))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Relatives in the database
        List<Relatives> relativesList = relativesRepository.findAll();
        assertThat(relativesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRelatives() throws Exception {
        // Initialize the database
        relativesRepository.saveAndFlush(relatives);

        int databaseSizeBeforeDelete = relativesRepository.findAll().size();

        // Delete the relatives
        restRelativesMockMvc
            .perform(delete(ENTITY_API_URL_ID, relatives.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Relatives> relativesList = relativesRepository.findAll();
        assertThat(relativesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
