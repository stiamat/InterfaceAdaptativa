package com.ufes.interfaceadaptativa.repository;

import com.ufes.interfaceadaptativa.domain.Preferences;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Preferences entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PreferencesRepository extends JpaRepository<Preferences, Long>, JpaSpecificationExecutor<Preferences> {
}
