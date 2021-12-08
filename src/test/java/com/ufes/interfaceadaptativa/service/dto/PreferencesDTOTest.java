package com.ufes.interfaceadaptativa.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.ufes.interfaceadaptativa.web.rest.TestUtil;

public class PreferencesDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PreferencesDTO.class);
        PreferencesDTO preferencesDTO1 = new PreferencesDTO();
        preferencesDTO1.setId(1L);
        PreferencesDTO preferencesDTO2 = new PreferencesDTO();
        assertThat(preferencesDTO1).isNotEqualTo(preferencesDTO2);
        preferencesDTO2.setId(preferencesDTO1.getId());
        assertThat(preferencesDTO1).isEqualTo(preferencesDTO2);
        preferencesDTO2.setId(2L);
        assertThat(preferencesDTO1).isNotEqualTo(preferencesDTO2);
        preferencesDTO1.setId(null);
        assertThat(preferencesDTO1).isNotEqualTo(preferencesDTO2);
    }
}
