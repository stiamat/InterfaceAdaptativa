package com.ufes.interfaceadaptativa.service.impl;

import com.ufes.interfaceadaptativa.domain.Preferences;
import com.ufes.interfaceadaptativa.domain.enumeration.ExperienceLevelMode;
import com.ufes.interfaceadaptativa.domain.enumeration.FontMode;
import com.ufes.interfaceadaptativa.domain.enumeration.StatusPreferences;
import com.ufes.interfaceadaptativa.repository.PreferencesRepository;
import com.ufes.interfaceadaptativa.repository.UserRepository;
import com.ufes.interfaceadaptativa.service.PreferencesService;
import com.ufes.interfaceadaptativa.service.dto.PreferencesDTO;
import com.ufes.interfaceadaptativa.service.mapper.PreferencesMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Preferences}.
 */
@Service
@Transactional
public class PreferencesServiceImpl implements PreferencesService {
  private final Logger log = LoggerFactory.getLogger(
    PreferencesServiceImpl.class
  );

  private final PreferencesRepository preferencesRepository;

  private final PreferencesMapper preferencesMapper;

  private final UserRepository userRepository;

  public PreferencesServiceImpl(
    PreferencesRepository preferencesRepository,
    PreferencesMapper preferencesMapper,
    UserRepository userRepository
  ) {
    this.preferencesRepository = preferencesRepository;
    this.preferencesMapper = preferencesMapper;
    this.userRepository = userRepository;
  }

  @Override
  public PreferencesDTO save(PreferencesDTO preferencesDTO) {
    log.debug("Request to save Preferences : {}", preferencesDTO);
    Preferences preferences = preferencesMapper.toEntity(preferencesDTO);
    Long userId = preferencesDTO.getUserId();
    userRepository.findById(userId).ifPresent(preferences::user);
    preferences = preferencesRepository.save(preferences);
    return preferencesMapper.toDto(preferences);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<PreferencesDTO> findAll(Pageable pageable) {
    log.debug("Request to get all Preferences");
    return preferencesRepository
      .findAll(pageable)
      .map(preferencesMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<PreferencesDTO> findOne(Long id) {
    log.debug("Request to get Preferences : {}", id);
    return preferencesRepository.findById(id).map(preferencesMapper::toDto);
  }

  @Override
  public void delete(Long id) {
    log.debug("Request to delete Preferences : {}", id);
    preferencesRepository.deleteById(id);
  }

  public void updatePreferencesByReasoner(String preference, long id) {
    Preferences pref = preferencesRepository.findById(id).get();

    switch (preference) {
      case "Dark_Mode":
        pref.setDarkMode(StatusPreferences.TRUE);
        break;
      case "Contrast_Mode":
        pref.setContrastMode(true);
        break;
      case "Font_Decrease":
        pref.setFontMode(FontMode.DECREASE);
        break;
      case "Font_Increase":
        pref.setFontMode(FontMode.INCREASE);
        break;
      case "Color_Vision_Mode":
        pref.setColorVisionMode(true);
        break;
      case "Basic_Mode":
        pref.setExperienceLevelMode(ExperienceLevelMode.BASICMODE);
        break;
      case "Average_Mode":
        pref.setExperienceLevelMode(ExperienceLevelMode.AVERAGEMODE);
        break;
      case "High_Mode":
        pref.setExperienceLevelMode(ExperienceLevelMode.HIGHMODE);
        break;
      default:
        break;
    }
    pref = preferencesRepository.save(pref);
    return;
  }

  public void clearPreferences(long id) {
    Preferences pref = preferencesRepository.findById(id).get();

    pref.setContrastMode(false);
    pref.setDarkMode(StatusPreferences.AUTO);
    pref.setFontMode(FontMode.NORMAL);
    pref.setColorVisionMode(false);
    pref.setExperienceLevelMode(ExperienceLevelMode.BASICMODE);

    pref = preferencesRepository.save(pref);
    return;
  }
}
