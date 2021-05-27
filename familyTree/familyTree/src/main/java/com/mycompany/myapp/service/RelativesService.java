package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Relatives;
import com.mycompany.myapp.repository.RelativesRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Relatives}.
 */
@Service
@Transactional
public class RelativesService {

    private final Logger log = LoggerFactory.getLogger(RelativesService.class);

    private final RelativesRepository relativesRepository;

    public RelativesService(RelativesRepository relativesRepository) {
        this.relativesRepository = relativesRepository;
    }

    /**
     * Save a relatives.
     *
     * @param relatives the entity to save.
     * @return the persisted entity.
     */
    public Relatives save(Relatives relatives) {
        log.debug("Request to save Relatives : {}", relatives);
        return relativesRepository.save(relatives);
    }

    /**
     * Partially update a relatives.
     *
     * @param relatives the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Relatives> partialUpdate(Relatives relatives) {
        log.debug("Request to partially update Relatives : {}", relatives);

        return relativesRepository
            .findById(relatives.getId())
            .map(
                existingRelatives -> {
                    if (relatives.getSurname() != null) {
                        existingRelatives.setSurname(relatives.getSurname());
                    }
                    if (relatives.getName() != null) {
                        existingRelatives.setName(relatives.getName());
                    }
                    if (relatives.getPatronymic() != null) {
                        existingRelatives.setPatronymic(relatives.getPatronymic());
                    }
                    if (relatives.getTypeOfRelationship() != null) {
                        existingRelatives.setTypeOfRelationship(relatives.getTypeOfRelationship());
                    }

                    return existingRelatives;
                }
            )
            .map(relativesRepository::save);
    }

    /**
     * Get all the relatives.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Relatives> findAll(Pageable pageable) {
        log.debug("Request to get all Relatives");
        return relativesRepository.findAll(pageable);
    }

    /**
     * Get one relatives by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Relatives> findOne(Long id) {
        log.debug("Request to get Relatives : {}", id);
        return relativesRepository.findById(id);
    }

    /**
     * Delete the relatives by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Relatives : {}", id);
        relativesRepository.deleteById(id);
    }
}
