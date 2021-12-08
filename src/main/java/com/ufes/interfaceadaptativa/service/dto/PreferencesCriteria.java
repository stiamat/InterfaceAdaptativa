package com.ufes.interfaceadaptativa.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.ufes.interfaceadaptativa.domain.enumeration.StatusPreferences;
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

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StatusPreferencesFilter darkMode;

    private LongFilter userId;

    public PreferencesCriteria() {
    }

    public PreferencesCriteria(PreferencesCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.darkMode = other.darkMode == null ? null : other.darkMode.copy();
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
            Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        darkMode,
        userId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PreferencesCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (darkMode != null ? "darkMode=" + darkMode + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
            "}";
    }

}
