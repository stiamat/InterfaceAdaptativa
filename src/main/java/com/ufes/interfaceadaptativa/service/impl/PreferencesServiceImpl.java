package com.ufes.interfaceadaptativa.service.impl;

import com.ufes.interfaceadaptativa.service.PreferencesService;
import com.ufes.interfaceadaptativa.domain.Preferences;
import com.ufes.interfaceadaptativa.repository.PreferencesRepository;
import com.ufes.interfaceadaptativa.repository.UserRepository;
import com.ufes.interfaceadaptativa.service.dto.PreferencesDTO;
import com.ufes.interfaceadaptativa.service.mapper.PreferencesMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Preferences}.
 */
@Service
@Transactional
public class PreferencesServiceImpl implements PreferencesService {

    private final Logger log = LoggerFactory.getLogger(PreferencesServiceImpl.class);

    private final PreferencesRepository preferencesRepository;

    private final PreferencesMapper preferencesMapper;

    private final UserRepository userRepository;

    public PreferencesServiceImpl(PreferencesRepository preferencesRepository, PreferencesMapper preferencesMapper, UserRepository userRepository) {
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
        return preferencesRepository.findAll(pageable)
            .map(preferencesMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<PreferencesDTO> findOne(Long id) {
        log.debug("Request to get Preferences : {}", id);
        return preferencesRepository.findById(id)
            .map(preferencesMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Preferences : {}", id);
        preferencesRepository.deleteById(id);
    }
}
