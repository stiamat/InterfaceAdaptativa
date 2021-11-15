package com.ufes.interfaceadaptativa.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.ufes.interfaceadaptativa.domain.enumeration.TipoPost;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link com.ufes.interfaceadaptativa.domain.Post} entity. This class is used
 * in {@link com.ufes.interfaceadaptativa.web.rest.PostResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /posts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PostCriteria implements Serializable, Criteria {
    /**
     * Class for filtering TipoPost
     */
    public static class TipoPostFilter extends Filter<TipoPost> {

        public TipoPostFilter() {
        }

        public TipoPostFilter(TipoPostFilter filter) {
            super(filter);
        }

        @Override
        public TipoPostFilter copy() {
            return new TipoPostFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter body;

    private ZonedDateTimeFilter date;

    private BooleanFilter active;

    private LongFilter likes;

    private StringFilter link;

    private TipoPostFilter tipoPost;

    private LongFilter userId;

    private LongFilter likeDeId;

    private LongFilter comentarioDeId;

    public PostCriteria() {
    }

    public PostCriteria(PostCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.body = other.body == null ? null : other.body.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.active = other.active == null ? null : other.active.copy();
        this.likes = other.likes == null ? null : other.likes.copy();
        this.link = other.link == null ? null : other.link.copy();
        this.tipoPost = other.tipoPost == null ? null : other.tipoPost.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.likeDeId = other.likeDeId == null ? null : other.likeDeId.copy();
        this.comentarioDeId = other.comentarioDeId == null ? null : other.comentarioDeId.copy();
    }

    @Override
    public PostCriteria copy() {
        return new PostCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getBody() {
        return body;
    }

    public void setBody(StringFilter body) {
        this.body = body;
    }

    public ZonedDateTimeFilter getDate() {
        return date;
    }

    public void setDate(ZonedDateTimeFilter date) {
        this.date = date;
    }

    public BooleanFilter getActive() {
        return active;
    }

    public void setActive(BooleanFilter active) {
        this.active = active;
    }

    public LongFilter getLikes() {
        return likes;
    }

    public void setLikes(LongFilter likes) {
        this.likes = likes;
    }

    public StringFilter getLink() {
        return link;
    }

    public void setLink(StringFilter link) {
        this.link = link;
    }

    public TipoPostFilter getTipoPost() {
        return tipoPost;
    }

    public void setTipoPost(TipoPostFilter tipoPost) {
        this.tipoPost = tipoPost;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getLikeDeId() {
        return likeDeId;
    }

    public void setLikeDeId(LongFilter likeDeId) {
        this.likeDeId = likeDeId;
    }

    public LongFilter getComentarioDeId() {
        return comentarioDeId;
    }

    public void setComentarioDeId(LongFilter comentarioDeId) {
        this.comentarioDeId = comentarioDeId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PostCriteria that = (PostCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(body, that.body) &&
            Objects.equals(date, that.date) &&
            Objects.equals(active, that.active) &&
            Objects.equals(likes, that.likes) &&
            Objects.equals(link, that.link) &&
            Objects.equals(tipoPost, that.tipoPost) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(likeDeId, that.likeDeId) &&
            Objects.equals(comentarioDeId, that.comentarioDeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        body,
        date,
        active,
        likes,
        link,
        tipoPost,
        userId,
        likeDeId,
        comentarioDeId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PostCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (body != null ? "body=" + body + ", " : "") +
                (date != null ? "date=" + date + ", " : "") +
                (active != null ? "active=" + active + ", " : "") +
                (likes != null ? "likes=" + likes + ", " : "") +
                (link != null ? "link=" + link + ", " : "") +
                (tipoPost != null ? "tipoPost=" + tipoPost + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
                (likeDeId != null ? "likeDeId=" + likeDeId + ", " : "") +
                (comentarioDeId != null ? "comentarioDeId=" + comentarioDeId + ", " : "") +
            "}";
    }

}
