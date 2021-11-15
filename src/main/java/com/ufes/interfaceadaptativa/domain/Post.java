package com.ufes.interfaceadaptativa.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

import com.ufes.interfaceadaptativa.domain.enumeration.TipoPost;

/**
 * A Post.
 */
@Entity
@Table(name = "post")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Post implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "body")
    private String body;

    @Column(name = "date")
    private ZonedDateTime date;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "likes")
    private Long likes;

    @Column(name = "link")
    private String link;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_post")
    private TipoPost tipoPost;

    @ManyToOne
    @JsonIgnoreProperties(value = "posts", allowSetters = true)
    private User user;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(name = "post_like_de",
               joinColumns = @JoinColumn(name = "post_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "like_de_id", referencedColumnName = "id"))
    private Set<User> likeDes = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "posts", allowSetters = true)
    private Post comentarioDe;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public Post body(String body) {
        this.body = body;
        return this;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public Post date(ZonedDateTime date) {
        this.date = date;
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public Boolean isActive() {
        return active;
    }

    public Post active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Long getLikes() {
        return likes;
    }

    public Post likes(Long likes) {
        this.likes = likes;
        return this;
    }

    public void setLikes(Long likes) {
        this.likes = likes;
    }

    public String getLink() {
        return link;
    }

    public Post link(String link) {
        this.link = link;
        return this;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public TipoPost getTipoPost() {
        return tipoPost;
    }

    public Post tipoPost(TipoPost tipoPost) {
        this.tipoPost = tipoPost;
        return this;
    }

    public void setTipoPost(TipoPost tipoPost) {
        this.tipoPost = tipoPost;
    }

    public User getUser() {
        return user;
    }

    public Post user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<User> getLikeDes() {
        return likeDes;
    }

    public Post likeDes(Set<User> users) {
        this.likeDes = users;
        return this;
    }

    public Post addLikeDe(User user) {
        this.likeDes.add(user);
        return this;
    }

    public Post removeLikeDe(User user) {
        this.likeDes.remove(user);
        return this;
    }

    public void setLikeDes(Set<User> users) {
        this.likeDes = users;
    }

    public Post getComentarioDe() {
        return comentarioDe;
    }

    public Post comentarioDe(Post post) {
        this.comentarioDe = post;
        return this;
    }

    public void setComentarioDe(Post post) {
        this.comentarioDe = post;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Post)) {
            return false;
        }
        return id != null && id.equals(((Post) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Post{" +
            "id=" + getId() +
            ", body='" + getBody() + "'" +
            ", date='" + getDate() + "'" +
            ", active='" + isActive() + "'" +
            ", likes=" + getLikes() +
            ", link='" + getLink() + "'" +
            ", tipoPost='" + getTipoPost() + "'" +
            "}";
    }
}
