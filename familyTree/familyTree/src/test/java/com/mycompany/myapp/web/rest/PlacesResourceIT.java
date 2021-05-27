package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Places;
import com.mycompany.myapp.repository.PlacesRepository;
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
 * Integration tests for the {@link PlacesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PlacesResourceIT {

    private static final String DEFAULT_PLACE_OF_BIRTH = "AAAAAAAAAA";
    private static final String UPDATED_PLACE_OF_BIRTH = "BBBBBBBBBB";

    private static final String DEFAULT_PLACE_OF_DEATH = "AAAAAAAAAA";
    private static final String UPDATED_PLACE_OF_DEATH = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/places";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PlacesRepository placesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPlacesMockMvc;

    private Places places;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Places createEntity(EntityManager em) {
        Places places = new Places().placeOfBirth(DEFAULT_PLACE_OF_BIRTH).placeOfDeath(DEFAULT_PLACE_OF_DEATH);
        return places;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Places createUpdatedEntity(EntityManager em) {
        Places places = new Places().placeOfBirth(UPDATED_PLACE_OF_BIRTH).placeOfDeath(UPDATED_PLACE_OF_DEATH);
        return places;
    }

    @BeforeEach
    public void initTest() {
        places = createEntity(em);
    }

    @Test
    @Transactional
    void createPlaces() throws Exception {
        int databaseSizeBeforeCreate = placesRepository.findAll().size();
        // Create the Places
        restPlacesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(places)))
            .andExpect(status().isCreated());

        // Validate the Places in the database
        List<Places> placesList = placesRepository.findAll();
        assertThat(placesList).hasSize(databaseSizeBeforeCreate + 1);
        Places testPlaces = placesList.get(placesList.size() - 1);
        assertThat(testPlaces.getPlaceOfBirth()).isEqualTo(DEFAULT_PLACE_OF_BIRTH);
        assertThat(testPlaces.getPlaceOfDeath()).isEqualTo(DEFAULT_PLACE_OF_DEATH);
    }

    @Test
    @Transactional
    void createPlacesWithExistingId() throws Exception {
        // Create the Places with an existing ID
        places.setId(1L);

        int databaseSizeBeforeCreate = placesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlacesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(places)))
            .andExpect(status().isBadRequest());

        // Validate the Places in the database
        List<Places> placesList = placesRepository.findAll();
        assertThat(placesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPlaceOfBirthIsRequired() throws Exception {
        int databaseSizeBeforeTest = placesRepository.findAll().size();
        // set the field null
        places.setPlaceOfBirth(null);

        // Create the Places, which fails.

        restPlacesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(places)))
            .andExpect(status().isBadRequest());

        List<Places> placesList = placesRepository.findAll();
        assertThat(placesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPlaceOfDeathIsRequired() throws Exception {
        int databaseSizeBeforeTest = placesRepository.findAll().size();
        // set the field null
        places.setPlaceOfDeath(null);

        // Create the Places, which fails.

        restPlacesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(places)))
            .andExpect(status().isBadRequest());

        List<Places> placesList = placesRepository.findAll();
        assertThat(placesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPlaces() throws Exception {
        // Initialize the database
        placesRepository.saveAndFlush(places);

        // Get all the placesList
        restPlacesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(places.getId().intValue())))
            .andExpect(jsonPath("$.[*].placeOfBirth").value(hasItem(DEFAULT_PLACE_OF_BIRTH)))
            .andExpect(jsonPath("$.[*].placeOfDeath").value(hasItem(DEFAULT_PLACE_OF_DEATH)));
    }

    @Test
    @Transactional
    void getPlaces() throws Exception {
        // Initialize the database
        placesRepository.saveAndFlush(places);

        // Get the places
        restPlacesMockMvc
            .perform(get(ENTITY_API_URL_ID, places.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(places.getId().intValue()))
            .andExpect(jsonPath("$.placeOfBirth").value(DEFAULT_PLACE_OF_BIRTH))
            .andExpect(jsonPath("$.placeOfDeath").value(DEFAULT_PLACE_OF_DEATH));
    }

    @Test
    @Transactional
    void getNonExistingPlaces() throws Exception {
        // Get the places
        restPlacesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPlaces() throws Exception {
        // Initialize the database
        placesRepository.saveAndFlush(places);

        int databaseSizeBeforeUpdate = placesRepository.findAll().size();

        // Update the places
        Places updatedPlaces = placesRepository.findById(places.getId()).get();
        // Disconnect from session so that the updates on updatedPlaces are not directly saved in db
        em.detach(updatedPlaces);
        updatedPlaces.placeOfBirth(UPDATED_PLACE_OF_BIRTH).placeOfDeath(UPDATED_PLACE_OF_DEATH);

        restPlacesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPlaces.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPlaces))
            )
            .andExpect(status().isOk());

        // Validate the Places in the database
        List<Places> placesList = placesRepository.findAll();
        assertThat(placesList).hasSize(databaseSizeBeforeUpdate);
        Places testPlaces = placesList.get(placesList.size() - 1);
        assertThat(testPlaces.getPlaceOfBirth()).isEqualTo(UPDATED_PLACE_OF_BIRTH);
        assertThat(testPlaces.getPlaceOfDeath()).isEqualTo(UPDATED_PLACE_OF_DEATH);
    }

    @Test
    @Transactional
    void putNonExistingPlaces() throws Exception {
        int databaseSizeBeforeUpdate = placesRepository.findAll().size();
        places.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlacesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, places.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(places))
            )
            .andExpect(status().isBadRequest());

        // Validate the Places in the database
        List<Places> placesList = placesRepository.findAll();
        assertThat(placesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPlaces() throws Exception {
        int databaseSizeBeforeUpdate = placesRepository.findAll().size();
        places.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlacesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(places))
            )
            .andExpect(status().isBadRequest());

        // Validate the Places in the database
        List<Places> placesList = placesRepository.findAll();
        assertThat(placesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPlaces() throws Exception {
        int databaseSizeBeforeUpdate = placesRepository.findAll().size();
        places.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlacesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(places)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Places in the database
        List<Places> placesList = placesRepository.findAll();
        assertThat(placesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePlacesWithPatch() throws Exception {
        // Initialize the database
        placesRepository.saveAndFlush(places);

        int databaseSizeBeforeUpdate = placesRepository.findAll().size();

        // Update the places using partial update
        Places partialUpdatedPlaces = new Places();
        partialUpdatedPlaces.setId(places.getId());

        partialUpdatedPlaces.placeOfDeath(UPDATED_PLACE_OF_DEATH);

        restPlacesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlaces.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPlaces))
            )
            .andExpect(status().isOk());

        // Validate the Places in the database
        List<Places> placesList = placesRepository.findAll();
        assertThat(placesList).hasSize(databaseSizeBeforeUpdate);
        Places testPlaces = placesList.get(placesList.size() - 1);
        assertThat(testPlaces.getPlaceOfBirth()).isEqualTo(DEFAULT_PLACE_OF_BIRTH);
        assertThat(testPlaces.getPlaceOfDeath()).isEqualTo(UPDATED_PLACE_OF_DEATH);
    }

    @Test
    @Transactional
    void fullUpdatePlacesWithPatch() throws Exception {
        // Initialize the database
        placesRepository.saveAndFlush(places);

        int databaseSizeBeforeUpdate = placesRepository.findAll().size();

        // Update the places using partial update
        Places partialUpdatedPlaces = new Places();
        partialUpdatedPlaces.setId(places.getId());

        partialUpdatedPlaces.placeOfBirth(UPDATED_PLACE_OF_BIRTH).placeOfDeath(UPDATED_PLACE_OF_DEATH);

        restPlacesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlaces.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPlaces))
            )
            .andExpect(status().isOk());

        // Validate the Places in the database
        List<Places> placesList = placesRepository.findAll();
        assertThat(placesList).hasSize(databaseSizeBeforeUpdate);
        Places testPlaces = placesList.get(placesList.size() - 1);
        assertThat(testPlaces.getPlaceOfBirth()).isEqualTo(UPDATED_PLACE_OF_BIRTH);
        assertThat(testPlaces.getPlaceOfDeath()).isEqualTo(UPDATED_PLACE_OF_DEATH);
    }

    @Test
    @Transactional
    void patchNonExistingPlaces() throws Exception {
        int databaseSizeBeforeUpdate = placesRepository.findAll().size();
        places.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlacesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, places.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(places))
            )
            .andExpect(status().isBadRequest());

        // Validate the Places in the database
        List<Places> placesList = placesRepository.findAll();
        assertThat(placesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPlaces() throws Exception {
        int databaseSizeBeforeUpdate = placesRepository.findAll().size();
        places.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlacesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(places))
            )
            .andExpect(status().isBadRequest());

        // Validate the Places in the database
        List<Places> placesList = placesRepository.findAll();
        assertThat(placesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPlaces() throws Exception {
        int databaseSizeBeforeUpdate = placesRepository.findAll().size();
        places.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlacesMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(places)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Places in the database
        List<Places> placesList = placesRepository.findAll();
        assertThat(placesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePlaces() throws Exception {
        // Initialize the database
        placesRepository.saveAndFlush(places);

        int databaseSizeBeforeDelete = placesRepository.findAll().size();

        // Delete the places
        restPlacesMockMvc
            .perform(delete(ENTITY_API_URL_ID, places.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Places> placesList = placesRepository.findAll();
        assertThat(placesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
