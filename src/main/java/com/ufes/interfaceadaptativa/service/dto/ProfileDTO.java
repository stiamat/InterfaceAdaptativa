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
            ", userId=" + getUserId() +
            ", userLogin='" + getUserLogin() + "'" +
            ", listFriends='" + getListFriends() + "'" +
            "}";
    }
}
