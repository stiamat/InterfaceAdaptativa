package com.ufes.interfaceadaptativa.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.ufes.interfaceadaptativa.domain.Post;
import com.ufes.interfaceadaptativa.domain.*; // for static metamodels
import com.ufes.interfaceadaptativa.repository.PostRepository;
import com.ufes.interfaceadaptativa.service.dto.PostCriteria;
import com.ufes.interfaceadaptativa.service.dto.PostDTO;
import com.ufes.interfaceadaptativa.service.mapper.PostMapper;

/**
 * Service for executing complex queries for {@link Post} entities in the database.
 * The main input is a {@link PostCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PostDTO} or a {@link Page} of {@link PostDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PostQueryService extends QueryService<Post> {

    private final Logger log = LoggerFactory.getLogger(PostQueryService.class);

    private final PostRepository postRepository;

    private final PostMapper postMapper;

    public PostQueryService(PostRepository postRepository, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
    }

    /**
     * Return a {@link List} of {@link PostDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PostDTO> findByCriteria(PostCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Post> specification = createSpecification(criteria);
        return postMapper.toDto(postRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PostDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PostDTO> findByCriteria(PostCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Post> specification = createSpecification(criteria);
        return postRepository.findAll(specification, page)
            .map(postMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PostCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Post> specification = createSpecification(criteria);
        return postRepository.count(specification);
    }

    /**
     * Function to convert {@link PostCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Post> createSpecification(PostCriteria criteria) {
        Specification<Post> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Post_.id));
            }
            if (criteria.getBody() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBody(), Post_.body));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), Post_.date));
            }
            if (criteria.getActive() != null) {
                specification = specification.and(buildSpecification(criteria.getActive(), Post_.active));
            }
            if (criteria.getLikes() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLikes(), Post_.likes));
            }
//            if (criteria.getLink() != null) {
//                specification = specification.and(buildStringSpecification(criteria.getLink(), Post_.link));
//            }
            if (criteria.getTipoPost() != null) {
                specification = specification.and(buildSpecification(criteria.getTipoPost(), Post_.tipoPost));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserId(),
                    root -> root.join(Post_.user, JoinType.LEFT).get(User_.id)));
            }
//            if (criteria.getLikeDeId() != null) {
//                specification = specification.and(buildSpecification(criteria.getLikeDeId(),
//                    root -> root.join(Post_.likeDes, JoinType.LEFT).get(User_.id)));
//            }
//            if (criteria.getComentarioDeId() != null) {
//                specification = specification.and(buildSpecification(criteria.getComentarioDeId(),
//                    root -> root.join(Post_.comentarioDe, JoinType.LEFT).get(Post_.id)));
//            }
        }
        return specification;
    }
}
