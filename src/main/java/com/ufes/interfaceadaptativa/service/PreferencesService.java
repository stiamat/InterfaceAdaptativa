package com.ufes.interfaceadaptativa.service;

import com.ufes.interfaceadaptativa.service.dto.PreferencesDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.ufes.interfaceadaptativa.domain.Preferences}.
 */
public interface PreferencesService {

    /**
     * Save a preferences.
     *
     * @param preferencesDTO the entity to save.
     * @return the persisted entity.
     */
    PreferencesDTO save(PreferencesDTO preferencesDTO);

    /**
     * Get all the preferences.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PreferencesDTO> findAll(Pageable pageable);


    /**
     * Get the "id" preferences.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PreferencesDTO> findOne(Long id);

    /**
     * Delete the "id" preferences.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
