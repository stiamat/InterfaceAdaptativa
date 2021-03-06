package com.ufes.interfaceadaptativa.service.impl;

import com.ufes.interfaceadaptativa.domain.Profile;
import com.ufes.interfaceadaptativa.domain.User;
import com.ufes.interfaceadaptativa.domain.enumeration.StatusProfile;
import com.ufes.interfaceadaptativa.repository.ProfileRepository;
import com.ufes.interfaceadaptativa.repository.UserRepository;
import com.ufes.interfaceadaptativa.service.PostService;
import com.ufes.interfaceadaptativa.service.ProfileService;
import com.ufes.interfaceadaptativa.service.dto.ProfileDTO;
import com.ufes.interfaceadaptativa.service.mapper.ProfileMapper;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Profile}.
 */
@Service
@Transactional
public class ProfileServiceImpl implements ProfileService {
  private final Logger log = LoggerFactory.getLogger(ProfileServiceImpl.class);

  private final ProfileRepository profileRepository;

  private final ProfileMapper profileMapper;

  private final UserRepository userRepository;

  private final PostService postService;

  public ProfileServiceImpl(
    ProfileRepository profileRepository,
    ProfileMapper profileMapper,
    UserRepository userRepository,
    PostService postService
  ) {
    this.profileRepository = profileRepository;
    this.profileMapper = profileMapper;
    this.userRepository = userRepository;
    this.postService = postService;
  }

  @Override
  public ProfileDTO save(ProfileDTO profileDTO) {
    log.debug("Request to save Profile : {}", profileDTO);
    Profile profile = profileMapper.toEntity(profileDTO);
    Long userId = profileDTO.getUserId();
    userRepository.findById(userId).ifPresent(profile::user);
    profile = profileRepository.save(profile);
    if (profile.getStatus() != StatusProfile.NOVO) {
      this.postService.owl(profile.getId());
    }
    return profileMapper.toDto(profile);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<ProfileDTO> findAll(Pageable pageable) {
    log.debug("Request to get all Profiles");
    return profileRepository.findAll(pageable).map(profileMapper::toDto);
  }

  public Page<ProfileDTO> findAllWithEagerRelationships(Pageable pageable) {
    return profileRepository
      .findAllWithEagerRelationships(pageable)
      .map(profileMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<ProfileDTO> findOne(Long id) {
    log.debug("Request to get Profile : {}", id);
    return profileRepository
      .findOneWithEagerRelationships(id)
      .map(profileMapper::toDto);
  }

  @Override
  public void delete(Long id) {
    log.debug("Request to delete Profile : {}", id);

    profileRepository.deleteById(id);
  }

  @Override
  public ProfileDTO friends(long profileId, long userId) {
    Profile profile = profileRepository.findById(profileId).get();
    User user = userRepository.findById(userId).get();
    Set<User> friends = profile.getListFriends();

    if (friends.contains(user)) {
      friends.remove(user);
    } else {
      friends.add(user);
    }

    profile.setListFriends(friends);
    return profileMapper.toDto(profile);
  }
}
