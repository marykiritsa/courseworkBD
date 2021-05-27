package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.HumanInfo;
import com.mycompany.myapp.repository.HumanInfoRepository;
import com.mycompany.myapp.service.HumanInfoService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.HumanInfo}.
 */
@RestController
@RequestMapping("/api")
public class HumanInfoResource {

    private final Logger log = LoggerFactory.getLogger(HumanInfoResource.class);

    private static final String ENTITY_NAME = "humanInfo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HumanInfoService humanInfoService;

    private final HumanInfoRepository humanInfoRepository;

    public HumanInfoResource(HumanInfoService humanInfoService, HumanInfoRepository humanInfoRepository) {
        this.humanInfoService = humanInfoService;
        this.humanInfoRepository = humanInfoRepository;
    }

    /**
     * {@code POST  /human-infos} : Create a new humanInfo.
     *
     * @param humanInfo the humanInfo to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new humanInfo, or with status {@code 400 (Bad Request)} if the humanInfo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/human-infos")
    public ResponseEntity<HumanInfo> createHumanInfo(@Valid @RequestBody HumanInfo humanInfo) throws URISyntaxException {
        log.debug("REST request to save HumanInfo : {}", humanInfo);
        if (humanInfo.getId() != null) {
            throw new BadRequestAlertException("A new humanInfo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HumanInfo result = humanInfoService.save(humanInfo);
        return ResponseEntity
            .created(new URI("/api/human-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /human-infos/:id} : Updates an existing humanInfo.
     *
     * @param id the id of the humanInfo to save.
     * @param humanInfo the humanInfo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated humanInfo,
     * or with status {@code 400 (Bad Request)} if the humanInfo is not valid,
     * or with status {@code 500 (Internal Server Error)} if the humanInfo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/human-infos/{id}")
    public ResponseEntity<HumanInfo> updateHumanInfo(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HumanInfo humanInfo
    ) throws URISyntaxException {
        log.debug("REST request to update HumanInfo : {}, {}", id, humanInfo);
        if (humanInfo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, humanInfo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!humanInfoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        HumanInfo result = humanInfoService.save(humanInfo);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, humanInfo.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /human-infos/:id} : Partial updates given fields of an existing humanInfo, field will ignore if it is null
     *
     * @param id the id of the humanInfo to save.
     * @param humanInfo the humanInfo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated humanInfo,
     * or with status {@code 400 (Bad Request)} if the humanInfo is not valid,
     * or with status {@code 404 (Not Found)} if the humanInfo is not found,
     * or with status {@code 500 (Internal Server Error)} if the humanInfo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/human-infos/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<HumanInfo> partialUpdateHumanInfo(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HumanInfo humanInfo
    ) throws URISyntaxException {
        log.debug("REST request to partial update HumanInfo partially : {}, {}", id, humanInfo);
        if (humanInfo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, humanInfo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!humanInfoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HumanInfo> result = humanInfoService.partialUpdate(humanInfo);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, humanInfo.getId().toString())
        );
    }

    /**
     * {@code GET  /human-infos} : get all the humanInfos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of humanInfos in body.
     */
    @GetMapping("/human-infos")
    public ResponseEntity<List<HumanInfo>> getAllHumanInfos(Pageable pageable) {
        log.debug("REST request to get a page of HumanInfos");
        Page<HumanInfo> page = humanInfoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /human-infos/:id} : get the "id" humanInfo.
     *
     * @param id the id of the humanInfo to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the humanInfo, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/human-infos/{id}")
    public ResponseEntity<HumanInfo> getHumanInfo(@PathVariable Long id) {
        log.debug("REST request to get HumanInfo : {}", id);
        Optional<HumanInfo> humanInfo = humanInfoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(humanInfo);
    }

    /**
     * {@code DELETE  /human-infos/:id} : delete the "id" humanInfo.
     *
     * @param id the id of the humanInfo to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/human-infos/{id}")
    public ResponseEntity<Void> deleteHumanInfo(@PathVariable Long id) {
        log.debug("REST request to delete HumanInfo : {}", id);
        humanInfoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
