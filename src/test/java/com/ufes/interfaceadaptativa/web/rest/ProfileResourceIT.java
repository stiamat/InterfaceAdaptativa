package com.ufes.interfaceadaptativa.web.rest;

import com.ufes.interfaceadaptativa.InterfaceAdaptativaApp;
import com.ufes.interfaceadaptativa.domain.Profile;
import com.ufes.interfaceadaptativa.domain.User;
import com.ufes.interfaceadaptativa.repository.ProfileRepository;
import com.ufes.interfaceadaptativa.service.ProfileService;
import com.ufes.interfaceadaptativa.service.dto.ProfileDTO;
import com.ufes.interfaceadaptativa.service.mapper.ProfileMapper;
import com.ufes.interfaceadaptativa.service.dto.ProfileCriteria;
import com.ufes.interfaceadaptativa.service.ProfileQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static com.ufes.interfaceadaptativa.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ufes.interfaceadaptativa.domain.enumeration.StatusProfile;
/**
 * Integration tests for the {@link ProfileResource} REST controller.
 */
@SpringBootTest(classes = InterfaceAdaptativaApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class ProfileResourceIT {

    private static final StatusProfile DEFAULT_STATUS = StatusProfile.NOVO;
    private static final StatusProfile UPDATED_STATUS = StatusProfile.ATUAL;

    private static final ZonedDateTime DEFAULT_ULTIMA_MODIFICACAO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_ULTIMA_MODIFICACAO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_ULTIMA_MODIFICACAO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    @Autowired
    private ProfileRepository profileRepository;

    @Mock
    private ProfileRepository profileRepositoryMock;

    @Autowired
    private ProfileMapper profileMapper;

    @Mock
    private ProfileService profileServiceMock;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ProfileQueryService profileQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProfileMockMvc;

    private Profile profile;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Profile createEntity(EntityManager em) {
        Profile profile = new Profile()
            .status(DEFAULT_STATUS)
            .ultimaModificacao(DEFAULT_ULTIMA_MODIFICACAO);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        profile.setUser(user);
        return profile;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Profile createUpdatedEntity(EntityManager em) {
        Profile profile = new Profile()
            .status(UPDATED_STATUS)
            .ultimaModificacao(UPDATED_ULTIMA_MODIFICACAO);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        profile.setUser(user);
        return profile;
    }

    @BeforeEach
    public void initTest() {
        profile = createEntity(em);
    }

    @Test
    @Transactional
    public void createProfile() throws Exception {
        int databaseSizeBeforeCreate = profileRepository.findAll().size();
        // Create the Profile
        ProfileDTO profileDTO = profileMapper.toDto(profile);
        restProfileMockMvc.perform(post("/api/profiles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(profileDTO)))
            .andExpect(status().isCreated());

        // Validate the Profile in the database
        List<Profile> profileList = profileRepository.findAll();
        assertThat(profileList).hasSize(databaseSizeBeforeCreate + 1);
        Profile testProfile = profileList.get(profileList.size() - 1);
        assertThat(testProfile.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testProfile.getUltimaModificacao()).isEqualTo(DEFAULT_ULTIMA_MODIFICACAO);

        // Validate the id for MapsId, the ids must be same
        assertThat(testProfile.getId()).isEqualTo(testProfile.getUser().getId());
    }

    @Test
    @Transactional
    public void createProfileWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = profileRepository.findAll().size();

        // Create the Profile with an existing ID
        profile.setId(1L);
        ProfileDTO profileDTO = profileMapper.toDto(profile);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProfileMockMvc.perform(post("/api/profiles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(profileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Profile in the database
        List<Profile> profileList = profileRepository.findAll();
        assertThat(profileList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void updateProfileMapsIdAssociationWithNewId() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);
        int databaseSizeBeforeCreate = profileRepository.findAll().size();

        // Add a new parent entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();

        // Load the profile
        Profile updatedProfile = profileRepository.findById(profile.getId()).get();
        // Disconnect from session so that the updates on updatedProfile are not directly saved in db
        em.detach(updatedProfile);

        // Update the User with new association value
        updatedProfile.setUser(user);
        ProfileDTO updatedProfileDTO = profileMapper.toDto(updatedProfile);

        // Update the entity
        restProfileMockMvc.perform(put("/api/profiles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedProfileDTO)))
            .andExpect(status().isOk());

        // Validate the Profile in the database
        List<Profile> profileList = profileRepository.findAll();
        assertThat(profileList).hasSize(databaseSizeBeforeCreate);
        Profile testProfile = profileList.get(profileList.size() - 1);

        // Validate the id for MapsId, the ids must be same
        // Uncomment the following line for assertion. However, please note that there is a known issue and uncommenting will fail the test.
        // Please look at https://github.com/jhipster/generator-jhipster/issues/9100. You can modify this test as necessary.
        // assertThat(testProfile.getId()).isEqualTo(testProfile.getUser().getId());
    }

    @Test
    @Transactional
    public void getAllProfiles() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList
        restProfileMockMvc.perform(get("/api/profiles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(profile.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].ultimaModificacao").value(hasItem(sameInstant(DEFAULT_ULTIMA_MODIFICACAO))));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllProfilesWithEagerRelationshipsIsEnabled() throws Exception {
        when(profileServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProfileMockMvc.perform(get("/api/profiles?eagerload=true"))
            .andExpect(status().isOk());

        verify(profileServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllProfilesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(profileServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProfileMockMvc.perform(get("/api/profiles?eagerload=true"))
            .andExpect(status().isOk());

        verify(profileServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getProfile() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get the profile
        restProfileMockMvc.perform(get("/api/profiles/{id}", profile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(profile.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.ultimaModificacao").value(sameInstant(DEFAULT_ULTIMA_MODIFICACAO)));
    }


    @Test
    @Transactional
    public void getProfilesByIdFiltering() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        Long id = profile.getId();

        defaultProfileShouldBeFound("id.equals=" + id);
        defaultProfileShouldNotBeFound("id.notEquals=" + id);

        defaultProfileShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProfileShouldNotBeFound("id.greaterThan=" + id);

        defaultProfileShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProfileShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllProfilesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where status equals to DEFAULT_STATUS
        defaultProfileShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the profileList where status equals to UPDATED_STATUS
        defaultProfileShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllProfilesByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where status not equals to DEFAULT_STATUS
        defaultProfileShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the profileList where status not equals to UPDATED_STATUS
        defaultProfileShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllProfilesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultProfileShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the profileList where status equals to UPDATED_STATUS
        defaultProfileShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllProfilesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where status is not null
        defaultProfileShouldBeFound("status.specified=true");

        // Get all the profileList where status is null
        defaultProfileShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesByUltimaModificacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where ultimaModificacao equals to DEFAULT_ULTIMA_MODIFICACAO
        defaultProfileShouldBeFound("ultimaModificacao.equals=" + DEFAULT_ULTIMA_MODIFICACAO);

        // Get all the profileList where ultimaModificacao equals to UPDATED_ULTIMA_MODIFICACAO
        defaultProfileShouldNotBeFound("ultimaModificacao.equals=" + UPDATED_ULTIMA_MODIFICACAO);
    }

    @Test
    @Transactional
    public void getAllProfilesByUltimaModificacaoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where ultimaModificacao not equals to DEFAULT_ULTIMA_MODIFICACAO
        defaultProfileShouldNotBeFound("ultimaModificacao.notEquals=" + DEFAULT_ULTIMA_MODIFICACAO);

        // Get all the profileList where ultimaModificacao not equals to UPDATED_ULTIMA_MODIFICACAO
        defaultProfileShouldBeFound("ultimaModificacao.notEquals=" + UPDATED_ULTIMA_MODIFICACAO);
    }

    @Test
    @Transactional
    public void getAllProfilesByUltimaModificacaoIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where ultimaModificacao in DEFAULT_ULTIMA_MODIFICACAO or UPDATED_ULTIMA_MODIFICACAO
        defaultProfileShouldBeFound("ultimaModificacao.in=" + DEFAULT_ULTIMA_MODIFICACAO + "," + UPDATED_ULTIMA_MODIFICACAO);

        // Get all the profileList where ultimaModificacao equals to UPDATED_ULTIMA_MODIFICACAO
        defaultProfileShouldNotBeFound("ultimaModificacao.in=" + UPDATED_ULTIMA_MODIFICACAO);
    }

    @Test
    @Transactional
    public void getAllProfilesByUltimaModificacaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where ultimaModificacao is not null
        defaultProfileShouldBeFound("ultimaModificacao.specified=true");

        // Get all the profileList where ultimaModificacao is null
        defaultProfileShouldNotBeFound("ultimaModificacao.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesByUltimaModificacaoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where ultimaModificacao is greater than or equal to DEFAULT_ULTIMA_MODIFICACAO
        defaultProfileShouldBeFound("ultimaModificacao.greaterThanOrEqual=" + DEFAULT_ULTIMA_MODIFICACAO);

        // Get all the profileList where ultimaModificacao is greater than or equal to UPDATED_ULTIMA_MODIFICACAO
        defaultProfileShouldNotBeFound("ultimaModificacao.greaterThanOrEqual=" + UPDATED_ULTIMA_MODIFICACAO);
    }

    @Test
    @Transactional
    public void getAllProfilesByUltimaModificacaoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where ultimaModificacao is less than or equal to DEFAULT_ULTIMA_MODIFICACAO
        defaultProfileShouldBeFound("ultimaModificacao.lessThanOrEqual=" + DEFAULT_ULTIMA_MODIFICACAO);

        // Get all the profileList where ultimaModificacao is less than or equal to SMALLER_ULTIMA_MODIFICACAO
        defaultProfileShouldNotBeFound("ultimaModificacao.lessThanOrEqual=" + SMALLER_ULTIMA_MODIFICACAO);
    }

    @Test
    @Transactional
    public void getAllProfilesByUltimaModificacaoIsLessThanSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where ultimaModificacao is less than DEFAULT_ULTIMA_MODIFICACAO
        defaultProfileShouldNotBeFound("ultimaModificacao.lessThan=" + DEFAULT_ULTIMA_MODIFICACAO);

        // Get all the profileList where ultimaModificacao is less than UPDATED_ULTIMA_MODIFICACAO
        defaultProfileShouldBeFound("ultimaModificacao.lessThan=" + UPDATED_ULTIMA_MODIFICACAO);
    }

    @Test
    @Transactional
    public void getAllProfilesByUltimaModificacaoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where ultimaModificacao is greater than DEFAULT_ULTIMA_MODIFICACAO
        defaultProfileShouldNotBeFound("ultimaModificacao.greaterThan=" + DEFAULT_ULTIMA_MODIFICACAO);

        // Get all the profileList where ultimaModificacao is greater than SMALLER_ULTIMA_MODIFICACAO
        defaultProfileShouldBeFound("ultimaModificacao.greaterThan=" + SMALLER_ULTIMA_MODIFICACAO);
    }


    @Test
    @Transactional
    public void getAllProfilesByUserIsEqualToSomething() throws Exception {
        // Get already existing entity
        User user = profile.getUser();
        profileRepository.saveAndFlush(profile);
        Long userId = user.getId();

        // Get all the profileList where user equals to userId
        defaultProfileShouldBeFound("userId.equals=" + userId);

        // Get all the profileList where user equals to userId + 1
        defaultProfileShouldNotBeFound("userId.equals=" + (userId + 1));
    }


    @Test
    @Transactional
    public void getAllProfilesByListFriendsIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);
        User listFriends = UserResourceIT.createEntity(em);
        em.persist(listFriends);
        em.flush();
        profile.addListFriends(listFriends);
        profileRepository.saveAndFlush(profile);
        Long listFriendsId = listFriends.getId();

        // Get all the profileList where listFriends equals to listFriendsId
        defaultProfileShouldBeFound("listFriendsId.equals=" + listFriendsId);

        // Get all the profileList where listFriends equals to listFriendsId + 1
        defaultProfileShouldNotBeFound("listFriendsId.equals=" + (listFriendsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProfileShouldBeFound(String filter) throws Exception {
        restProfileMockMvc.perform(get("/api/profiles?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(profile.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].ultimaModificacao").value(hasItem(sameInstant(DEFAULT_ULTIMA_MODIFICACAO))));

        // Check, that the count call also returns 1
        restProfileMockMvc.perform(get("/api/profiles/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProfileShouldNotBeFound(String filter) throws Exception {
        restProfileMockMvc.perform(get("/api/profiles?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProfileMockMvc.perform(get("/api/profiles/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingProfile() throws Exception {
        // Get the profile
        restProfileMockMvc.perform(get("/api/profiles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProfile() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        int databaseSizeBeforeUpdate = profileRepository.findAll().size();

        // Update the profile
        Profile updatedProfile = profileRepository.findById(profile.getId()).get();
        // Disconnect from session so that the updates on updatedProfile are not directly saved in db
        em.detach(updatedProfile);
        updatedProfile
            .status(UPDATED_STATUS)
            .ultimaModificacao(UPDATED_ULTIMA_MODIFICACAO);
        ProfileDTO profileDTO = profileMapper.toDto(updatedProfile);

        restProfileMockMvc.perform(put("/api/profiles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(profileDTO)))
            .andExpect(status().isOk());

        // Validate the Profile in the database
        List<Profile> profileList = profileRepository.findAll();
        assertThat(profileList).hasSize(databaseSizeBeforeUpdate);
        Profile testProfile = profileList.get(profileList.size() - 1);
        assertThat(testProfile.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testProfile.getUltimaModificacao()).isEqualTo(UPDATED_ULTIMA_MODIFICACAO);
    }

    @Test
    @Transactional
    public void updateNonExistingProfile() throws Exception {
        int databaseSizeBeforeUpdate = profileRepository.findAll().size();

        // Create the Profile
        ProfileDTO profileDTO = profileMapper.toDto(profile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfileMockMvc.perform(put("/api/profiles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(profileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Profile in the database
        List<Profile> profileList = profileRepository.findAll();
        assertThat(profileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProfile() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        int databaseSizeBeforeDelete = profileRepository.findAll().size();

        // Delete the profile
        restProfileMockMvc.perform(delete("/api/profiles/{id}", profile.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Profile> profileList = profileRepository.findAll();
        assertThat(profileList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
