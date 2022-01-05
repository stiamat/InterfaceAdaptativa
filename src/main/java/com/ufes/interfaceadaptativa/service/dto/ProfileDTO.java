package com.ufes.interfaceadaptativa.service.dto;

import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import com.ufes.interfaceadaptativa.domain.enumeration.StatusProfile;

/**
 * A DTO for the {@link com.ufes.interfaceadaptativa.domain.Profile} entity.
 */
public class ProfileDTO implements Serializable {
    
    private Long id;

    private StatusProfile status;

    private ZonedDateTime ultimaModificacao;

    private Integer numModificacao;

    private Integer age;

    private Boolean auditoryDisabilities;

    private Boolean blindness;

    private Boolean colorVision;

    private Boolean contrastSensitivity;

    private Boolean fildOfVision;

    private Boolean lightSensitivity;

    private Boolean visualAcuity;

    private String education;

    private String experienceLevel;

    private String gender;

    private String language;


    private Long userId;

    private String userLogin;
    private Set<UserDTO> listFriends = new HashSet<>();
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StatusProfile getStatus() {
        return status;
    }

    public void setStatus(StatusProfile status) {
        this.status = status;
    }

    public ZonedDateTime getUltimaModificacao() {
        return ultimaModificacao;
    }

    public void setUltimaModificacao(ZonedDateTime ultimaModificacao) {
        this.ultimaModificacao = ultimaModificacao;
    }

    public Integer getNumModificacao() {
        return numModificacao;
    }

    public void setNumModificacao(Integer numModificacao) {
        this.numModificacao = numModificacao;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Boolean isAuditoryDisabilities() {
        return auditoryDisabilities;
    }

    public void setAuditoryDisabilities(Boolean auditoryDisabilities) {
        this.auditoryDisabilities = auditoryDisabilities;
    }

    public Boolean isBlindness() {
        return blindness;
    }

    public void setBlindness(Boolean blindness) {
        this.blindness = blindness;
    }

    public Boolean isColorVision() {
        return colorVision;
    }

    public void setColorVision(Boolean colorVision) {
        this.colorVision = colorVision;
    }

    public Boolean isContrastSensitivity() {
        return contrastSensitivity;
    }

    public void setContrastSensitivity(Boolean contrastSensitivity) {
        this.contrastSensitivity = contrastSensitivity;
    }

    public Boolean isFildOfVision() {
        return fildOfVision;
    }

    public void setFildOfVision(Boolean fildOfVision) {
        this.fildOfVision = fildOfVision;
    }

    public Boolean isLightSensitivity() {
        return lightSensitivity;
    }

    public void setLightSensitivity(Boolean lightSensitivity) {
        this.lightSensitivity = lightSensitivity;
    }

    public Boolean isVisualAcuity() {
        return visualAcuity;
    }

    public void setVisualAcuity(Boolean visualAcuity) {
        this.visualAcuity = visualAcuity;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getExperienceLevel() {
        return experienceLevel;
    }

    public void setExperienceLevel(String experienceLevel) {
        this.experienceLevel = experienceLevel;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
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

    public Set<UserDTO> getListFriends() {
        return listFriends;
    }

    public void setListFriends(Set<UserDTO> users) {
        this.listFriends = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProfileDTO)) {
            return false;
        }

        return id != null && id.equals(((ProfileDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProfileDTO{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", ultimaModificacao='" + getUltimaModificacao() + "'" +
            ", numModificacao=" + getNumModificacao() +
            ", age=" + getAge() +
            ", auditoryDisabilities='" + isAuditoryDisabilities() + "'" +
            ", blindness='" + isBlindness() + "'" +
            ", colorVision='" + isColorVision() + "'" +
            ", contrastSensitivity='" + isContrastSensitivity() + "'" +
            ", fildOfVision='" + isFildOfVision() + "'" +
            ", lightSensitivity='" + isLightSensitivity() + "'" +
            ", visualAcuity='" + isVisualAcuity() + "'" +
            ", education='" + getEducation() + "'" +
            ", experienceLevel='" + getExperienceLevel() + "'" +
            ", gender='" + getGender() + "'" +
            ", language='" + getLanguage() + "'" +
            ", userId=" + getUserId() +
            ", userLogin='" + getUserLogin() + "'" +
            ", listFriends='" + getListFriends() + "'" +
            "}";
    }
}
