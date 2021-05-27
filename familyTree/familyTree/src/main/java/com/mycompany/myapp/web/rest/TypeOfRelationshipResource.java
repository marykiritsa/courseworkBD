package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.TypeOfRelationship;
import com.mycompany.myapp.repository.TypeOfRelationshipRepository;
import com.mycompany.myapp.service.TypeOfRelationshipService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.TypeOfRelationship}.
 */
@RestController
@RequestMapping("/api")
public class TypeOfRelationshipResource {

    private final Logger log = LoggerFactory.getLogger(TypeOfRelationshipResource.class);

    private static final String ENTITY_NAME = "typeOfRelationship";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TypeOfRelationshipService typeOfRelationshipService;

    private final TypeOfRelationshipRepository typeOfRelationshipRepository;

    public TypeOfRelationshipResource(
        TypeOfRelationshipService typeOfRelationshipService,
        TypeOfRelationshipRepository typeOfRelationshipRepository
    ) {
        this.typeOfRelationshipService = typeOfRelationshipService;
        this.typeOfRelationshipRepository = typeOfRelationshipRepository;
    }

    /**
     * {@code POST  /type-of-relationships} : Create a new typeOfRelationship.
     *
     * @param typeOfRelationship the typeOfRelationship to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new typeOfRelationship, or with status {@code 400 (Bad Request)} if the typeOfRelationship has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/type-of-relationships")
    public ResponseEntity<TypeOfRelationship> createTypeOfRelationship(@Valid @RequestBody TypeOfRelationship typeOfRelationship)
        throws URISyntaxException {
        log.debug("REST request to save TypeOfRelationship : {}", typeOfRelationship);
        if (typeOfRelationship.getId() != null) {
            throw new BadRequestAlertException("A new typeOfRelationship cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TypeOfRelationship result = typeOfRelationshipService.save(typeOfRelationship);
        return ResponseEntity
            .created(new URI("/api/type-of-relationships/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /type-of-relationships/:id} : Updates an existing typeOfRelationship.
     *
     * @param id the id of the typeOfRelationship to save.
     * @param typeOfRelationship the typeOfRelationship to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeOfRelationship,
     * or with status {@code 400 (Bad Request)} if the typeOfRelationship is not valid,
     * or with status {@code 500 (Internal Server Error)} if the typeOfRelationship couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/type-of-relationships/{id}")
    public ResponseEntity<TypeOfRelationship> updateTypeOfRelationship(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TypeOfRelationship typeOfRelationship
    ) throws URISyntaxException {
        log.debug("REST request to update TypeOfRelationship : {}, {}", id, typeOfRelationship);
        if (typeOfRelationship.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeOfRelationship.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeOfRelationshipRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TypeOfRelationship result = typeOfRelationshipService.save(typeOfRelationship);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, typeOfRelationship.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /type-of-relationships/:id} : Partial updates given fields of an existing typeOfRelationship, field will ignore if it is null
     *
     * @param id the id of the typeOfRelationship to save.
     * @param typeOfRelationship the typeOfRelationship to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeOfRelationship,
     * or with status {@code 400 (Bad Request)} if the typeOfRelationship is not valid,
     * or with status {@code 404 (Not Found)} if the typeOfRelationship is not found,
     * or with status {@code 500 (Internal Server Error)} if the typeOfRelationship couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/type-of-relationships/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<TypeOfRelationship> partialUpdateTypeOfRelationship(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TypeOfRelationship typeOfRelationship
    ) throws URISyntaxException {
        log.debug("REST request to partial update TypeOfRelationship partially : {}, {}", id, typeOfRelationship);
        if (typeOfRelationship.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeOfRelationship.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeOfRelationshipRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TypeOfRelationship> result = typeOfRelationshipService.partialUpdate(typeOfRelationship);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, typeOfRelationship.getId().toString())
        );
    }

    /**
     * {@code GET  /type-of-relationships} : get all the typeOfRelationships.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of typeOfRelationships in body.
     */
    @GetMapping("/type-of-relationships")
    public ResponseEntity<List<TypeOfRelationship>> getAllTypeOfRelationships(Pageable pageable) {
        log.debug("REST request to get a page of TypeOfRelationships");
        Page<TypeOfRelationship> page = typeOfRelationshipService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /type-of-relationships/:id} : get the "id" typeOfRelationship.
     *
     * @param id the id of the typeOfRelationship to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the typeOfRelationship, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/type-of-relationships/{id}")
    public ResponseEntity<TypeOfRelationship> getTypeOfRelationship(@PathVariable Long id) {
        log.debug("REST request to get TypeOfRelationship : {}", id);
        Optional<TypeOfRelationship> typeOfRelationship = typeOfRelationshipService.findOne(id);
        return ResponseUtil.wrapOrNotFound(typeOfRelationship);
    }

    /**
     * {@code DELETE  /type-of-relationships/:id} : delete the "id" typeOfRelationship.
     *
     * @param id the id of the typeOfRelationship to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/type-of-relationships/{id}")
    public ResponseEntity<Void> deleteTypeOfRelationship(@PathVariable Long id) {
        log.debug("REST request to delete TypeOfRelationship : {}", id);
        typeOfRelationshipService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
