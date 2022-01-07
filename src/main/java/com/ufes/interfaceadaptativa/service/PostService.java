package com.ufes.interfaceadaptativa.service;

import com.ufes.interfaceadaptativa.service.dto.PostDTO;

import com.ufes.interfaceadaptativa.service.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.ufes.interfaceadaptativa.domain.Post}.
 */
public interface PostService {

    /**
     * Save a post.
     *
     * @param postDTO the entity to save.
     * @return the persisted entity.
     */
    PostDTO save(PostDTO postDTO);

    /**
     * Get all the posts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PostDTO> findAll(Pageable pageable);

    /**
     * Get all the posts with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    Page<PostDTO> findAllWithEagerRelationships(Pageable pageable);


    /**
     * Get the "id" post.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PostDTO> findOne(Long id);

    /**
     * Delete the "id" post.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<UserDTO> curti(long postId, long userId);

    void owl(long idUser);
}
