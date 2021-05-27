package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.TypeOfRelationship;
import com.mycompany.myapp.repository.TypeOfRelationshipRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TypeOfRelationship}.
 */
@Service
@Transactional
public class TypeOfRelationshipService {

    private final Logger log = LoggerFactory.getLogger(TypeOfRelationshipService.class);

    private final TypeOfRelationshipRepository typeOfRelationshipRepository;

    public TypeOfRelationshipService(TypeOfRelationshipRepository typeOfRelationshipRepository) {
        this.typeOfRelationshipRepository = typeOfRelationshipRepository;
    }

    /**
     * Save a typeOfRelationship.
     *
     * @param typeOfRelationship the entity to save.
     * @return the persisted entity.
     */
    public TypeOfRelationship save(TypeOfRelationship typeOfRelationship) {
        log.debug("Request to save TypeOfRelationship : {}", typeOfRelationship);
        return typeOfRelationshipRepository.save(typeOfRelationship);
    }

    /**
     * Partially update a typeOfRelationship.
     *
     * @param typeOfRelationship the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TypeOfRelationship> partialUpdate(TypeOfRelationship typeOfRelationship) {
        log.debug("Request to partially update TypeOfRelationship : {}", typeOfRelationship);

        return typeOfRelationshipRepository
            .findById(typeOfRelationship.getId())
            .map(
                existingTypeOfRelationship -> {
                    if (typeOfRelationship.getDegreeOfKinship() != null) {
                        existingTypeOfRelationship.setDegreeOfKinship(typeOfRelationship.getDegreeOfKinship());
                    }

                    return existingTypeOfRelationship;
                }
            )
            .map(typeOfRelationshipRepository::save);
    }

    /**
     * Get all the typeOfRelationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TypeOfRelationship> findAll(Pageable pageable) {
        log.debug("Request to get all TypeOfRelationships");
        return typeOfRelationshipRepository.findAll(pageable);
    }

    /**
     * Get one typeOfRelationship by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TypeOfRelationship> findOne(Long id) {
        log.debug("Request to get TypeOfRelationship : {}", id);
        return typeOfRelationshipRepository.findById(id);
    }

    /**
     * Delete the typeOfRelationship by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TypeOfRelationship : {}", id);
        typeOfRelationshipRepository.deleteById(id);
    }
}
