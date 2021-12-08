package com.ufes.interfaceadaptativa.web.rest;

import com.ufes.interfaceadaptativa.service.PreferencesService;
import com.ufes.interfaceadaptativa.web.rest.errors.BadRequestAlertException;
import com.ufes.interfaceadaptativa.service.dto.PreferencesDTO;
import com.ufes.interfaceadaptativa.service.dto.PreferencesCriteria;
import com.ufes.interfaceadaptativa.service.PreferencesQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link com.ufes.interfaceadaptativa.domain.Preferences}.
 */
@RestController
@RequestMapping("/api")
public class PreferencesResource {

    private final Logger log = LoggerFactory.getLogger(PreferencesResource.class);

    private static final String ENTITY_NAME = "preferences";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PreferencesService preferencesService;

    private final PreferencesQueryService preferencesQueryService;

    public PreferencesResource(PreferencesService preferencesService, PreferencesQueryService preferencesQueryService) {
        this.preferencesService = preferencesService;
        this.preferencesQueryService = preferencesQueryService;
    }

    /**
     * {@code POST  /preferences} : Create a new preferences.
     *
     * @param preferencesDTO the preferencesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new preferencesDTO, or with status {@code 400 (Bad Request)} if the preferences has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/preferences")
    public ResponseEntity<PreferencesDTO> createPreferences(@RequestBody PreferencesDTO preferencesDTO) throws URISyntaxException {
        log.debug("REST request to save Preferences : {}", preferencesDTO);
        if (preferencesDTO.getId() != null) {
            throw new BadRequestAlertException("A new preferences cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (Objects.isNull(preferencesDTO.getUserId())) {
            throw new BadRequestAlertException("Invalid association value provided", ENTITY_NAME, "null");
        }
        PreferencesDTO result = preferencesService.save(preferencesDTO);
        return ResponseEntity.created(new URI("/api/preferences/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /preferences} : Updates an existing preferences.
     *
     * @param preferencesDTO the preferencesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated preferencesDTO,
     * or with status {@code 400 (Bad Request)} if the preferencesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the preferencesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/preferences")
    public ResponseEntity<PreferencesDTO> updatePreferences(@RequestBody PreferencesDTO preferencesDTO) throws URISyntaxException {
        log.debug("REST request to update Preferences : {}", preferencesDTO);
        if (preferencesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PreferencesDTO result = preferencesService.save(preferencesDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, preferencesDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /preferences} : get all the preferences.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of preferences in body.
     */
    @GetMapping("/preferences")
    public ResponseEntity<List<PreferencesDTO>> getAllPreferences(PreferencesCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Preferences by criteria: {}", criteria);
        Page<PreferencesDTO> page = preferencesQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /preferences/count} : count all the preferences.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/preferences/count")
    public ResponseEntity<Long> countPreferences(PreferencesCriteria criteria) {
        log.debug("REST request to count Preferences by criteria: {}", criteria);
        return ResponseEntity.ok().body(preferencesQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /preferences/:id} : get the "id" preferences.
     *
     * @param id the id of the preferencesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the preferencesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/preferences/{id}")
    public ResponseEntity<PreferencesDTO> getPreferences(@PathVariable Long id) {
        log.debug("REST request to get Preferences : {}", id);
        Optional<PreferencesDTO> preferencesDTO = preferencesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(preferencesDTO);
    }

    /**
     * {@code DELETE  /preferences/:id} : delete the "id" preferences.
     *
     * @param id the id of the preferencesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/preferences/{id}")
    public ResponseEntity<Void> deletePreferences(@PathVariable Long id) {
        log.debug("REST request to delete Preferences : {}", id);
        preferencesService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
