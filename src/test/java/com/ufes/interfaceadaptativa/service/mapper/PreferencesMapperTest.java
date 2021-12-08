package com.ufes.interfaceadaptativa.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class PreferencesMapperTest {

    private PreferencesMapper preferencesMapper;

    @BeforeEach
    public void setUp() {
        preferencesMapper = new PreferencesMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(preferencesMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(preferencesMapper.fromId(null)).isNull();
    }
}
