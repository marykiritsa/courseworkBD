package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.HumanInfo;
import com.mycompany.myapp.repository.HumanInfoRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link HumanInfo}.
 */
@Service
@Transactional
public class HumanInfoService {

    private final Logger log = LoggerFactory.getLogger(HumanInfoService.class);

    private final HumanInfoRepository humanInfoRepository;

    public HumanInfoService(HumanInfoRepository humanInfoRepository) {
        this.humanInfoRepository = humanInfoRepository;
    }

    /**
     * Save a humanInfo.
     *
     * @param humanInfo the entity to save.
     * @return the persisted entity.
     */
    public HumanInfo save(HumanInfo humanInfo) {
        log.debug("Request to save HumanInfo : {}", humanInfo);
        return humanInfoRepository.save(humanInfo);
    }

    /**
     * Partially update a humanInfo.
     *
     * @param humanInfo the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<HumanInfo> partialUpdate(HumanInfo humanInfo) {
        log.debug("Request to partially update HumanInfo : {}", humanInfo);

        return humanInfoRepository
            .findById(humanInfo.getId())
            .map(
                existingHumanInfo -> {
                    if (humanInfo.getDateOfBirth() != null) {
                        existingHumanInfo.setDateOfBirth(humanInfo.getDateOfBirth());
                    }
                    if (humanInfo.getDateOfDeath() != null) {
                        existingHumanInfo.setDateOfDeath(humanInfo.getDateOfDeath());
                    }
                    if (humanInfo.getPlaces() != null) {
                        existingHumanInfo.setPlaces(humanInfo.getPlaces());
                    }
                    if (humanInfo.getRelatives() != null) {
                        existingHumanInfo.setRelatives(humanInfo.getRelatives());
                    }

                    return existingHumanInfo;
                }
            )
            .map(humanInfoRepository::save);
    }

    /**
     * Get all the humanInfos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<HumanInfo> findAll(Pageable pageable) {
        log.debug("Request to get all HumanInfos");
        return humanInfoRepository.findAll(pageable);
    }

    /**
     * Get one humanInfo by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HumanInfo> findOne(Long id) {
        log.debug("Request to get HumanInfo : {}", id);
        return humanInfoRepository.findById(id);
    }

    /**
     * Delete the humanInfo by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete HumanInfo : {}", id);
        humanInfoRepository.deleteById(id);
    }
}
