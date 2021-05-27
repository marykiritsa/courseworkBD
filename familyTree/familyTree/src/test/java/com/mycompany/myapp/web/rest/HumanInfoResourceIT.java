package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.HumanInfo;
import com.mycompany.myapp.repository.HumanInfoRepository;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link HumanInfoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HumanInfoResourceIT {

    private static final LocalDate DEFAULT_DATE_OF_BIRTH = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_BIRTH = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_OF_DEATH = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_DEATH = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_PLACES = 0;
    private static final Integer UPDATED_PLACES = 1;

    private static final Integer DEFAULT_RELATIVES = 0;
    private static final Integer UPDATED_RELATIVES = 1;

    private static final String ENTITY_API_URL = "/api/human-infos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HumanInfoRepository humanInfoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHumanInfoMockMvc;

    private HumanInfo humanInfo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HumanInfo createEntity(EntityManager em) {
        HumanInfo humanInfo = new HumanInfo()
            .dateOfBirth(DEFAULT_DATE_OF_BIRTH)
            .dateOfDeath(DEFAULT_DATE_OF_DEATH)
            .places(DEFAULT_PLACES)
            .relatives(DEFAULT_RELATIVES);
        return humanInfo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HumanInfo createUpdatedEntity(EntityManager em) {
        HumanInfo humanInfo = new HumanInfo()
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .dateOfDeath(UPDATED_DATE_OF_DEATH)
            .places(UPDATED_PLACES)
            .relatives(UPDATED_RELATIVES);
        return humanInfo;
    }

    @BeforeEach
    public void initTest() {
        humanInfo = createEntity(em);
    }

    @Test
    @Transactional
    void createHumanInfo() throws Exception {
        int databaseSizeBeforeCreate = humanInfoRepository.findAll().size();
        // Create the HumanInfo
        restHumanInfoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(humanInfo)))
            .andExpect(status().isCreated());

        // Validate the HumanInfo in the database
        List<HumanInfo> humanInfoList = humanInfoRepository.findAll();
        assertThat(humanInfoList).hasSize(databaseSizeBeforeCreate + 1);
        HumanInfo testHumanInfo = humanInfoList.get(humanInfoList.size() - 1);
        assertThat(testHumanInfo.getDateOfBirth()).isEqualTo(DEFAULT_DATE_OF_BIRTH);
        assertThat(testHumanInfo.getDateOfDeath()).isEqualTo(DEFAULT_DATE_OF_DEATH);
        assertThat(testHumanInfo.getPlaces()).isEqualTo(DEFAULT_PLACES);
        assertThat(testHumanInfo.getRelatives()).isEqualTo(DEFAULT_RELATIVES);
    }

    @Test
    @Transactional
    void createHumanInfoWithExistingId() throws Exception {
        // Create the HumanInfo with an existing ID
        humanInfo.setId(1L);

        int databaseSizeBeforeCreate = humanInfoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHumanInfoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(humanInfo)))
            .andExpect(status().isBadRequest());

        // Validate the HumanInfo in the database
        List<HumanInfo> humanInfoList = humanInfoRepository.findAll();
        assertThat(humanInfoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPlacesIsRequired() throws Exception {
        int databaseSizeBeforeTest = humanInfoRepository.findAll().size();
        // set the field null
        humanInfo.setPlaces(null);

        // Create the HumanInfo, which fails.

        restHumanInfoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(humanInfo)))
            .andExpect(status().isBadRequest());

        List<HumanInfo> humanInfoList = humanInfoRepository.findAll();
        assertThat(humanInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRelativesIsRequired() throws Exception {
        int databaseSizeBeforeTest = humanInfoRepository.findAll().size();
        // set the field null
        humanInfo.setRelatives(null);

        // Create the HumanInfo, which fails.

        restHumanInfoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(humanInfo)))
            .andExpect(status().isBadRequest());

        List<HumanInfo> humanInfoList = humanInfoRepository.findAll();
        assertThat(humanInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHumanInfos() throws Exception {
        // Initialize the database
        humanInfoRepository.saveAndFlush(humanInfo);

        // Get all the humanInfoList
        restHumanInfoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(humanInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
            .andExpect(jsonPath("$.[*].dateOfDeath").value(hasItem(DEFAULT_DATE_OF_DEATH.toString())))
            .andExpect(jsonPath("$.[*].places").value(hasItem(DEFAULT_PLACES)))
            .andExpect(jsonPath("$.[*].relatives").value(hasItem(DEFAULT_RELATIVES)));
    }

    @Test
    @Transactional
    void getHumanInfo() throws Exception {
        // Initialize the database
        humanInfoRepository.saveAndFlush(humanInfo);

        // Get the humanInfo
        restHumanInfoMockMvc
            .perform(get(ENTITY_API_URL_ID, humanInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(humanInfo.getId().intValue()))
            .andExpect(jsonPath("$.dateOfBirth").value(DEFAULT_DATE_OF_BIRTH.toString()))
            .andExpect(jsonPath("$.dateOfDeath").value(DEFAULT_DATE_OF_DEATH.toString()))
            .andExpect(jsonPath("$.places").value(DEFAULT_PLACES))
            .andExpect(jsonPath("$.relatives").value(DEFAULT_RELATIVES));
    }

    @Test
    @Transactional
    void getNonExistingHumanInfo() throws Exception {
        // Get the humanInfo
        restHumanInfoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewHumanInfo() throws Exception {
        // Initialize the database
        humanInfoRepository.saveAndFlush(humanInfo);

        int databaseSizeBeforeUpdate = humanInfoRepository.findAll().size();

        // Update the humanInfo
        HumanInfo updatedHumanInfo = humanInfoRepository.findById(humanInfo.getId()).get();
        // Disconnect from session so that the updates on updatedHumanInfo are not directly saved in db
        em.detach(updatedHumanInfo);
        updatedHumanInfo
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .dateOfDeath(UPDATED_DATE_OF_DEATH)
            .places(UPDATED_PLACES)
            .relatives(UPDATED_RELATIVES);

        restHumanInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedHumanInfo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedHumanInfo))
            )
            .andExpect(status().isOk());

        // Validate the HumanInfo in the database
        List<HumanInfo> humanInfoList = humanInfoRepository.findAll();
        assertThat(humanInfoList).hasSize(databaseSizeBeforeUpdate);
        HumanInfo testHumanInfo = humanInfoList.get(humanInfoList.size() - 1);
        assertThat(testHumanInfo.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testHumanInfo.getDateOfDeath()).isEqualTo(UPDATED_DATE_OF_DEATH);
        assertThat(testHumanInfo.getPlaces()).isEqualTo(UPDATED_PLACES);
        assertThat(testHumanInfo.getRelatives()).isEqualTo(UPDATED_RELATIVES);
    }

    @Test
    @Transactional
    void putNonExistingHumanInfo() throws Exception {
        int databaseSizeBeforeUpdate = humanInfoRepository.findAll().size();
        humanInfo.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHumanInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, humanInfo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(humanInfo))
            )
            .andExpect(status().isBadRequest());

        // Validate the HumanInfo in the database
        List<HumanInfo> humanInfoList = humanInfoRepository.findAll();
        assertThat(humanInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHumanInfo() throws Exception {
        int databaseSizeBeforeUpdate = humanInfoRepository.findAll().size();
        humanInfo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHumanInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(humanInfo))
            )
            .andExpect(status().isBadRequest());

        // Validate the HumanInfo in the database
        List<HumanInfo> humanInfoList = humanInfoRepository.findAll();
        assertThat(humanInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHumanInfo() throws Exception {
        int databaseSizeBeforeUpdate = humanInfoRepository.findAll().size();
        humanInfo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHumanInfoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(humanInfo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HumanInfo in the database
        List<HumanInfo> humanInfoList = humanInfoRepository.findAll();
        assertThat(humanInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHumanInfoWithPatch() throws Exception {
        // Initialize the database
        humanInfoRepository.saveAndFlush(humanInfo);

        int databaseSizeBeforeUpdate = humanInfoRepository.findAll().size();

        // Update the humanInfo using partial update
        HumanInfo partialUpdatedHumanInfo = new HumanInfo();
        partialUpdatedHumanInfo.setId(humanInfo.getId());

        partialUpdatedHumanInfo.dateOfBirth(UPDATED_DATE_OF_BIRTH);

        restHumanInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHumanInfo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHumanInfo))
            )
            .andExpect(status().isOk());

        // Validate the HumanInfo in the database
        List<HumanInfo> humanInfoList = humanInfoRepository.findAll();
        assertThat(humanInfoList).hasSize(databaseSizeBeforeUpdate);
        HumanInfo testHumanInfo = humanInfoList.get(humanInfoList.size() - 1);
        assertThat(testHumanInfo.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testHumanInfo.getDateOfDeath()).isEqualTo(DEFAULT_DATE_OF_DEATH);
        assertThat(testHumanInfo.getPlaces()).isEqualTo(DEFAULT_PLACES);
        assertThat(testHumanInfo.getRelatives()).isEqualTo(DEFAULT_RELATIVES);
    }

    @Test
    @Transactional
    void fullUpdateHumanInfoWithPatch() throws Exception {
        // Initialize the database
        humanInfoRepository.saveAndFlush(humanInfo);

        int databaseSizeBeforeUpdate = humanInfoRepository.findAll().size();

        // Update the humanInfo using partial update
        HumanInfo partialUpdatedHumanInfo = new HumanInfo();
        partialUpdatedHumanInfo.setId(humanInfo.getId());

        partialUpdatedHumanInfo
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .dateOfDeath(UPDATED_DATE_OF_DEATH)
            .places(UPDATED_PLACES)
            .relatives(UPDATED_RELATIVES);

        restHumanInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHumanInfo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHumanInfo))
            )
            .andExpect(status().isOk());

        // Validate the HumanInfo in the database
        List<HumanInfo> humanInfoList = humanInfoRepository.findAll();
        assertThat(humanInfoList).hasSize(databaseSizeBeforeUpdate);
        HumanInfo testHumanInfo = humanInfoList.get(humanInfoList.size() - 1);
        assertThat(testHumanInfo.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testHumanInfo.getDateOfDeath()).isEqualTo(UPDATED_DATE_OF_DEATH);
        assertThat(testHumanInfo.getPlaces()).isEqualTo(UPDATED_PLACES);
        assertThat(testHumanInfo.getRelatives()).isEqualTo(UPDATED_RELATIVES);
    }

    @Test
    @Transactional
    void patchNonExistingHumanInfo() throws Exception {
        int databaseSizeBeforeUpdate = humanInfoRepository.findAll().size();
        humanInfo.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHumanInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, humanInfo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(humanInfo))
            )
            .andExpect(status().isBadRequest());

        // Validate the HumanInfo in the database
        List<HumanInfo> humanInfoList = humanInfoRepository.findAll();
        assertThat(humanInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHumanInfo() throws Exception {
        int databaseSizeBeforeUpdate = humanInfoRepository.findAll().size();
        humanInfo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHumanInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(humanInfo))
            )
            .andExpect(status().isBadRequest());

        // Validate the HumanInfo in the database
        List<HumanInfo> humanInfoList = humanInfoRepository.findAll();
        assertThat(humanInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHumanInfo() throws Exception {
        int databaseSizeBeforeUpdate = humanInfoRepository.findAll().size();
        humanInfo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHumanInfoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(humanInfo))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HumanInfo in the database
        List<HumanInfo> humanInfoList = humanInfoRepository.findAll();
        assertThat(humanInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHumanInfo() throws Exception {
        // Initialize the database
        humanInfoRepository.saveAndFlush(humanInfo);

        int databaseSizeBeforeDelete = humanInfoRepository.findAll().size();

        // Delete the humanInfo
        restHumanInfoMockMvc
            .perform(delete(ENTITY_API_URL_ID, humanInfo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<HumanInfo> humanInfoList = humanInfoRepository.findAll();
        assertThat(humanInfoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
