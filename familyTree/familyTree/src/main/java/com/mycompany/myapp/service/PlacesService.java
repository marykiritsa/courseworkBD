package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Places;
import com.mycompany.myapp.repository.PlacesRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Places}.
 */
@Service
@Transactional
public class PlacesService {

    private final Logger log = LoggerFactory.getLogger(PlacesService.class);

    private final PlacesRepository placesRepository;

    public PlacesService(PlacesRepository placesRepository) {
        this.placesRepository = placesRepository;
    }

    /**
     * Save a places.
     *
     * @param places the entity to save.
     * @return the persisted entity.
     */
    public Places save(Places places) {
        log.debug("Request to save Places : {}", places);
        return placesRepository.save(places);
    }

    /**
     * Partially update a places.
     *
     * @param places the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Places> partialUpdate(Places places) {
        log.debug("Request to partially update Places : {}", places);

        return placesRepository
            .findById(places.getId())
            .map(
                existingPlaces -> {
                    if (places.getPlaceOfBirth() != null) {
                        existingPlaces.setPlaceOfBirth(places.getPlaceOfBirth());
                    }
                    if (places.getPlaceOfDeath() != null) {
                        existingPlaces.setPlaceOfDeath(places.getPlaceOfDeath());
                    }

                    return existingPlaces;
                }
            )
            .map(placesRepository::save);
    }

    /**
     * Get all the places.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Places> findAll(Pageable pageable) {
        log.debug("Request to get all Places");
        return placesRepository.findAll(pageable);
    }

    /**
     * Get one places by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Places> findOne(Long id) {
        log.debug("Request to get Places : {}", id);
        return placesRepository.findById(id);
    }

    /**
     * Delete the places by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Places : {}", id);
        placesRepository.deleteById(id);
    }
}
