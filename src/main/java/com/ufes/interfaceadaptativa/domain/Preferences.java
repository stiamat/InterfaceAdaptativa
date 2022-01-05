package com.ufes.interfaceadaptativa.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

import com.ufes.interfaceadaptativa.domain.enumeration.StatusPreferences;

import com.ufes.interfaceadaptativa.domain.enumeration.ExperienceLevelMode;

import com.ufes.interfaceadaptativa.domain.enumeration.FontMode;

/**
 * A Preferences.
 */
@Entity
@Table(name = "preferences")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Preferences implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "dark_mode")
    private StatusPreferences darkMode;

    @Enumerated(EnumType.STRING)
    @Column(name = "experience_level_mode")
    private ExperienceLevelMode experienceLevelMode;

    @Enumerated(EnumType.STRING)
    @Column(name = "font_mode")
    private FontMode fontMode;

    @Column(name = "contrast_mode")
    private Boolean contrastMode;

    @Column(name = "color_vision_mode")
    private Boolean colorVisionMode;

    @OneToOne

    @MapsId
    @JoinColumn(name = "id")
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StatusPreferences getDarkMode() {
        return darkMode;
    }

    public Preferences darkMode(StatusPreferences darkMode) {
        this.darkMode = darkMode;
        return this;
    }

    public void setDarkMode(StatusPreferences darkMode) {
        this.darkMode = darkMode;
    }

    public ExperienceLevelMode getExperienceLevelMode() {
        return experienceLevelMode;
    }

    public Preferences experienceLevelMode(ExperienceLevelMode experienceLevelMode) {
        this.experienceLevelMode = experienceLevelMode;
        return this;
    }

    public void setExperienceLevelMode(ExperienceLevelMode experienceLevelMode) {
        this.experienceLevelMode = experienceLevelMode;
    }

    public FontMode getFontMode() {
        return fontMode;
    }

    public Preferences fontMode(FontMode fontMode) {
        this.fontMode = fontMode;
        return this;
    }

    public void setFontMode(FontMode fontMode) {
        this.fontMode = fontMode;
    }

    public Boolean isContrastMode() {
        return contrastMode;
    }

    public Preferences contrastMode(Boolean contrastMode) {
        this.contrastMode = contrastMode;
        return this;
    }

    public void setContrastMode(Boolean contrastMode) {
        this.contrastMode = contrastMode;
    }

    public Boolean isColorVisionMode() {
        return colorVisionMode;
    }

    public Preferences colorVisionMode(Boolean colorVisionMode) {
        this.colorVisionMode = colorVisionMode;
        return this;
    }

    public void setColorVisionMode(Boolean colorVisionMode) {
        this.colorVisionMode = colorVisionMode;
    }

    public User getUser() {
        return user;
    }

    public Preferences user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Preferences)) {
            return false;
        }
        return id != null && id.equals(((Preferences) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Preferences{" +
            "id=" + getId() +
            ", darkMode='" + getDarkMode() + "'" +
            ", experienceLevelMode='" + getExperienceLevelMode() + "'" +
            ", fontMode='" + getFontMode() + "'" +
            ", contrastMode='" + isContrastMode() + "'" +
            ", colorVisionMode='" + isColorVisionMode() + "'" +
            "}";
    }
}
