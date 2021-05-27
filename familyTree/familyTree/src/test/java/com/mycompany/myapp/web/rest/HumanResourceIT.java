package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Human;
import com.mycompany.myapp.repository.HumanRepository;
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
 * Integration tests for the {@link HumanResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HumanResourceIT {

    private static final String DEFAULT_SURNAME = "AAAAAAAAAA";
    private static final String UPDATED_SURNAME = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PATRONYMIC = "AAAAAAAAAA";
    private static final String UPDATED_PATRONYMIC = "BBBBBBBBBB";

    private static final Integer DEFAULT_HUMAN_INFO = 0;
    private static final Integer UPDATED_HUMAN_INFO = 1;

    private static final String ENTITY_API_URL = "/api/humans";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HumanRepository humanRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHumanMockMvc;

    private Human human;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Human createEntity(EntityManager em) {
        Human human = new Human().surname(DEFAULT_SURNAME).name(DEFAULT_NAME).patronymic(DEFAULT_PATRONYMIC).humanInfo(DEFAULT_HUMAN_INFO);
        return human;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Human createUpdatedEntity(EntityManager em) {
        Human human = new Human().surname(UPDATED_SURNAME).name(UPDATED_NAME).patronymic(UPDATED_PATRONYMIC).humanInfo(UPDATED_HUMAN_INFO);
        return human;
    }

    @BeforeEach
    public void initTest() {
        human = createEntity(em);
    }

    @Test
    @Transactional
    void createHuman() throws Exception {
        int databaseSizeBeforeCreate = humanRepository.findAll().size();
        // Create the Human
        restHumanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(human)))
            .andExpect(status().isCreated());

        // Validate the Human in the database
        List<Human> humanList = humanRepository.findAll();
        assertThat(humanList).hasSize(databaseSizeBeforeCreate + 1);
        Human testHuman = humanList.get(humanList.size() - 1);
        assertThat(testHuman.getSurname()).isEqualTo(DEFAULT_SURNAME);
        assertThat(testHuman.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testHuman.getPatronymic()).isEqualTo(DEFAULT_PATRONYMIC);
        assertThat(testHuman.getHumanInfo()).isEqualTo(DEFAULT_HUMAN_INFO);
    }

    @Test
    @Transactional
    void createHumanWithExistingId() throws Exception {
        // Create the Human with an existing ID
        human.setId(1L);

        int databaseSizeBeforeCreate = humanRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHumanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(human)))
            .andExpect(status().isBadRequest());

        // Validate the Human in the database
        List<Human> humanList = humanRepository.findAll();
        assertThat(humanList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSurnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = humanRepository.findAll().size();
        // set the field null
        human.setSurname(null);

        // Create the Human, which fails.

        restHumanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(human)))
            .andExpect(status().isBadRequest());

        List<Human> humanList = humanRepository.findAll();
        assertThat(humanList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = humanRepository.findAll().size();
        // set the field null
        human.setName(null);

        // Create the Human, which fails.

        restHumanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(human)))
            .andExpect(status().isBadRequest());

        List<Human> humanList = humanRepository.findAll();
        assertThat(humanList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPatronymicIsRequired() throws Exception {
        int databaseSizeBeforeTest = humanRepository.findAll().size();
        // set the field null
        human.setPatronymic(null);

        // Create the Human, which fails.

        restHumanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(human)))
            .andExpect(status().isBadRequest());

        List<Human> humanList = humanRepository.findAll();
        assertThat(humanList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkHumanInfoIsRequired() throws Exception {
        int databaseSizeBeforeTest = humanRepository.findAll().size();
        // set the field null
        human.setHumanInfo(null);

        // Create the Human, which fails.

        restHumanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(human)))
            .andExpect(status().isBadRequest());

        List<Human> humanList = humanRepository.findAll();
        assertThat(humanList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHumans() throws Exception {
        // Initialize the database
        humanRepository.saveAndFlush(human);

        // Get all the humanList
        restHumanMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(human.getId().intValue())))
            .andExpect(jsonPath("$.[*].surname").value(hasItem(DEFAULT_SURNAME)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].patronymic").value(hasItem(DEFAULT_PATRONYMIC)))
            .andExpect(jsonPath("$.[*].humanInfo").value(hasItem(DEFAULT_HUMAN_INFO)));
    }

    @Test
    @Transactional
    void getHuman() throws Exception {
        // Initialize the database
        humanRepository.saveAndFlush(human);

        // Get the human
        restHumanMockMvc
            .perform(get(ENTITY_API_URL_ID, human.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(human.getId().intValue()))
            .andExpect(jsonPath("$.surname").value(DEFAULT_SURNAME))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.patronymic").value(DEFAULT_PATRONYMIC))
            .andExpect(jsonPath("$.humanInfo").value(DEFAULT_HUMAN_INFO));
    }

    @Test
    @Transactional
    void getNonExistingHuman() throws Exception {
        // Get the human
        restHumanMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewHuman() throws Exception {
        // Initialize the database
        humanRepository.saveAndFlush(human);

        int databaseSizeBeforeUpdate = humanRepository.findAll().size();

        // Update the human
        Human updatedHuman = humanRepository.findById(human.getId()).get();
        // Disconnect from session so that the updates on updatedHuman are not directly saved in db
        em.detach(updatedHuman);
        updatedHuman.surname(UPDATED_SURNAME).name(UPDATED_NAME).patronymic(UPDATED_PATRONYMIC).humanInfo(UPDATED_HUMAN_INFO);

        restHumanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedHuman.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedHuman))
            )
            .andExpect(status().isOk());

        // Validate the Human in the database
        List<Human> humanList = humanRepository.findAll();
        assertThat(humanList).hasSize(databaseSizeBeforeUpdate);
        Human testHuman = humanList.get(humanList.size() - 1);
        assertThat(testHuman.getSurname()).isEqualTo(UPDATED_SURNAME);
        assertThat(testHuman.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testHuman.getPatronymic()).isEqualTo(UPDATED_PATRONYMIC);
        assertThat(testHuman.getHumanInfo()).isEqualTo(UPDATED_HUMAN_INFO);
    }

    @Test
    @Transactional
    void putNonExistingHuman() throws Exception {
        int databaseSizeBeforeUpdate = humanRepository.findAll().size();
        human.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHumanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, human.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(human))
            )
            .andExpect(status().isBadRequest());

        // Validate the Human in the database
        List<Human> humanList = humanRepository.findAll();
        assertThat(humanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHuman() throws Exception {
        int databaseSizeBeforeUpdate = humanRepository.findAll().size();
        human.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHumanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(human))
            )
            .andExpect(status().isBadRequest());

        // Validate the Human in the database
        List<Human> humanList = humanRepository.findAll();
        assertThat(humanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHuman() throws Exception {
        int databaseSizeBeforeUpdate = humanRepository.findAll().size();
        human.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHumanMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(human)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Human in the database
        List<Human> humanList = humanRepository.findAll();
        assertThat(humanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHumanWithPatch() throws Exception {
        // Initialize the database
        humanRepository.saveAndFlush(human);

        int databaseSizeBeforeUpdate = humanRepository.findAll().size();

        // Update the human using partial update
        Human partialUpdatedHuman = new Human();
        partialUpdatedHuman.setId(human.getId());

        partialUpdatedHuman.patronymic(UPDATED_PATRONYMIC).humanInfo(UPDATED_HUMAN_INFO);

        restHumanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHuman.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHuman))
            )
            .andExpect(status().isOk());

        // Validate the Human in the database
        List<Human> humanList = humanRepository.findAll();
        assertThat(humanList).hasSize(databaseSizeBeforeUpdate);
        Human testHuman = humanList.get(humanList.size() - 1);
        assertThat(testHuman.getSurname()).isEqualTo(DEFAULT_SURNAME);
        assertThat(testHuman.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testHuman.getPatronymic()).isEqualTo(UPDATED_PATRONYMIC);
        assertThat(testHuman.getHumanInfo()).isEqualTo(UPDATED_HUMAN_INFO);
    }

    @Test
    @Transactional
    void fullUpdateHumanWithPatch() throws Exception {
        // Initialize the database
        humanRepository.saveAndFlush(human);

        int databaseSizeBeforeUpdate = humanRepository.findAll().size();

        // Update the human using partial update
        Human partialUpdatedHuman = new Human();
        partialUpdatedHuman.setId(human.getId());

        partialUpdatedHuman.surname(UPDATED_SURNAME).name(UPDATED_NAME).patronymic(UPDATED_PATRONYMIC).humanInfo(UPDATED_HUMAN_INFO);

        restHumanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHuman.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHuman))
            )
            .andExpect(status().isOk());

        // Validate the Human in the database
        List<Human> humanList = humanRepository.findAll();
        assertThat(humanList).hasSize(databaseSizeBeforeUpdate);
        Human testHuman = humanList.get(humanList.size() - 1);
        assertThat(testHuman.getSurname()).isEqualTo(UPDATED_SURNAME);
        assertThat(testHuman.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testHuman.getPatronymic()).isEqualTo(UPDATED_PATRONYMIC);
        assertThat(testHuman.getHumanInfo()).isEqualTo(UPDATED_HUMAN_INFO);
    }

    @Test
    @Transactional
    void patchNonExistingHuman() throws Exception {
        int databaseSizeBeforeUpdate = humanRepository.findAll().size();
        human.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHumanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, human.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(human))
            )
            .andExpect(status().isBadRequest());

        // Validate the Human in the database
        List<Human> humanList = humanRepository.findAll();
        assertThat(humanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHuman() throws Exception {
        int databaseSizeBeforeUpdate = humanRepository.findAll().size();
        human.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHumanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(human))
            )
            .andExpect(status().isBadRequest());

        // Validate the Human in the database
        List<Human> humanList = humanRepository.findAll();
        assertThat(humanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHuman() throws Exception {
        int databaseSizeBeforeUpdate = humanRepository.findAll().size();
        human.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHumanMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(human)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Human in the database
        List<Human> humanList = humanRepository.findAll();
        assertThat(humanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHuman() throws Exception {
        // Initialize the database
        humanRepository.saveAndFlush(human);

        int databaseSizeBeforeDelete = humanRepository.findAll().size();

        // Delete the human
        restHumanMockMvc
            .perform(delete(ENTITY_API_URL_ID, human.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Human> humanList = humanRepository.findAll();
        assertThat(humanList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
