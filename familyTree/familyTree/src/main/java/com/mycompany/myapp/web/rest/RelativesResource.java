package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Relatives;
import com.mycompany.myapp.repository.RelativesRepository;
import com.mycompany.myapp.service.RelativesService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Relatives}.
 */
@RestController
@RequestMapping("/api")
public class RelativesResource {

    private final Logger log = LoggerFactory.getLogger(RelativesResource.class);

    private static final String ENTITY_NAME = "relatives";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RelativesService relativesService;

    private final RelativesRepository relativesRepository;

    public RelativesResource(RelativesService relativesService, RelativesRepository relativesRepository) {
        this.relativesService = relativesService;
        this.relativesRepository = relativesRepository;
    }

    /**
     * {@code POST  /relatives} : Create a new relatives.
     *
     * @param relatives the relatives to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new relatives, or with status {@code 400 (Bad Request)} if the relatives has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/relatives")
    public ResponseEntity<Relatives> createRelatives(@Valid @RequestBody Relatives relatives) throws URISyntaxException {
        log.debug("REST request to save Relatives : {}", relatives);
        if (relatives.getId() != null) {
            throw new BadRequestAlertException("A new relatives cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Relatives result = relativesService.save(relatives);
        return ResponseEntity
            .created(new URI("/api/relatives/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /relatives/:id} : Updates an existing relatives.
     *
     * @param id the id of the relatives to save.
     * @param relatives the relatives to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated relatives,
     * or with status {@code 400 (Bad Request)} if the relatives is not valid,
     * or with status {@code 500 (Internal Server Error)} if the relatives couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/relatives/{id}")
    public ResponseEntity<Relatives> updateRelatives(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Relatives relatives
    ) throws URISyntaxException {
        log.debug("REST request to update Relatives : {}, {}", id, relatives);
        if (relatives.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, relatives.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!relativesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Relatives result = relativesService.save(relatives);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, relatives.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /relatives/:id} : Partial updates given fields of an existing relatives, field will ignore if it is null
     *
     * @param id the id of the relatives to save.
     * @param relatives the relatives to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated relatives,
     * or with status {@code 400 (Bad Request)} if the relatives is not valid,
     * or with status {@code 404 (Not Found)} if the relatives is not found,
     * or with status {@code 500 (Internal Server Error)} if the relatives couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/relatives/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Relatives> partialUpdateRelatives(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Relatives relatives
    ) throws URISyntaxException {
        log.debug("REST request to partial update Relatives partially : {}, {}", id, relatives);
        if (relatives.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, relatives.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!relativesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Relatives> result = relativesService.partialUpdate(relatives);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, relatives.getId().toString())
        );
    }

    /**
     * {@code GET  /relatives} : get all the relatives.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of relatives in body.
     */
    @GetMapping("/relatives")
    public ResponseEntity<List<Relatives>> getAllRelatives(Pageable pageable) {
        log.debug("REST request to get a page of Relatives");
        Page<Relatives> page = relativesService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /relatives/:id} : get the "id" relatives.
     *
     * @param id the id of the relatives to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the relatives, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/relatives/{id}")
    public ResponseEntity<Relatives> getRelatives(@PathVariable Long id) {
        log.debug("REST request to get Relatives : {}", id);
        Optional<Relatives> relatives = relativesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(relatives);
    }

    /**
     * {@code DELETE  /relatives/:id} : delete the "id" relatives.
     *
     * @param id the id of the relatives to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/relatives/{id}")
    public ResponseEntity<Void> deleteRelatives(@PathVariable Long id) {
        log.debug("REST request to delete Relatives : {}", id);
        relativesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
