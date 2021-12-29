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
            "}";
    }
}
