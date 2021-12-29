package com.ufes.interfaceadaptativa.service.dto;

import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import com.ufes.interfaceadaptativa.domain.enumeration.TipoPost;

/**
 * A DTO for the {@link com.ufes.interfaceadaptativa.domain.Post} entity.
 */
public class PostDTO implements Serializable {

    private Long id;

    private String body;

    private ZonedDateTime date;

    private Boolean active;

    private Long likes;

    private String link;

    private TipoPost tipoPost;


    private Long userId;

    private String userLogin;

    private String userImagem;

    private Set<UserDTO> likeDes = new HashSet<>();

    private Long comentarioDeId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Long getLikes() {
        return likes;
    }

    public void setLikes(Long likes) {
        this.likes = likes;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public TipoPost getTipoPost() {
        return tipoPost;
    }

    public void setTipoPost(TipoPost tipoPost) {
        this.tipoPost = tipoPost;
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

    public Set<UserDTO> getLikeDes() {
        return likeDes;
    }

    public void setLikeDes(Set<UserDTO> users) {
        this.likeDes = users;
    }

    public Long getComentarioDeId() {
        return comentarioDeId;
    }

    public void setComentarioDeId(Long postId) {
        this.comentarioDeId = postId;
    }

    public String getUserImagem() {
        return userImagem;
    }

    public void setUserImagem(String userImagem) {
        this.userImagem = userImagem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PostDTO)) {
            return false;
        }

        return id != null && id.equals(((PostDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PostDTO{" +
            "id=" + getId() +
            ", body='" + getBody() + "'" +
            ", date='" + getDate() + "'" +
            ", active='" + isActive() + "'" +
            ", likes=" + getLikes() +
            ", link='" + getLink() + "'" +
            ", tipoPost='" + getTipoPost() + "'" +
            ", userId=" + getUserId() +
            ", userLogin='" + getUserLogin() + "'" +
            ", comentarioDeId=" + getComentarioDeId() +
            "}";
    }
}
