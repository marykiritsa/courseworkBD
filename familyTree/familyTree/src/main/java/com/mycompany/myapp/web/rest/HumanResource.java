package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Human;
import com.mycompany.myapp.repository.HumanRepository;
import com.mycompany.myapp.service.HumanService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Human}.
 */
@RestController
@RequestMapping("/api")
public class HumanResource {

    private final Logger log = LoggerFactory.getLogger(HumanResource.class);

    private static final String ENTITY_NAME = "human";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HumanService humanService;

    private final HumanRepository humanRepository;

    public HumanResource(HumanService humanService, HumanRepository humanRepository) {
        this.humanService = humanService;
        this.humanRepository = humanRepository;
    }

    /**
     * {@code POST  /humans} : Create a new human.
     *
     * @param human the human to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new human, or with status {@code 400 (Bad Request)} if the human has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/humans")
    public ResponseEntity<Human> createHuman(@Valid @RequestBody Human human) throws URISyntaxException {
        log.debug("REST request to save Human : {}", human);
        if (human.getId() != null) {
            throw new BadRequestAlertException("A new human cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Human result = humanService.save(human);
        return ResponseEntity
            .created(new URI("/api/humans/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /humans/:id} : Updates an existing human.
     *
     * @param id the id of the human to save.
     * @param human the human to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated human,
     * or with status {@code 400 (Bad Request)} if the human is not valid,
     * or with status {@code 500 (Internal Server Error)} if the human couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/humans/{id}")
    public ResponseEntity<Human> updateHuman(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Human human)
        throws URISyntaxException {
        log.debug("REST request to update Human : {}, {}", id, human);
        if (human.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, human.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!humanRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Human result = humanService.save(human);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, human.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /humans/:id} : Partial updates given fields of an existing human, field will ignore if it is null
     *
     * @param id the id of the human to save.
     * @param human the human to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated human,
     * or with status {@code 400 (Bad Request)} if the human is not valid,
     * or with status {@code 404 (Not Found)} if the human is not found,
     * or with status {@code 500 (Internal Server Error)} if the human couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/humans/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Human> partialUpdateHuman(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Human human
    ) throws URISyntaxException {
        log.debug("REST request to partial update Human partially : {}, {}", id, human);
        if (human.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, human.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!humanRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Human> result = humanService.partialUpdate(human);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, human.getId().toString())
        );
    }

    /**
     * {@code GET  /humans} : get all the humans.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of humans in body.
     */
    @GetMapping("/humans")
    public ResponseEntity<List<Human>> getAllHumans(Pageable pageable) {
        log.debug("REST request to get a page of Humans");
        Page<Human> page = humanService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /humans/:id} : get the "id" human.
     *
     * @param id the id of the human to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the human, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/humans/{id}")
    public ResponseEntity<Human> getHuman(@PathVariable Long id) {
        log.debug("REST request to get Human : {}", id);
        Optional<Human> human = humanService.findOne(id);
        return ResponseUtil.wrapOrNotFound(human);
    }

    /**
     * {@code DELETE  /humans/:id} : delete the "id" human.
     *
     * @param id the id of the human to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/humans/{id}")
    public ResponseEntity<Void> deleteHuman(@PathVariable Long id) {
        log.debug("REST request to delete Human : {}", id);
        humanService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
