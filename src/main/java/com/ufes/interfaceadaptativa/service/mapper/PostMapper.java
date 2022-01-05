package com.ufes.interfaceadaptativa.service.mapper;


import com.ufes.interfaceadaptativa.domain.*;
import com.ufes.interfaceadaptativa.service.dto.PostDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Post} and its DTO {@link PostDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface PostMapper extends EntityMapper<PostDTO, Post> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    @Mapping(source = "user.imageUrl", target = "userImagem")
    @Mapping(source = "comentarioDe.id", target = "comentarioDeId")
    @Mapping(source = "comentarioDe.user.login", target = "userComentario")
    PostDTO toDto(Post post);

    @Mapping(source = "userId", target = "user")
    @Mapping(target = "removeLikeDe", ignore = true)
    @Mapping(source = "comentarioDeId", target = "comentarioDe")
    Post toEntity(PostDTO postDTO);

    default Post fromId(Long id) {
        if (id == null) {
            return null;
        }
        Post post = new Post();
        post.setId(id);
        return post;
    }
}
