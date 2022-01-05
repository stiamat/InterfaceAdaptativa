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

    private static final Integer DEFAULT_NUM_MODIFICACAO = 1;
    private static final Integer UPDATED_NUM_MODIFICACAO = 2;
    private static final Integer SMALLER_NUM_MODIFICACAO = 1 - 1;

    private static final Integer DEFAULT_AGE = 1;
    private static final Integer UPDATED_AGE = 2;
    private static final Integer SMALLER_AGE = 1 - 1;

    private static final Boolean DEFAULT_AUDITORY_DISABILITIES = false;
    private static final Boolean UPDATED_AUDITORY_DISABILITIES = true;

    private static final Boolean DEFAULT_BLINDNESS = false;
    private static final Boolean UPDATED_BLINDNESS = true;

    private static final Boolean DEFAULT_COLOR_VISION = false;
    private static final Boolean UPDATED_COLOR_VISION = true;

    private static final Boolean DEFAULT_CONTRAST_SENSITIVITY = false;
    private static final Boolean UPDATED_CONTRAST_SENSITIVITY = true;

    private static final Boolean DEFAULT_FILD_OF_VISION = false;
    private static final Boolean UPDATED_FILD_OF_VISION = true;

    private static final Boolean DEFAULT_LIGHT_SENSITIVITY = false;
    private static final Boolean UPDATED_LIGHT_SENSITIVITY = true;

    private static final Boolean DEFAULT_VISUAL_ACUITY = false;
    private static final Boolean UPDATED_VISUAL_ACUITY = true;

    private static final String DEFAULT_EDUCATION = "AAAAAAAAAA";
    private static final String UPDATED_EDUCATION = "BBBBBBBBBB";

    private static final String DEFAULT_EXPERIENCE_LEVEL = "AAAAAAAAAA";
    private static final String UPDATED_EXPERIENCE_LEVEL = "BBBBBBBBBB";

    private static final String DEFAULT_GENDER = "AAAAAAAAAA";
    private static final String UPDATED_GENDER = "BBBBBBBBBB";

    private static final String DEFAULT_LANGUAGE = "AAAAAAAAAA";
    private static final String UPDATED_LANGUAGE = "BBBBBBBBBB";

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
            .ultimaModificacao(DEFAULT_ULTIMA_MODIFICACAO)
            .numModificacao(DEFAULT_NUM_MODIFICACAO)
            .age(DEFAULT_AGE)
            .auditoryDisabilities(DEFAULT_AUDITORY_DISABILITIES)
            .blindness(DEFAULT_BLINDNESS)
            .colorVision(DEFAULT_COLOR_VISION)
            .contrastSensitivity(DEFAULT_CONTRAST_SENSITIVITY)
            .fildOfVision(DEFAULT_FILD_OF_VISION)
            .lightSensitivity(DEFAULT_LIGHT_SENSITIVITY)
            .visualAcuity(DEFAULT_VISUAL_ACUITY)
            .education(DEFAULT_EDUCATION)
            .experienceLevel(DEFAULT_EXPERIENCE_LEVEL)
            .gender(DEFAULT_GENDER)
            .language(DEFAULT_LANGUAGE);
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
            .ultimaModificacao(UPDATED_ULTIMA_MODIFICACAO)
            .numModificacao(UPDATED_NUM_MODIFICACAO)
            .age(UPDATED_AGE)
            .auditoryDisabilities(UPDATED_AUDITORY_DISABILITIES)
            .blindness(UPDATED_BLINDNESS)
            .colorVision(UPDATED_COLOR_VISION)
            .contrastSensitivity(UPDATED_CONTRAST_SENSITIVITY)
            .fildOfVision(UPDATED_FILD_OF_VISION)
            .lightSensitivity(UPDATED_LIGHT_SENSITIVITY)
            .visualAcuity(UPDATED_VISUAL_ACUITY)
            .education(UPDATED_EDUCATION)
            .experienceLevel(UPDATED_EXPERIENCE_LEVEL)
            .gender(UPDATED_GENDER)
            .language(UPDATED_LANGUAGE);
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
        assertThat(testProfile.getNumModificacao()).isEqualTo(DEFAULT_NUM_MODIFICACAO);
        assertThat(testProfile.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testProfile.isAuditoryDisabilities()).isEqualTo(DEFAULT_AUDITORY_DISABILITIES);
        assertThat(testProfile.isBlindness()).isEqualTo(DEFAULT_BLINDNESS);
        assertThat(testProfile.isColorVision()).isEqualTo(DEFAULT_COLOR_VISION);
        assertThat(testProfile.isContrastSensitivity()).isEqualTo(DEFAULT_CONTRAST_SENSITIVITY);
        assertThat(testProfile.isFildOfVision()).isEqualTo(DEFAULT_FILD_OF_VISION);
        assertThat(testProfile.isLightSensitivity()).isEqualTo(DEFAULT_LIGHT_SENSITIVITY);
        assertThat(testProfile.isVisualAcuity()).isEqualTo(DEFAULT_VISUAL_ACUITY);
        assertThat(testProfile.getEducation()).isEqualTo(DEFAULT_EDUCATION);
        assertThat(testProfile.getExperienceLevel()).isEqualTo(DEFAULT_EXPERIENCE_LEVEL);
        assertThat(testProfile.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testProfile.getLanguage()).isEqualTo(DEFAULT_LANGUAGE);

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
            .andExpect(jsonPath("$.[*].ultimaModificacao").value(hasItem(sameInstant(DEFAULT_ULTIMA_MODIFICACAO))))
            .andExpect(jsonPath("$.[*].numModificacao").value(hasItem(DEFAULT_NUM_MODIFICACAO)))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)))
            .andExpect(jsonPath("$.[*].auditoryDisabilities").value(hasItem(DEFAULT_AUDITORY_DISABILITIES.booleanValue())))
            .andExpect(jsonPath("$.[*].blindness").value(hasItem(DEFAULT_BLINDNESS.booleanValue())))
            .andExpect(jsonPath("$.[*].colorVision").value(hasItem(DEFAULT_COLOR_VISION.booleanValue())))
            .andExpect(jsonPath("$.[*].contrastSensitivity").value(hasItem(DEFAULT_CONTRAST_SENSITIVITY.booleanValue())))
            .andExpect(jsonPath("$.[*].fildOfVision").value(hasItem(DEFAULT_FILD_OF_VISION.booleanValue())))
            .andExpect(jsonPath("$.[*].lightSensitivity").value(hasItem(DEFAULT_LIGHT_SENSITIVITY.booleanValue())))
            .andExpect(jsonPath("$.[*].visualAcuity").value(hasItem(DEFAULT_VISUAL_ACUITY.booleanValue())))
            .andExpect(jsonPath("$.[*].education").value(hasItem(DEFAULT_EDUCATION)))
            .andExpect(jsonPath("$.[*].experienceLevel").value(hasItem(DEFAULT_EXPERIENCE_LEVEL)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER)))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE)));
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
            .andExpect(jsonPath("$.ultimaModificacao").value(sameInstant(DEFAULT_ULTIMA_MODIFICACAO)))
            .andExpect(jsonPath("$.numModificacao").value(DEFAULT_NUM_MODIFICACAO))
            .andExpect(jsonPath("$.age").value(DEFAULT_AGE))
            .andExpect(jsonPath("$.auditoryDisabilities").value(DEFAULT_AUDITORY_DISABILITIES.booleanValue()))
            .andExpect(jsonPath("$.blindness").value(DEFAULT_BLINDNESS.booleanValue()))
            .andExpect(jsonPath("$.colorVision").value(DEFAULT_COLOR_VISION.booleanValue()))
            .andExpect(jsonPath("$.contrastSensitivity").value(DEFAULT_CONTRAST_SENSITIVITY.booleanValue()))
            .andExpect(jsonPath("$.fildOfVision").value(DEFAULT_FILD_OF_VISION.booleanValue()))
            .andExpect(jsonPath("$.lightSensitivity").value(DEFAULT_LIGHT_SENSITIVITY.booleanValue()))
            .andExpect(jsonPath("$.visualAcuity").value(DEFAULT_VISUAL_ACUITY.booleanValue()))
            .andExpect(jsonPath("$.education").value(DEFAULT_EDUCATION))
            .andExpect(jsonPath("$.experienceLevel").value(DEFAULT_EXPERIENCE_LEVEL))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER))
            .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE));
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
    public void getAllProfilesByNumModificacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where numModificacao equals to DEFAULT_NUM_MODIFICACAO
        defaultProfileShouldBeFound("numModificacao.equals=" + DEFAULT_NUM_MODIFICACAO);

        // Get all the profileList where numModificacao equals to UPDATED_NUM_MODIFICACAO
        defaultProfileShouldNotBeFound("numModificacao.equals=" + UPDATED_NUM_MODIFICACAO);
    }

    @Test
    @Transactional
    public void getAllProfilesByNumModificacaoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where numModificacao not equals to DEFAULT_NUM_MODIFICACAO
        defaultProfileShouldNotBeFound("numModificacao.notEquals=" + DEFAULT_NUM_MODIFICACAO);

        // Get all the profileList where numModificacao not equals to UPDATED_NUM_MODIFICACAO
        defaultProfileShouldBeFound("numModificacao.notEquals=" + UPDATED_NUM_MODIFICACAO);
    }

    @Test
    @Transactional
    public void getAllProfilesByNumModificacaoIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where numModificacao in DEFAULT_NUM_MODIFICACAO or UPDATED_NUM_MODIFICACAO
        defaultProfileShouldBeFound("numModificacao.in=" + DEFAULT_NUM_MODIFICACAO + "," + UPDATED_NUM_MODIFICACAO);

        // Get all the profileList where numModificacao equals to UPDATED_NUM_MODIFICACAO
        defaultProfileShouldNotBeFound("numModificacao.in=" + UPDATED_NUM_MODIFICACAO);
    }

    @Test
    @Transactional
    public void getAllProfilesByNumModificacaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where numModificacao is not null
        defaultProfileShouldBeFound("numModificacao.specified=true");

        // Get all the profileList where numModificacao is null
        defaultProfileShouldNotBeFound("numModificacao.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesByNumModificacaoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where numModificacao is greater than or equal to DEFAULT_NUM_MODIFICACAO
        defaultProfileShouldBeFound("numModificacao.greaterThanOrEqual=" + DEFAULT_NUM_MODIFICACAO);

        // Get all the profileList where numModificacao is greater than or equal to UPDATED_NUM_MODIFICACAO
        defaultProfileShouldNotBeFound("numModificacao.greaterThanOrEqual=" + UPDATED_NUM_MODIFICACAO);
    }

    @Test
    @Transactional
    public void getAllProfilesByNumModificacaoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where numModificacao is less than or equal to DEFAULT_NUM_MODIFICACAO
        defaultProfileShouldBeFound("numModificacao.lessThanOrEqual=" + DEFAULT_NUM_MODIFICACAO);

        // Get all the profileList where numModificacao is less than or equal to SMALLER_NUM_MODIFICACAO
        defaultProfileShouldNotBeFound("numModificacao.lessThanOrEqual=" + SMALLER_NUM_MODIFICACAO);
    }

    @Test
    @Transactional
    public void getAllProfilesByNumModificacaoIsLessThanSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where numModificacao is less than DEFAULT_NUM_MODIFICACAO
        defaultProfileShouldNotBeFound("numModificacao.lessThan=" + DEFAULT_NUM_MODIFICACAO);

        // Get all the profileList where numModificacao is less than UPDATED_NUM_MODIFICACAO
        defaultProfileShouldBeFound("numModificacao.lessThan=" + UPDATED_NUM_MODIFICACAO);
    }

    @Test
    @Transactional
    public void getAllProfilesByNumModificacaoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where numModificacao is greater than DEFAULT_NUM_MODIFICACAO
        defaultProfileShouldNotBeFound("numModificacao.greaterThan=" + DEFAULT_NUM_MODIFICACAO);

        // Get all the profileList where numModificacao is greater than SMALLER_NUM_MODIFICACAO
        defaultProfileShouldBeFound("numModificacao.greaterThan=" + SMALLER_NUM_MODIFICACAO);
    }


    @Test
    @Transactional
    public void getAllProfilesByAgeIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where age equals to DEFAULT_AGE
        defaultProfileShouldBeFound("age.equals=" + DEFAULT_AGE);

        // Get all the profileList where age equals to UPDATED_AGE
        defaultProfileShouldNotBeFound("age.equals=" + UPDATED_AGE);
    }

    @Test
    @Transactional
    public void getAllProfilesByAgeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where age not equals to DEFAULT_AGE
        defaultProfileShouldNotBeFound("age.notEquals=" + DEFAULT_AGE);

        // Get all the profileList where age not equals to UPDATED_AGE
        defaultProfileShouldBeFound("age.notEquals=" + UPDATED_AGE);
    }

    @Test
    @Transactional
    public void getAllProfilesByAgeIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where age in DEFAULT_AGE or UPDATED_AGE
        defaultProfileShouldBeFound("age.in=" + DEFAULT_AGE + "," + UPDATED_AGE);

        // Get all the profileList where age equals to UPDATED_AGE
        defaultProfileShouldNotBeFound("age.in=" + UPDATED_AGE);
    }

    @Test
    @Transactional
    public void getAllProfilesByAgeIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where age is not null
        defaultProfileShouldBeFound("age.specified=true");

        // Get all the profileList where age is null
        defaultProfileShouldNotBeFound("age.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesByAgeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where age is greater than or equal to DEFAULT_AGE
        defaultProfileShouldBeFound("age.greaterThanOrEqual=" + DEFAULT_AGE);

        // Get all the profileList where age is greater than or equal to UPDATED_AGE
        defaultProfileShouldNotBeFound("age.greaterThanOrEqual=" + UPDATED_AGE);
    }

    @Test
    @Transactional
    public void getAllProfilesByAgeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where age is less than or equal to DEFAULT_AGE
        defaultProfileShouldBeFound("age.lessThanOrEqual=" + DEFAULT_AGE);

        // Get all the profileList where age is less than or equal to SMALLER_AGE
        defaultProfileShouldNotBeFound("age.lessThanOrEqual=" + SMALLER_AGE);
    }

    @Test
    @Transactional
    public void getAllProfilesByAgeIsLessThanSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where age is less than DEFAULT_AGE
        defaultProfileShouldNotBeFound("age.lessThan=" + DEFAULT_AGE);

        // Get all the profileList where age is less than UPDATED_AGE
        defaultProfileShouldBeFound("age.lessThan=" + UPDATED_AGE);
    }

    @Test
    @Transactional
    public void getAllProfilesByAgeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where age is greater than DEFAULT_AGE
        defaultProfileShouldNotBeFound("age.greaterThan=" + DEFAULT_AGE);

        // Get all the profileList where age is greater than SMALLER_AGE
        defaultProfileShouldBeFound("age.greaterThan=" + SMALLER_AGE);
    }


    @Test
    @Transactional
    public void getAllProfilesByAuditoryDisabilitiesIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where auditoryDisabilities equals to DEFAULT_AUDITORY_DISABILITIES
        defaultProfileShouldBeFound("auditoryDisabilities.equals=" + DEFAULT_AUDITORY_DISABILITIES);

        // Get all the profileList where auditoryDisabilities equals to UPDATED_AUDITORY_DISABILITIES
        defaultProfileShouldNotBeFound("auditoryDisabilities.equals=" + UPDATED_AUDITORY_DISABILITIES);
    }

    @Test
    @Transactional
    public void getAllProfilesByAuditoryDisabilitiesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where auditoryDisabilities not equals to DEFAULT_AUDITORY_DISABILITIES
        defaultProfileShouldNotBeFound("auditoryDisabilities.notEquals=" + DEFAULT_AUDITORY_DISABILITIES);

        // Get all the profileList where auditoryDisabilities not equals to UPDATED_AUDITORY_DISABILITIES
        defaultProfileShouldBeFound("auditoryDisabilities.notEquals=" + UPDATED_AUDITORY_DISABILITIES);
    }

    @Test
    @Transactional
    public void getAllProfilesByAuditoryDisabilitiesIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where auditoryDisabilities in DEFAULT_AUDITORY_DISABILITIES or UPDATED_AUDITORY_DISABILITIES
        defaultProfileShouldBeFound("auditoryDisabilities.in=" + DEFAULT_AUDITORY_DISABILITIES + "," + UPDATED_AUDITORY_DISABILITIES);

        // Get all the profileList where auditoryDisabilities equals to UPDATED_AUDITORY_DISABILITIES
        defaultProfileShouldNotBeFound("auditoryDisabilities.in=" + UPDATED_AUDITORY_DISABILITIES);
    }

    @Test
    @Transactional
    public void getAllProfilesByAuditoryDisabilitiesIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where auditoryDisabilities is not null
        defaultProfileShouldBeFound("auditoryDisabilities.specified=true");

        // Get all the profileList where auditoryDisabilities is null
        defaultProfileShouldNotBeFound("auditoryDisabilities.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesByBlindnessIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where blindness equals to DEFAULT_BLINDNESS
        defaultProfileShouldBeFound("blindness.equals=" + DEFAULT_BLINDNESS);

        // Get all the profileList where blindness equals to UPDATED_BLINDNESS
        defaultProfileShouldNotBeFound("blindness.equals=" + UPDATED_BLINDNESS);
    }

    @Test
    @Transactional
    public void getAllProfilesByBlindnessIsNotEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where blindness not equals to DEFAULT_BLINDNESS
        defaultProfileShouldNotBeFound("blindness.notEquals=" + DEFAULT_BLINDNESS);

        // Get all the profileList where blindness not equals to UPDATED_BLINDNESS
        defaultProfileShouldBeFound("blindness.notEquals=" + UPDATED_BLINDNESS);
    }

    @Test
    @Transactional
    public void getAllProfilesByBlindnessIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where blindness in DEFAULT_BLINDNESS or UPDATED_BLINDNESS
        defaultProfileShouldBeFound("blindness.in=" + DEFAULT_BLINDNESS + "," + UPDATED_BLINDNESS);

        // Get all the profileList where blindness equals to UPDATED_BLINDNESS
        defaultProfileShouldNotBeFound("blindness.in=" + UPDATED_BLINDNESS);
    }

    @Test
    @Transactional
    public void getAllProfilesByBlindnessIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where blindness is not null
        defaultProfileShouldBeFound("blindness.specified=true");

        // Get all the profileList where blindness is null
        defaultProfileShouldNotBeFound("blindness.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesByColorVisionIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where colorVision equals to DEFAULT_COLOR_VISION
        defaultProfileShouldBeFound("colorVision.equals=" + DEFAULT_COLOR_VISION);

        // Get all the profileList where colorVision equals to UPDATED_COLOR_VISION
        defaultProfileShouldNotBeFound("colorVision.equals=" + UPDATED_COLOR_VISION);
    }

    @Test
    @Transactional
    public void getAllProfilesByColorVisionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where colorVision not equals to DEFAULT_COLOR_VISION
        defaultProfileShouldNotBeFound("colorVision.notEquals=" + DEFAULT_COLOR_VISION);

        // Get all the profileList where colorVision not equals to UPDATED_COLOR_VISION
        defaultProfileShouldBeFound("colorVision.notEquals=" + UPDATED_COLOR_VISION);
    }

    @Test
    @Transactional
    public void getAllProfilesByColorVisionIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where colorVision in DEFAULT_COLOR_VISION or UPDATED_COLOR_VISION
        defaultProfileShouldBeFound("colorVision.in=" + DEFAULT_COLOR_VISION + "," + UPDATED_COLOR_VISION);

        // Get all the profileList where colorVision equals to UPDATED_COLOR_VISION
        defaultProfileShouldNotBeFound("colorVision.in=" + UPDATED_COLOR_VISION);
    }

    @Test
    @Transactional
    public void getAllProfilesByColorVisionIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where colorVision is not null
        defaultProfileShouldBeFound("colorVision.specified=true");

        // Get all the profileList where colorVision is null
        defaultProfileShouldNotBeFound("colorVision.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesByContrastSensitivityIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where contrastSensitivity equals to DEFAULT_CONTRAST_SENSITIVITY
        defaultProfileShouldBeFound("contrastSensitivity.equals=" + DEFAULT_CONTRAST_SENSITIVITY);

        // Get all the profileList where contrastSensitivity equals to UPDATED_CONTRAST_SENSITIVITY
        defaultProfileShouldNotBeFound("contrastSensitivity.equals=" + UPDATED_CONTRAST_SENSITIVITY);
    }

    @Test
    @Transactional
    public void getAllProfilesByContrastSensitivityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where contrastSensitivity not equals to DEFAULT_CONTRAST_SENSITIVITY
        defaultProfileShouldNotBeFound("contrastSensitivity.notEquals=" + DEFAULT_CONTRAST_SENSITIVITY);

        // Get all the profileList where contrastSensitivity not equals to UPDATED_CONTRAST_SENSITIVITY
        defaultProfileShouldBeFound("contrastSensitivity.notEquals=" + UPDATED_CONTRAST_SENSITIVITY);
    }

    @Test
    @Transactional
    public void getAllProfilesByContrastSensitivityIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where contrastSensitivity in DEFAULT_CONTRAST_SENSITIVITY or UPDATED_CONTRAST_SENSITIVITY
        defaultProfileShouldBeFound("contrastSensitivity.in=" + DEFAULT_CONTRAST_SENSITIVITY + "," + UPDATED_CONTRAST_SENSITIVITY);

        // Get all the profileList where contrastSensitivity equals to UPDATED_CONTRAST_SENSITIVITY
        defaultProfileShouldNotBeFound("contrastSensitivity.in=" + UPDATED_CONTRAST_SENSITIVITY);
    }

    @Test
    @Transactional
    public void getAllProfilesByContrastSensitivityIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where contrastSensitivity is not null
        defaultProfileShouldBeFound("contrastSensitivity.specified=true");

        // Get all the profileList where contrastSensitivity is null
        defaultProfileShouldNotBeFound("contrastSensitivity.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesByFildOfVisionIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where fildOfVision equals to DEFAULT_FILD_OF_VISION
        defaultProfileShouldBeFound("fildOfVision.equals=" + DEFAULT_FILD_OF_VISION);

        // Get all the profileList where fildOfVision equals to UPDATED_FILD_OF_VISION
        defaultProfileShouldNotBeFound("fildOfVision.equals=" + UPDATED_FILD_OF_VISION);
    }

    @Test
    @Transactional
    public void getAllProfilesByFildOfVisionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where fildOfVision not equals to DEFAULT_FILD_OF_VISION
        defaultProfileShouldNotBeFound("fildOfVision.notEquals=" + DEFAULT_FILD_OF_VISION);

        // Get all the profileList where fildOfVision not equals to UPDATED_FILD_OF_VISION
        defaultProfileShouldBeFound("fildOfVision.notEquals=" + UPDATED_FILD_OF_VISION);
    }

    @Test
    @Transactional
    public void getAllProfilesByFildOfVisionIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where fildOfVision in DEFAULT_FILD_OF_VISION or UPDATED_FILD_OF_VISION
        defaultProfileShouldBeFound("fildOfVision.in=" + DEFAULT_FILD_OF_VISION + "," + UPDATED_FILD_OF_VISION);

        // Get all the profileList where fildOfVision equals to UPDATED_FILD_OF_VISION
        defaultProfileShouldNotBeFound("fildOfVision.in=" + UPDATED_FILD_OF_VISION);
    }

    @Test
    @Transactional
    public void getAllProfilesByFildOfVisionIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where fildOfVision is not null
        defaultProfileShouldBeFound("fildOfVision.specified=true");

        // Get all the profileList where fildOfVision is null
        defaultProfileShouldNotBeFound("fildOfVision.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesByLightSensitivityIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where lightSensitivity equals to DEFAULT_LIGHT_SENSITIVITY
        defaultProfileShouldBeFound("lightSensitivity.equals=" + DEFAULT_LIGHT_SENSITIVITY);

        // Get all the profileList where lightSensitivity equals to UPDATED_LIGHT_SENSITIVITY
        defaultProfileShouldNotBeFound("lightSensitivity.equals=" + UPDATED_LIGHT_SENSITIVITY);
    }

    @Test
    @Transactional
    public void getAllProfilesByLightSensitivityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where lightSensitivity not equals to DEFAULT_LIGHT_SENSITIVITY
        defaultProfileShouldNotBeFound("lightSensitivity.notEquals=" + DEFAULT_LIGHT_SENSITIVITY);

        // Get all the profileList where lightSensitivity not equals to UPDATED_LIGHT_SENSITIVITY
        defaultProfileShouldBeFound("lightSensitivity.notEquals=" + UPDATED_LIGHT_SENSITIVITY);
    }

    @Test
    @Transactional
    public void getAllProfilesByLightSensitivityIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where lightSensitivity in DEFAULT_LIGHT_SENSITIVITY or UPDATED_LIGHT_SENSITIVITY
        defaultProfileShouldBeFound("lightSensitivity.in=" + DEFAULT_LIGHT_SENSITIVITY + "," + UPDATED_LIGHT_SENSITIVITY);

        // Get all the profileList where lightSensitivity equals to UPDATED_LIGHT_SENSITIVITY
        defaultProfileShouldNotBeFound("lightSensitivity.in=" + UPDATED_LIGHT_SENSITIVITY);
    }

    @Test
    @Transactional
    public void getAllProfilesByLightSensitivityIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where lightSensitivity is not null
        defaultProfileShouldBeFound("lightSensitivity.specified=true");

        // Get all the profileList where lightSensitivity is null
        defaultProfileShouldNotBeFound("lightSensitivity.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesByVisualAcuityIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where visualAcuity equals to DEFAULT_VISUAL_ACUITY
        defaultProfileShouldBeFound("visualAcuity.equals=" + DEFAULT_VISUAL_ACUITY);

        // Get all the profileList where visualAcuity equals to UPDATED_VISUAL_ACUITY
        defaultProfileShouldNotBeFound("visualAcuity.equals=" + UPDATED_VISUAL_ACUITY);
    }

    @Test
    @Transactional
    public void getAllProfilesByVisualAcuityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where visualAcuity not equals to DEFAULT_VISUAL_ACUITY
        defaultProfileShouldNotBeFound("visualAcuity.notEquals=" + DEFAULT_VISUAL_ACUITY);

        // Get all the profileList where visualAcuity not equals to UPDATED_VISUAL_ACUITY
        defaultProfileShouldBeFound("visualAcuity.notEquals=" + UPDATED_VISUAL_ACUITY);
    }

    @Test
    @Transactional
    public void getAllProfilesByVisualAcuityIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where visualAcuity in DEFAULT_VISUAL_ACUITY or UPDATED_VISUAL_ACUITY
        defaultProfileShouldBeFound("visualAcuity.in=" + DEFAULT_VISUAL_ACUITY + "," + UPDATED_VISUAL_ACUITY);

        // Get all the profileList where visualAcuity equals to UPDATED_VISUAL_ACUITY
        defaultProfileShouldNotBeFound("visualAcuity.in=" + UPDATED_VISUAL_ACUITY);
    }

    @Test
    @Transactional
    public void getAllProfilesByVisualAcuityIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where visualAcuity is not null
        defaultProfileShouldBeFound("visualAcuity.specified=true");

        // Get all the profileList where visualAcuity is null
        defaultProfileShouldNotBeFound("visualAcuity.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesByEducationIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where education equals to DEFAULT_EDUCATION
        defaultProfileShouldBeFound("education.equals=" + DEFAULT_EDUCATION);

        // Get all the profileList where education equals to UPDATED_EDUCATION
        defaultProfileShouldNotBeFound("education.equals=" + UPDATED_EDUCATION);
    }

    @Test
    @Transactional
    public void getAllProfilesByEducationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where education not equals to DEFAULT_EDUCATION
        defaultProfileShouldNotBeFound("education.notEquals=" + DEFAULT_EDUCATION);

        // Get all the profileList where education not equals to UPDATED_EDUCATION
        defaultProfileShouldBeFound("education.notEquals=" + UPDATED_EDUCATION);
    }

    @Test
    @Transactional
    public void getAllProfilesByEducationIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where education in DEFAULT_EDUCATION or UPDATED_EDUCATION
        defaultProfileShouldBeFound("education.in=" + DEFAULT_EDUCATION + "," + UPDATED_EDUCATION);

        // Get all the profileList where education equals to UPDATED_EDUCATION
        defaultProfileShouldNotBeFound("education.in=" + UPDATED_EDUCATION);
    }

    @Test
    @Transactional
    public void getAllProfilesByEducationIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where education is not null
        defaultProfileShouldBeFound("education.specified=true");

        // Get all the profileList where education is null
        defaultProfileShouldNotBeFound("education.specified=false");
    }
                @Test
    @Transactional
    public void getAllProfilesByEducationContainsSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where education contains DEFAULT_EDUCATION
        defaultProfileShouldBeFound("education.contains=" + DEFAULT_EDUCATION);

        // Get all the profileList where education contains UPDATED_EDUCATION
        defaultProfileShouldNotBeFound("education.contains=" + UPDATED_EDUCATION);
    }

    @Test
    @Transactional
    public void getAllProfilesByEducationNotContainsSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where education does not contain DEFAULT_EDUCATION
        defaultProfileShouldNotBeFound("education.doesNotContain=" + DEFAULT_EDUCATION);

        // Get all the profileList where education does not contain UPDATED_EDUCATION
        defaultProfileShouldBeFound("education.doesNotContain=" + UPDATED_EDUCATION);
    }


    @Test
    @Transactional
    public void getAllProfilesByExperienceLevelIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where experienceLevel equals to DEFAULT_EXPERIENCE_LEVEL
        defaultProfileShouldBeFound("experienceLevel.equals=" + DEFAULT_EXPERIENCE_LEVEL);

        // Get all the profileList where experienceLevel equals to UPDATED_EXPERIENCE_LEVEL
        defaultProfileShouldNotBeFound("experienceLevel.equals=" + UPDATED_EXPERIENCE_LEVEL);
    }

    @Test
    @Transactional
    public void getAllProfilesByExperienceLevelIsNotEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where experienceLevel not equals to DEFAULT_EXPERIENCE_LEVEL
        defaultProfileShouldNotBeFound("experienceLevel.notEquals=" + DEFAULT_EXPERIENCE_LEVEL);

        // Get all the profileList where experienceLevel not equals to UPDATED_EXPERIENCE_LEVEL
        defaultProfileShouldBeFound("experienceLevel.notEquals=" + UPDATED_EXPERIENCE_LEVEL);
    }

    @Test
    @Transactional
    public void getAllProfilesByExperienceLevelIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where experienceLevel in DEFAULT_EXPERIENCE_LEVEL or UPDATED_EXPERIENCE_LEVEL
        defaultProfileShouldBeFound("experienceLevel.in=" + DEFAULT_EXPERIENCE_LEVEL + "," + UPDATED_EXPERIENCE_LEVEL);

        // Get all the profileList where experienceLevel equals to UPDATED_EXPERIENCE_LEVEL
        defaultProfileShouldNotBeFound("experienceLevel.in=" + UPDATED_EXPERIENCE_LEVEL);
    }

    @Test
    @Transactional
    public void getAllProfilesByExperienceLevelIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where experienceLevel is not null
        defaultProfileShouldBeFound("experienceLevel.specified=true");

        // Get all the profileList where experienceLevel is null
        defaultProfileShouldNotBeFound("experienceLevel.specified=false");
    }
                @Test
    @Transactional
    public void getAllProfilesByExperienceLevelContainsSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where experienceLevel contains DEFAULT_EXPERIENCE_LEVEL
        defaultProfileShouldBeFound("experienceLevel.contains=" + DEFAULT_EXPERIENCE_LEVEL);

        // Get all the profileList where experienceLevel contains UPDATED_EXPERIENCE_LEVEL
        defaultProfileShouldNotBeFound("experienceLevel.contains=" + UPDATED_EXPERIENCE_LEVEL);
    }

    @Test
    @Transactional
    public void getAllProfilesByExperienceLevelNotContainsSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where experienceLevel does not contain DEFAULT_EXPERIENCE_LEVEL
        defaultProfileShouldNotBeFound("experienceLevel.doesNotContain=" + DEFAULT_EXPERIENCE_LEVEL);

        // Get all the profileList where experienceLevel does not contain UPDATED_EXPERIENCE_LEVEL
        defaultProfileShouldBeFound("experienceLevel.doesNotContain=" + UPDATED_EXPERIENCE_LEVEL);
    }


    @Test
    @Transactional
    public void getAllProfilesByGenderIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where gender equals to DEFAULT_GENDER
        defaultProfileShouldBeFound("gender.equals=" + DEFAULT_GENDER);

        // Get all the profileList where gender equals to UPDATED_GENDER
        defaultProfileShouldNotBeFound("gender.equals=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    public void getAllProfilesByGenderIsNotEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where gender not equals to DEFAULT_GENDER
        defaultProfileShouldNotBeFound("gender.notEquals=" + DEFAULT_GENDER);

        // Get all the profileList where gender not equals to UPDATED_GENDER
        defaultProfileShouldBeFound("gender.notEquals=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    public void getAllProfilesByGenderIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where gender in DEFAULT_GENDER or UPDATED_GENDER
        defaultProfileShouldBeFound("gender.in=" + DEFAULT_GENDER + "," + UPDATED_GENDER);

        // Get all the profileList where gender equals to UPDATED_GENDER
        defaultProfileShouldNotBeFound("gender.in=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    public void getAllProfilesByGenderIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where gender is not null
        defaultProfileShouldBeFound("gender.specified=true");

        // Get all the profileList where gender is null
        defaultProfileShouldNotBeFound("gender.specified=false");
    }
                @Test
    @Transactional
    public void getAllProfilesByGenderContainsSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where gender contains DEFAULT_GENDER
        defaultProfileShouldBeFound("gender.contains=" + DEFAULT_GENDER);

        // Get all the profileList where gender contains UPDATED_GENDER
        defaultProfileShouldNotBeFound("gender.contains=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    public void getAllProfilesByGenderNotContainsSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where gender does not contain DEFAULT_GENDER
        defaultProfileShouldNotBeFound("gender.doesNotContain=" + DEFAULT_GENDER);

        // Get all the profileList where gender does not contain UPDATED_GENDER
        defaultProfileShouldBeFound("gender.doesNotContain=" + UPDATED_GENDER);
    }


    @Test
    @Transactional
    public void getAllProfilesByLanguageIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where language equals to DEFAULT_LANGUAGE
        defaultProfileShouldBeFound("language.equals=" + DEFAULT_LANGUAGE);

        // Get all the profileList where language equals to UPDATED_LANGUAGE
        defaultProfileShouldNotBeFound("language.equals=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    public void getAllProfilesByLanguageIsNotEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where language not equals to DEFAULT_LANGUAGE
        defaultProfileShouldNotBeFound("language.notEquals=" + DEFAULT_LANGUAGE);

        // Get all the profileList where language not equals to UPDATED_LANGUAGE
        defaultProfileShouldBeFound("language.notEquals=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    public void getAllProfilesByLanguageIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where language in DEFAULT_LANGUAGE or UPDATED_LANGUAGE
        defaultProfileShouldBeFound("language.in=" + DEFAULT_LANGUAGE + "," + UPDATED_LANGUAGE);

        // Get all the profileList where language equals to UPDATED_LANGUAGE
        defaultProfileShouldNotBeFound("language.in=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    public void getAllProfilesByLanguageIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where language is not null
        defaultProfileShouldBeFound("language.specified=true");

        // Get all the profileList where language is null
        defaultProfileShouldNotBeFound("language.specified=false");
    }
                @Test
    @Transactional
    public void getAllProfilesByLanguageContainsSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where language contains DEFAULT_LANGUAGE
        defaultProfileShouldBeFound("language.contains=" + DEFAULT_LANGUAGE);

        // Get all the profileList where language contains UPDATED_LANGUAGE
        defaultProfileShouldNotBeFound("language.contains=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    public void getAllProfilesByLanguageNotContainsSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where language does not contain DEFAULT_LANGUAGE
        defaultProfileShouldNotBeFound("language.doesNotContain=" + DEFAULT_LANGUAGE);

        // Get all the profileList where language does not contain UPDATED_LANGUAGE
        defaultProfileShouldBeFound("language.doesNotContain=" + UPDATED_LANGUAGE);
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
            .andExpect(jsonPath("$.[*].ultimaModificacao").value(hasItem(sameInstant(DEFAULT_ULTIMA_MODIFICACAO))))
            .andExpect(jsonPath("$.[*].numModificacao").value(hasItem(DEFAULT_NUM_MODIFICACAO)))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)))
            .andExpect(jsonPath("$.[*].auditoryDisabilities").value(hasItem(DEFAULT_AUDITORY_DISABILITIES.booleanValue())))
            .andExpect(jsonPath("$.[*].blindness").value(hasItem(DEFAULT_BLINDNESS.booleanValue())))
            .andExpect(jsonPath("$.[*].colorVision").value(hasItem(DEFAULT_COLOR_VISION.booleanValue())))
            .andExpect(jsonPath("$.[*].contrastSensitivity").value(hasItem(DEFAULT_CONTRAST_SENSITIVITY.booleanValue())))
            .andExpect(jsonPath("$.[*].fildOfVision").value(hasItem(DEFAULT_FILD_OF_VISION.booleanValue())))
            .andExpect(jsonPath("$.[*].lightSensitivity").value(hasItem(DEFAULT_LIGHT_SENSITIVITY.booleanValue())))
            .andExpect(jsonPath("$.[*].visualAcuity").value(hasItem(DEFAULT_VISUAL_ACUITY.booleanValue())))
            .andExpect(jsonPath("$.[*].education").value(hasItem(DEFAULT_EDUCATION)))
            .andExpect(jsonPath("$.[*].experienceLevel").value(hasItem(DEFAULT_EXPERIENCE_LEVEL)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER)))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE)));

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
            .ultimaModificacao(UPDATED_ULTIMA_MODIFICACAO)
            .numModificacao(UPDATED_NUM_MODIFICACAO)
            .age(UPDATED_AGE)
            .auditoryDisabilities(UPDATED_AUDITORY_DISABILITIES)
            .blindness(UPDATED_BLINDNESS)
            .colorVision(UPDATED_COLOR_VISION)
            .contrastSensitivity(UPDATED_CONTRAST_SENSITIVITY)
            .fildOfVision(UPDATED_FILD_OF_VISION)
            .lightSensitivity(UPDATED_LIGHT_SENSITIVITY)
            .visualAcuity(UPDATED_VISUAL_ACUITY)
            .education(UPDATED_EDUCATION)
            .experienceLevel(UPDATED_EXPERIENCE_LEVEL)
            .gender(UPDATED_GENDER)
            .language(UPDATED_LANGUAGE);
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
        assertThat(testProfile.getNumModificacao()).isEqualTo(UPDATED_NUM_MODIFICACAO);
        assertThat(testProfile.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testProfile.isAuditoryDisabilities()).isEqualTo(UPDATED_AUDITORY_DISABILITIES);
        assertThat(testProfile.isBlindness()).isEqualTo(UPDATED_BLINDNESS);
        assertThat(testProfile.isColorVision()).isEqualTo(UPDATED_COLOR_VISION);
        assertThat(testProfile.isContrastSensitivity()).isEqualTo(UPDATED_CONTRAST_SENSITIVITY);
        assertThat(testProfile.isFildOfVision()).isEqualTo(UPDATED_FILD_OF_VISION);
        assertThat(testProfile.isLightSensitivity()).isEqualTo(UPDATED_LIGHT_SENSITIVITY);
        assertThat(testProfile.isVisualAcuity()).isEqualTo(UPDATED_VISUAL_ACUITY);
        assertThat(testProfile.getEducation()).isEqualTo(UPDATED_EDUCATION);
        assertThat(testProfile.getExperienceLevel()).isEqualTo(UPDATED_EXPERIENCE_LEVEL);
        assertThat(testProfile.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testProfile.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
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
