package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Human;
import com.mycompany.myapp.repository.HumanRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Human}.
 */
@Service
@Transactional
public class HumanService {

    private final Logger log = LoggerFactory.getLogger(HumanService.class);

    private final HumanRepository humanRepository;

    public HumanService(HumanRepository humanRepository) {
        this.humanRepository = humanRepository;
    }

    /**
     * Save a human.
     *
     * @param human the entity to save.
     * @return the persisted entity.
     */
    public Human save(Human human) {
        log.debug("Request to save Human : {}", human);
        return humanRepository.save(human);
    }

    /**
     * Partially update a human.
     *
     * @param human the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Human> partialUpdate(Human human) {
        log.debug("Request to partially update Human : {}", human);

        return humanRepository
            .findById(human.getId())
            .map(
                existingHuman -> {
                    if (human.getSurname() != null) {
                        existingHuman.setSurname(human.getSurname());
                    }
                    if (human.getName() != null) {
                        existingHuman.setName(human.getName());
                    }
                    if (human.getPatronymic() != null) {
                        existingHuman.setPatronymic(human.getPatronymic());
                    }
                    if (human.getHumanInfo() != null) {
                        existingHuman.setHumanInfo(human.getHumanInfo());
                    }

                    return existingHuman;
                }
            )
            .map(humanRepository::save);
    }

    /**
     * Get all the humans.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Human> findAll(Pageable pageable) {
        log.debug("Request to get all Humans");
        return humanRepository.findAll(pageable);
    }

    /**
     * Get one human by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Human> findOne(Long id) {
        log.debug("Request to get Human : {}", id);
        return humanRepository.findById(id);
    }

    /**
     * Delete the human by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Human : {}", id);
        humanRepository.deleteById(id);
    }
}
