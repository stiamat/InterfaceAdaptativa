package com.ufes.interfaceadaptativa.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.ufes.interfaceadaptativa.domain.enumeration.StatusPreferences;
import com.ufes.interfaceadaptativa.domain.enumeration.ExperienceLevelMode;
import com.ufes.interfaceadaptativa.domain.enumeration.FontMode;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.ufes.interfaceadaptativa.domain.Preferences} entity. This class is used
 * in {@link com.ufes.interfaceadaptativa.web.rest.PreferencesResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /preferences?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PreferencesCriteria implements Serializable, Criteria {
    /**
     * Class for filtering StatusPreferences
     */
    public static class StatusPreferencesFilter extends Filter<StatusPreferences> {

        public StatusPreferencesFilter() {
        }

        public StatusPreferencesFilter(StatusPreferencesFilter filter) {
            super(filter);
        }

        @Override
        public StatusPreferencesFilter copy() {
            return new StatusPreferencesFilter(this);
        }

    }
    /**
     * Class for filtering ExperienceLevelMode
     */
    public static class ExperienceLevelModeFilter extends Filter<ExperienceLevelMode> {

        public ExperienceLevelModeFilter() {
        }

        public ExperienceLevelModeFilter(ExperienceLevelModeFilter filter) {
            super(filter);
        }

        @Override
        public ExperienceLevelModeFilter copy() {
            return new ExperienceLevelModeFilter(this);
        }

    }
    /**
     * Class for filtering FontMode
     */
    public static class FontModeFilter extends Filter<FontMode> {

        public FontModeFilter() {
        }

        public FontModeFilter(FontModeFilter filter) {
            super(filter);
        }

        @Override
        public FontModeFilter copy() {
            return new FontModeFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StatusPreferencesFilter darkMode;

    private ExperienceLevelModeFilter experienceLevelMode;

    private FontModeFilter fontMode;

    private BooleanFilter contrastMode;

    private BooleanFilter colorVisionMode;

    private LongFilter userId;

    public PreferencesCriteria() {
    }

    public PreferencesCriteria(PreferencesCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.darkMode = other.darkMode == null ? null : other.darkMode.copy();
        this.experienceLevelMode = other.experienceLevelMode == null ? null : other.experienceLevelMode.copy();
        this.fontMode = other.fontMode == null ? null : other.fontMode.copy();
        this.contrastMode = other.contrastMode == null ? null : other.contrastMode.copy();
        this.colorVisionMode = other.colorVisionMode == null ? null : other.colorVisionMode.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
    }

    @Override
    public PreferencesCriteria copy() {
        return new PreferencesCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StatusPreferencesFilter getDarkMode() {
        return darkMode;
    }

    public void setDarkMode(StatusPreferencesFilter darkMode) {
        this.darkMode = darkMode;
    }

    public ExperienceLevelModeFilter getExperienceLevelMode() {
        return experienceLevelMode;
    }

    public void setExperienceLevelMode(ExperienceLevelModeFilter experienceLevelMode) {
        this.experienceLevelMode = experienceLevelMode;
    }

    public FontModeFilter getFontMode() {
        return fontMode;
    }

    public void setFontMode(FontModeFilter fontMode) {
        this.fontMode = fontMode;
    }

    public BooleanFilter getContrastMode() {
        return contrastMode;
    }

    public void setContrastMode(BooleanFilter contrastMode) {
        this.contrastMode = contrastMode;
    }

    public BooleanFilter getColorVisionMode() {
        return colorVisionMode;
    }

    public void setColorVisionMode(BooleanFilter colorVisionMode) {
        this.colorVisionMode = colorVisionMode;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PreferencesCriteria that = (PreferencesCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(darkMode, that.darkMode) &&
            Objects.equals(experienceLevelMode, that.experienceLevelMode) &&
            Objects.equals(fontMode, that.fontMode) &&
            Objects.equals(contrastMode, that.contrastMode) &&
            Objects.equals(colorVisionMode, that.colorVisionMode) &&
            Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        darkMode,
        experienceLevelMode,
        fontMode,
        contrastMode,
        colorVisionMode,
        userId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PreferencesCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (darkMode != null ? "darkMode=" + darkMode + ", " : "") +
                (experienceLevelMode != null ? "experienceLevelMode=" + experienceLevelMode + ", " : "") +
                (fontMode != null ? "fontMode=" + fontMode + ", " : "") +
                (contrastMode != null ? "contrastMode=" + contrastMode + ", " : "") +
                (colorVisionMode != null ? "colorVisionMode=" + colorVisionMode + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
            "}";
    }

}
