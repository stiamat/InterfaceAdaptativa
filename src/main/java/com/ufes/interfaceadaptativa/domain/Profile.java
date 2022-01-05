package com.ufes.interfaceadaptativa.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

import com.ufes.interfaceadaptativa.domain.enumeration.StatusProfile;

/**
 * A Profile.
 */
@Entity
@Table(name = "profile")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Profile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusProfile status;

    @Column(name = "ultima_modificacao")
    private ZonedDateTime ultimaModificacao;

    @Column(name = "num_modificacao")
    private Integer numModificacao;

    @Column(name = "age")
    private Integer age;

    @Column(name = "auditory_disabilities")
    private Boolean auditoryDisabilities;

    @Column(name = "blindness")
    private Boolean blindness;

    @Column(name = "color_vision")
    private Boolean colorVision;

    @Column(name = "contrast_sensitivity")
    private Boolean contrastSensitivity;

    @Column(name = "fild_of_vision")
    private Boolean fildOfVision;

    @Column(name = "light_sensitivity")
    private Boolean lightSensitivity;

    @Column(name = "visual_acuity")
    private Boolean visualAcuity;

    @Column(name = "education")
    private String education;

    @Column(name = "experience_level")
    private String experienceLevel;

    @Column(name = "gender")
    private String gender;

    @Column(name = "language")
    private String language;

    @OneToOne

    @MapsId
    @JoinColumn(name = "id")
    private User user;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(name = "profile_list_friends",
               joinColumns = @JoinColumn(name = "profile_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "list_friends_id", referencedColumnName = "id"))
    private Set<User> listFriends = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StatusProfile getStatus() {
        return status;
    }

    public Profile status(StatusProfile status) {
        this.status = status;
        return this;
    }

    public void setStatus(StatusProfile status) {
        this.status = status;
    }

    public ZonedDateTime getUltimaModificacao() {
        return ultimaModificacao;
    }

    public Profile ultimaModificacao(ZonedDateTime ultimaModificacao) {
        this.ultimaModificacao = ultimaModificacao;
        return this;
    }

    public void setUltimaModificacao(ZonedDateTime ultimaModificacao) {
        this.ultimaModificacao = ultimaModificacao;
    }

    public Integer getNumModificacao() {
        return numModificacao;
    }

    public Profile numModificacao(Integer numModificacao) {
        this.numModificacao = numModificacao;
        return this;
    }

    public void setNumModificacao(Integer numModificacao) {
        this.numModificacao = numModificacao;
    }

    public Integer getAge() {
        return age;
    }

    public Profile age(Integer age) {
        this.age = age;
        return this;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Boolean isAuditoryDisabilities() {
        return auditoryDisabilities;
    }

    public Profile auditoryDisabilities(Boolean auditoryDisabilities) {
        this.auditoryDisabilities = auditoryDisabilities;
        return this;
    }

    public void setAuditoryDisabilities(Boolean auditoryDisabilities) {
        this.auditoryDisabilities = auditoryDisabilities;
    }

    public Boolean isBlindness() {
        return blindness;
    }

    public Profile blindness(Boolean blindness) {
        this.blindness = blindness;
        return this;
    }

    public void setBlindness(Boolean blindness) {
        this.blindness = blindness;
    }

    public Boolean isColorVision() {
        return colorVision;
    }

    public Profile colorVision(Boolean colorVision) {
        this.colorVision = colorVision;
        return this;
    }

    public void setColorVision(Boolean colorVision) {
        this.colorVision = colorVision;
    }

    public Boolean isContrastSensitivity() {
        return contrastSensitivity;
    }

    public Profile contrastSensitivity(Boolean contrastSensitivity) {
        this.contrastSensitivity = contrastSensitivity;
        return this;
    }

    public void setContrastSensitivity(Boolean contrastSensitivity) {
        this.contrastSensitivity = contrastSensitivity;
    }

    public Boolean isFildOfVision() {
        return fildOfVision;
    }

    public Profile fildOfVision(Boolean fildOfVision) {
        this.fildOfVision = fildOfVision;
        return this;
    }

    public void setFildOfVision(Boolean fildOfVision) {
        this.fildOfVision = fildOfVision;
    }

    public Boolean isLightSensitivity() {
        return lightSensitivity;
    }

    public Profile lightSensitivity(Boolean lightSensitivity) {
        this.lightSensitivity = lightSensitivity;
        return this;
    }

    public void setLightSensitivity(Boolean lightSensitivity) {
        this.lightSensitivity = lightSensitivity;
    }

    public Boolean isVisualAcuity() {
        return visualAcuity;
    }

    public Profile visualAcuity(Boolean visualAcuity) {
        this.visualAcuity = visualAcuity;
        return this;
    }

    public void setVisualAcuity(Boolean visualAcuity) {
        this.visualAcuity = visualAcuity;
    }

    public String getEducation() {
        return education;
    }

    public Profile education(String education) {
        this.education = education;
        return this;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getExperienceLevel() {
        return experienceLevel;
    }

    public Profile experienceLevel(String experienceLevel) {
        this.experienceLevel = experienceLevel;
        return this;
    }

    public void setExperienceLevel(String experienceLevel) {
        this.experienceLevel = experienceLevel;
    }

    public String getGender() {
        return gender;
    }

    public Profile gender(String gender) {
        this.gender = gender;
        return this;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLanguage() {
        return language;
    }

    public Profile language(String language) {
        this.language = language;
        return this;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public User getUser() {
        return user;
    }

    public Profile user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<User> getListFriends() {
        return listFriends;
    }

    public Profile listFriends(Set<User> users) {
        this.listFriends = users;
        return this;
    }

    public Profile addListFriends(User user) {
        this.listFriends.add(user);
        return this;
    }

    public Profile removeListFriends(User user) {
        this.listFriends.remove(user);
        return this;
    }

    public void setListFriends(Set<User> users) {
        this.listFriends = users;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Profile)) {
            return false;
        }
        return id != null && id.equals(((Profile) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Profile{" +
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
            "}";
    }
}
