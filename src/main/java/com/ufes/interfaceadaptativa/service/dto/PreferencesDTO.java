package com.ufes.interfaceadaptativa.service.dto;

import java.io.Serializable;
import com.ufes.interfaceadaptativa.domain.enumeration.StatusPreferences;
import com.ufes.interfaceadaptativa.domain.enumeration.ExperienceLevelMode;
import com.ufes.interfaceadaptativa.domain.enumeration.FontMode;

/**
 * A DTO for the {@link com.ufes.interfaceadaptativa.domain.Preferences} entity.
 */
public class PreferencesDTO implements Serializable {
    
    private Long id;

    private StatusPreferences darkMode;

    private ExperienceLevelMode experienceLevelMode;

    private FontMode fontMode;

    private Boolean contrastMode;

    private Boolean colorVisionMode;


    private Long userId;

    private String userLogin;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StatusPreferences getDarkMode() {
        return darkMode;
    }

    public void setDarkMode(StatusPreferences darkMode) {
        this.darkMode = darkMode;
    }

    public ExperienceLevelMode getExperienceLevelMode() {
        return experienceLevelMode;
    }

    public void setExperienceLevelMode(ExperienceLevelMode experienceLevelMode) {
        this.experienceLevelMode = experienceLevelMode;
    }

    public FontMode getFontMode() {
        return fontMode;
    }

    public void setFontMode(FontMode fontMode) {
        this.fontMode = fontMode;
    }

    public Boolean isContrastMode() {
        return contrastMode;
    }

    public void setContrastMode(Boolean contrastMode) {
        this.contrastMode = contrastMode;
    }

    public Boolean isColorVisionMode() {
        return colorVisionMode;
    }

    public void setColorVisionMode(Boolean colorVisionMode) {
        this.colorVisionMode = colorVisionMode;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PreferencesDTO)) {
            return false;
        }

        return id != null && id.equals(((PreferencesDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PreferencesDTO{" +
            "id=" + getId() +
            ", darkMode='" + getDarkMode() + "'" +
            ", experienceLevelMode='" + getExperienceLevelMode() + "'" +
            ", fontMode='" + getFontMode() + "'" +
            ", contrastMode='" + isContrastMode() + "'" +
            ", colorVisionMode='" + isColorVisionMode() + "'" +
            ", userId=" + getUserId() +
            ", userLogin='" + getUserLogin() + "'" +
            "}";
    }
}
