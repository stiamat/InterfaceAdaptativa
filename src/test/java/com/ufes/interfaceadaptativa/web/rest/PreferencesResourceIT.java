package com.ufes.interfaceadaptativa.web.rest;

import com.ufes.interfaceadaptativa.InterfaceAdaptativaApp;
import com.ufes.interfaceadaptativa.domain.Preferences;
import com.ufes.interfaceadaptativa.domain.User;
import com.ufes.interfaceadaptativa.repository.PreferencesRepository;
import com.ufes.interfaceadaptativa.service.PreferencesService;
import com.ufes.interfaceadaptativa.service.dto.PreferencesDTO;
import com.ufes.interfaceadaptativa.service.mapper.PreferencesMapper;
import com.ufes.interfaceadaptativa.service.dto.PreferencesCriteria;
import com.ufes.interfaceadaptativa.service.PreferencesQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ufes.interfaceadaptativa.domain.enumeration.StatusPreferences;
/**
 * Integration tests for the {@link PreferencesResource} REST controller.
 */
@SpringBootTest(classes = InterfaceAdaptativaApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class PreferencesResourceIT {

    private static final StatusPreferences DEFAULT_DARK_MODE = StatusPreferences.TRUE;
    private static final StatusPreferences UPDATED_DARK_MODE = StatusPreferences.FALSE;

    @Autowired
    private PreferencesRepository preferencesRepository;

    @Autowired
    private PreferencesMapper preferencesMapper;

    @Autowired
    private PreferencesService preferencesService;

    @Autowired
    private PreferencesQueryService preferencesQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPreferencesMockMvc;

    private Preferences preferences;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Preferences createEntity(EntityManager em) {
        Preferences preferences = new Preferences()
            .darkMode(DEFAULT_DARK_MODE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        preferences.setUser(user);
        return preferences;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Preferences createUpdatedEntity(EntityManager em) {
        Preferences preferences = new Preferences()
            .darkMode(UPDATED_DARK_MODE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        preferences.setUser(user);
        return preferences;
    }

    @BeforeEach
    public void initTest() {
        preferences = createEntity(em);
    }

    @Test
    @Transactional
    public void createPreferences() throws Exception {
        int databaseSizeBeforeCreate = preferencesRepository.findAll().size();
        // Create the Preferences
        PreferencesDTO preferencesDTO = preferencesMapper.toDto(preferences);
        restPreferencesMockMvc.perform(post("/api/preferences")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(preferencesDTO)))
            .andExpect(status().isCreated());

        // Validate the Preferences in the database
        List<Preferences> preferencesList = preferencesRepository.findAll();
        assertThat(preferencesList).hasSize(databaseSizeBeforeCreate + 1);
        Preferences testPreferences = preferencesList.get(preferencesList.size() - 1);
        assertThat(testPreferences.getDarkMode()).isEqualTo(DEFAULT_DARK_MODE);

        // Validate the id for MapsId, the ids must be same
        assertThat(testPreferences.getId()).isEqualTo(testPreferences.getUser().getId());
    }

    @Test
    @Transactional
    public void createPreferencesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = preferencesRepository.findAll().size();

        // Create the Preferences with an existing ID
        preferences.setId(1L);
        PreferencesDTO preferencesDTO = preferencesMapper.toDto(preferences);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPreferencesMockMvc.perform(post("/api/preferences")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(preferencesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Preferences in the database
        List<Preferences> preferencesList = preferencesRepository.findAll();
        assertThat(preferencesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void updatePreferencesMapsIdAssociationWithNewId() throws Exception {
        // Initialize the database
        preferencesRepository.saveAndFlush(preferences);
        int databaseSizeBeforeCreate = preferencesRepository.findAll().size();

        // Add a new parent entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();

        // Load the preferences
        Preferences updatedPreferences = preferencesRepository.findById(preferences.getId()).get();
        // Disconnect from session so that the updates on updatedPreferences are not directly saved in db
        em.detach(updatedPreferences);

        // Update the User with new association value
        updatedPreferences.setUser(user);
        PreferencesDTO updatedPreferencesDTO = preferencesMapper.toDto(updatedPreferences);

        // Update the entity
        restPreferencesMockMvc.perform(put("/api/preferences")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPreferencesDTO)))
            .andExpect(status().isOk());

        // Validate the Preferences in the database
        List<Preferences> preferencesList = preferencesRepository.findAll();
        assertThat(preferencesList).hasSize(databaseSizeBeforeCreate);
        Preferences testPreferences = preferencesList.get(preferencesList.size() - 1);

        // Validate the id for MapsId, the ids must be same
        // Uncomment the following line for assertion. However, please note that there is a known issue and uncommenting will fail the test.
        // Please look at https://github.com/jhipster/generator-jhipster/issues/9100. You can modify this test as necessary.
        // assertThat(testPreferences.getId()).isEqualTo(testPreferences.getUser().getId());
    }

    @Test
    @Transactional
    public void getAllPreferences() throws Exception {
        // Initialize the database
        preferencesRepository.saveAndFlush(preferences);

        // Get all the preferencesList
        restPreferencesMockMvc.perform(get("/api/preferences?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(preferences.getId().intValue())))
            .andExpect(jsonPath("$.[*].darkMode").value(hasItem(DEFAULT_DARK_MODE.toString())));
    }
    
    @Test
    @Transactional
    public void getPreferences() throws Exception {
        // Initialize the database
        preferencesRepository.saveAndFlush(preferences);

        // Get the preferences
        restPreferencesMockMvc.perform(get("/api/preferences/{id}", preferences.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(preferences.getId().intValue()))
            .andExpect(jsonPath("$.darkMode").value(DEFAULT_DARK_MODE.toString()));
    }


    @Test
    @Transactional
    public void getPreferencesByIdFiltering() throws Exception {
        // Initialize the database
        preferencesRepository.saveAndFlush(preferences);

        Long id = preferences.getId();

        defaultPreferencesShouldBeFound("id.equals=" + id);
        defaultPreferencesShouldNotBeFound("id.notEquals=" + id);

        defaultPreferencesShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPreferencesShouldNotBeFound("id.greaterThan=" + id);

        defaultPreferencesShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPreferencesShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllPreferencesByDarkModeIsEqualToSomething() throws Exception {
        // Initialize the database
        preferencesRepository.saveAndFlush(preferences);

        // Get all the preferencesList where darkMode equals to DEFAULT_DARK_MODE
        defaultPreferencesShouldBeFound("darkMode.equals=" + DEFAULT_DARK_MODE);

        // Get all the preferencesList where darkMode equals to UPDATED_DARK_MODE
        defaultPreferencesShouldNotBeFound("darkMode.equals=" + UPDATED_DARK_MODE);
    }

    @Test
    @Transactional
    public void getAllPreferencesByDarkModeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        preferencesRepository.saveAndFlush(preferences);

        // Get all the preferencesList where darkMode not equals to DEFAULT_DARK_MODE
        defaultPreferencesShouldNotBeFound("darkMode.notEquals=" + DEFAULT_DARK_MODE);

        // Get all the preferencesList where darkMode not equals to UPDATED_DARK_MODE
        defaultPreferencesShouldBeFound("darkMode.notEquals=" + UPDATED_DARK_MODE);
    }

    @Test
    @Transactional
    public void getAllPreferencesByDarkModeIsInShouldWork() throws Exception {
        // Initialize the database
        preferencesRepository.saveAndFlush(preferences);

        // Get all the preferencesList where darkMode in DEFAULT_DARK_MODE or UPDATED_DARK_MODE
        defaultPreferencesShouldBeFound("darkMode.in=" + DEFAULT_DARK_MODE + "," + UPDATED_DARK_MODE);

        // Get all the preferencesList where darkMode equals to UPDATED_DARK_MODE
        defaultPreferencesShouldNotBeFound("darkMode.in=" + UPDATED_DARK_MODE);
    }

    @Test
    @Transactional
    public void getAllPreferencesByDarkModeIsNullOrNotNull() throws Exception {
        // Initialize the database
        preferencesRepository.saveAndFlush(preferences);

        // Get all the preferencesList where darkMode is not null
        defaultPreferencesShouldBeFound("darkMode.specified=true");

        // Get all the preferencesList where darkMode is null
        defaultPreferencesShouldNotBeFound("darkMode.specified=false");
    }

    @Test
    @Transactional
    public void getAllPreferencesByUserIsEqualToSomething() throws Exception {
        // Get already existing entity
        User user = preferences.getUser();
        preferencesRepository.saveAndFlush(preferences);
        Long userId = user.getId();

        // Get all the preferencesList where user equals to userId
        defaultPreferencesShouldBeFound("userId.equals=" + userId);

        // Get all the preferencesList where user equals to userId + 1
        defaultPreferencesShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPreferencesShouldBeFound(String filter) throws Exception {
        restPreferencesMockMvc.perform(get("/api/preferences?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(preferences.getId().intValue())))
            .andExpect(jsonPath("$.[*].darkMode").value(hasItem(DEFAULT_DARK_MODE.toString())));

        // Check, that the count call also returns 1
        restPreferencesMockMvc.perform(get("/api/preferences/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPreferencesShouldNotBeFound(String filter) throws Exception {
        restPreferencesMockMvc.perform(get("/api/preferences?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPreferencesMockMvc.perform(get("/api/preferences/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingPreferences() throws Exception {
        // Get the preferences
        restPreferencesMockMvc.perform(get("/api/preferences/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePreferences() throws Exception {
        // Initialize the database
        preferencesRepository.saveAndFlush(preferences);

        int databaseSizeBeforeUpdate = preferencesRepository.findAll().size();

        // Update the preferences
        Preferences updatedPreferences = preferencesRepository.findById(preferences.getId()).get();
        // Disconnect from session so that the updates on updatedPreferences are not directly saved in db
        em.detach(updatedPreferences);
        updatedPreferences
            .darkMode(UPDATED_DARK_MODE);
        PreferencesDTO preferencesDTO = preferencesMapper.toDto(updatedPreferences);

        restPreferencesMockMvc.perform(put("/api/preferences")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(preferencesDTO)))
            .andExpect(status().isOk());

        // Validate the Preferences in the database
        List<Preferences> preferencesList = preferencesRepository.findAll();
        assertThat(preferencesList).hasSize(databaseSizeBeforeUpdate);
        Preferences testPreferences = preferencesList.get(preferencesList.size() - 1);
        assertThat(testPreferences.getDarkMode()).isEqualTo(UPDATED_DARK_MODE);
    }

    @Test
    @Transactional
    public void updateNonExistingPreferences() throws Exception {
        int databaseSizeBeforeUpdate = preferencesRepository.findAll().size();

        // Create the Preferences
        PreferencesDTO preferencesDTO = preferencesMapper.toDto(preferences);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPreferencesMockMvc.perform(put("/api/preferences")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(preferencesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Preferences in the database
        List<Preferences> preferencesList = preferencesRepository.findAll();
        assertThat(preferencesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePreferences() throws Exception {
        // Initialize the database
        preferencesRepository.saveAndFlush(preferences);

        int databaseSizeBeforeDelete = preferencesRepository.findAll().size();

        // Delete the preferences
        restPreferencesMockMvc.perform(delete("/api/preferences/{id}", preferences.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Preferences> preferencesList = preferencesRepository.findAll();
        assertThat(preferencesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
