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

    private LongFilter userId;

    private LongFilter listFriendsId;

    public ProfileCriteria() {
    }

    public ProfileCriteria(ProfileCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.ultimaModificacao = other.ultimaModificacao == null ? null : other.ultimaModificacao.copy();
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
            Objects.equals(userId, that.userId) &&
            Objects.equals(listFriendsId, that.listFriendsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        status,
        ultimaModificacao,
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
                (userId != null ? "userId=" + userId + ", " : "") +
                (listFriendsId != null ? "listFriendsId=" + listFriendsId + ", " : "") +
            "}";
    }

}
