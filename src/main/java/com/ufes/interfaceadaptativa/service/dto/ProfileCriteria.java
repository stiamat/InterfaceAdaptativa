package com.ufes.interfaceadaptativa.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.ufes.interfaceadaptativa.domain.enumeration.StatusProfile;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link com.ufes.interfaceadaptativa.domain.Profile} entity. This class is used
 * in {@link com.ufes.interfaceadaptativa.web.rest.ProfileResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /profiles?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProfileCriteria implements Serializable, Criteria {
    /**
     * Class for filtering StatusProfile
     */
    public static class StatusProfileFilter extends Filter<StatusProfile> {

        public StatusProfileFilter() {
        }

        public StatusProfileFilter(StatusProfileFilter filter) {
            super(filter);
        }

        @Override
        public StatusProfileFilter copy() {
            return new StatusProfileFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StatusProfileFilter status;

    private ZonedDateTimeFilter ultimaModificacao;

    private IntegerFilter numModificacao;

    private IntegerFilter age;

    private BooleanFilter auditoryDisabilities;

    private BooleanFilter blindness;

    private BooleanFilter colorVision;

    private BooleanFilter contrastSensitivity;

    private BooleanFilter fildOfVision;

    private BooleanFilter lightSensitivity;

    private BooleanFilter visualAcuity;

    private StringFilter education;

    private StringFilter experienceLevel;

    private StringFilter gender;

    private StringFilter language;

    private LongFilter userId;

    private LongFilter listFriendsId;

    public ProfileCriteria() {
    }

    public ProfileCriteria(ProfileCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.ultimaModificacao = other.ultimaModificacao == null ? null : other.ultimaModificacao.copy();
        this.numModificacao = other.numModificacao == null ? null : other.numModificacao.copy();
        this.age = other.age == null ? null : other.age.copy();
        this.auditoryDisabilities = other.auditoryDisabilities == null ? null : other.auditoryDisabilities.copy();
        this.blindness = other.blindness == null ? null : other.blindness.copy();
        this.colorVision = other.colorVision == null ? null : other.colorVision.copy();
        this.contrastSensitivity = other.contrastSensitivity == null ? null : other.contrastSensitivity.copy();
        this.fildOfVision = other.fildOfVision == null ? null : other.fildOfVision.copy();
        this.lightSensitivity = other.lightSensitivity == null ? null : other.lightSensitivity.copy();
        this.visualAcuity = other.visualAcuity == null ? null : other.visualAcuity.copy();
        this.education = other.education == null ? null : other.education.copy();
        this.experienceLevel = other.experienceLevel == null ? null : other.experienceLevel.copy();
        this.gender = other.gender == null ? null : other.gender.copy();
        this.language = other.language == null ? null : other.language.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.listFriendsId = other.listFriendsId == null ? null : other.listFriendsId.copy();
    }

    @Override
    public ProfileCriteria copy() {
        return new ProfileCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StatusProfileFilter getStatus() {
        return status;
    }

    public void setStatus(StatusProfileFilter status) {
        this.status = status;
    }

    public ZonedDateTimeFilter getUltimaModificacao() {
        return ultimaModificacao;
    }

    public void setUltimaModificacao(ZonedDateTimeFilter ultimaModificacao) {
        this.ultimaModificacao = ultimaModificacao;
    }

    public IntegerFilter getNumModificacao() {
        return numModificacao;
    }

    public void setNumModificacao(IntegerFilter numModificacao) {
        this.numModificacao = numModificacao;
    }

    public IntegerFilter getAge() {
        return age;
    }

    public void setAge(IntegerFilter age) {
        this.age = age;
    }

    public BooleanFilter getAuditoryDisabilities() {
        return auditoryDisabilities;
    }

    public void setAuditoryDisabilities(BooleanFilter auditoryDisabilities) {
        this.auditoryDisabilities = auditoryDisabilities;
    }

    public BooleanFilter getBlindness() {
        return blindness;
    }

    public void setBlindness(BooleanFilter blindness) {
        this.blindness = blindness;
    }

    public BooleanFilter getColorVision() {
        return colorVision;
    }

    public void setColorVision(BooleanFilter colorVision) {
        this.colorVision = colorVision;
    }

    public BooleanFilter getContrastSensitivity() {
        return contrastSensitivity;
    }

    public void setContrastSensitivity(BooleanFilter contrastSensitivity) {
        this.contrastSensitivity = contrastSensitivity;
    }

    public BooleanFilter getFildOfVision() {
        return fildOfVision;
    }

    public void setFildOfVision(BooleanFilter fildOfVision) {
        this.fildOfVision = fildOfVision;
    }

    public BooleanFilter getLightSensitivity() {
        return lightSensitivity;
    }

    public void setLightSensitivity(BooleanFilter lightSensitivity) {
        this.lightSensitivity = lightSensitivity;
    }

    public BooleanFilter getVisualAcuity() {
        return visualAcuity;
    }

    public void setVisualAcuity(BooleanFilter visualAcuity) {
        this.visualAcuity = visualAcuity;
    }

    public StringFilter getEducation() {
        return education;
    }

    public void setEducation(StringFilter education) {
        this.education = education;
    }

    public StringFilter getExperienceLevel() {
        return experienceLevel;
    }

    public void setExperienceLevel(StringFilter experienceLevel) {
        this.experienceLevel = experienceLevel;
    }

    public StringFilter getGender() {
        return gender;
    }

    public void setGender(StringFilter gender) {
        this.gender = gender;
    }

    public StringFilter getLanguage() {
        return language;
    }

    public void setLanguage(StringFilter language) {
        this.language = language;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getListFriendsId() {
        return listFriendsId;
    }

    public void setListFriendsId(LongFilter listFriendsId) {
        this.listFriendsId = listFriendsId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProfileCriteria that = (ProfileCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(status, that.status) &&
            Objects.equals(ultimaModificacao, that.ultimaModificacao) &&
            Objects.equals(numModificacao, that.numModificacao) &&
            Objects.equals(age, that.age) &&
            Objects.equals(auditoryDisabilities, that.auditoryDisabilities) &&
            Objects.equals(blindness, that.blindness) &&
            Objects.equals(colorVision, that.colorVision) &&
            Objects.equals(contrastSensitivity, that.contrastSensitivity) &&
            Objects.equals(fildOfVision, that.fildOfVision) &&
            Objects.equals(lightSensitivity, that.lightSensitivity) &&
            Objects.equals(visualAcuity, that.visualAcuity) &&
            Objects.equals(education, that.education) &&
            Objects.equals(experienceLevel, that.experienceLevel) &&
            Objects.equals(gender, that.gender) &&
            Objects.equals(language, that.language) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(listFriendsId, that.listFriendsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        status,
        ultimaModificacao,
        numModificacao,
        age,
        auditoryDisabilities,
        blindness,
        colorVision,
        contrastSensitivity,
        fildOfVision,
        lightSensitivity,
        visualAcuity,
        education,
        experienceLevel,
        gender,
        language,
        userId,
        listFriendsId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProfileCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (ultimaModificacao != null ? "ultimaModificacao=" + ultimaModificacao + ", " : "") +
                (numModificacao != null ? "numModificacao=" + numModificacao + ", " : "") +
                (age != null ? "age=" + age + ", " : "") +
                (auditoryDisabilities != null ? "auditoryDisabilities=" + auditoryDisabilities + ", " : "") +
                (blindness != null ? "blindness=" + blindness + ", " : "") +
                (colorVision != null ? "colorVision=" + colorVision + ", " : "") +
                (contrastSensitivity != null ? "contrastSensitivity=" + contrastSensitivity + ", " : "") +
                (fildOfVision != null ? "fildOfVision=" + fildOfVision + ", " : "") +
                (lightSensitivity != null ? "lightSensitivity=" + lightSensitivity + ", " : "") +
                (visualAcuity != null ? "visualAcuity=" + visualAcuity + ", " : "") +
                (education != null ? "education=" + education + ", " : "") +
                (experienceLevel != null ? "experienceLevel=" + experienceLevel + ", " : "") +
                (gender != null ? "gender=" + gender + ", " : "") +
                (language != null ? "language=" + language + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
                (listFriendsId != null ? "listFriendsId=" + listFriendsId + ", " : "") +
            "}";
    }

}
