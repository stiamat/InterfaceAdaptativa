package com.ufes.interfaceadaptativa.repository;

import com.ufes.interfaceadaptativa.domain.Post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Post entity.
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {

    @Query("select post from Post post where post.user.login = ?#{principal.username}")
    List<Post> findByUserIsCurrentUser();

    @Query(value = "select distinct post from Post post left join fetch post.likeDes",
        countQuery = "select count(distinct post) from Post post")
    Page<Post> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct post from Post post left join fetch post.likeDes")
    List<Post> findAllWithEagerRelationships();

    @Query("select post from Post post left join fetch post.likeDes where post.id =:id")
    Optional<Post> findOneWithEagerRelationships(@Param("id") Long id);

    @Query("select post from Post post where post.comentarioDe.id = :id")
    Optional<List<Post>> findAnswersPost(@Param("id") Long id);

    @Query("select post from Post post where post.user.id = :userId and post.comentarioDe is not null")
    Optional<List<Post>> findAnswersUser(@Param("userId") Long userId);

    @Query("select post from Post post where post.user.login LIKE %:login%")
    Optional<List<Post>> searchLogin(@Param("login") String login);

    @Query(value = "select post.id, body, date, active, likes, link, tipo_post, user_id, comentario_de_id from post inner join jhi_user on user_id = jhi_user.id where jhi_user.login like '%:login%'", nativeQuery = true)
    Optional<List<Post>> findLikesID(@Param("login") String login);
}
