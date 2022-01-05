package com.ufes.interfaceadaptativa.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.ufes.interfaceadaptativa.domain.Preferences;
import com.ufes.interfaceadaptativa.domain.*; // for static metamodels
import com.ufes.interfaceadaptativa.repository.PreferencesRepository;
import com.ufes.interfaceadaptativa.service.dto.PreferencesCriteria;
import com.ufes.interfaceadaptativa.service.dto.PreferencesDTO;
import com.ufes.interfaceadaptativa.service.mapper.PreferencesMapper;

/**
 * Service for executing complex queries for {@link Preferences} entities in the database.
 * The main input is a {@link PreferencesCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PreferencesDTO} or a {@link Page} of {@link PreferencesDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PreferencesQueryService extends QueryService<Preferences> {

    private final Logger log = LoggerFactory.getLogger(PreferencesQueryService.class);

    private final PreferencesRepository preferencesRepository;

    private final PreferencesMapper preferencesMapper;

    public PreferencesQueryService(PreferencesRepository preferencesRepository, PreferencesMapper preferencesMapper) {
        this.preferencesRepository = preferencesRepository;
        this.preferencesMapper = preferencesMapper;
    }

    /**
     * Return a {@link List} of {@link PreferencesDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PreferencesDTO> findByCriteria(PreferencesCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Preferences> specification = createSpecification(criteria);
        return preferencesMapper.toDto(preferencesRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PreferencesDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PreferencesDTO> findByCriteria(PreferencesCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Preferences> specification = createSpecification(criteria);
        return preferencesRepository.findAll(specification, page)
            .map(preferencesMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PreferencesCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Preferences> specification = createSpecification(criteria);
        return preferencesRepository.count(specification);
    }

    /**
     * Function to convert {@link PreferencesCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Preferences> createSpecification(PreferencesCriteria criteria) {
        Specification<Preferences> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Preferences_.id));
            }
            if (criteria.getDarkMode() != null) {
                specification = specification.and(buildSpecification(criteria.getDarkMode(), Preferences_.darkMode));
            }
            if (criteria.getExperienceLevelMode() != null) {
                specification = specification.and(buildSpecification(criteria.getExperienceLevelMode(), Preferences_.experienceLevelMode));
            }
            if (criteria.getFontMode() != null) {
                specification = specification.and(buildSpecification(criteria.getFontMode(), Preferences_.fontMode));
            }
            if (criteria.getContrastMode() != null) {
                specification = specification.and(buildSpecification(criteria.getContrastMode(), Preferences_.contrastMode));
            }
            if (criteria.getColorVisionMode() != null) {
                specification = specification.and(buildSpecification(criteria.getColorVisionMode(), Preferences_.colorVisionMode));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserId(),
                    root -> root.join(Preferences_.user, JoinType.LEFT).get(User_.id)));
            }
        }
        return specification;
    }
}
