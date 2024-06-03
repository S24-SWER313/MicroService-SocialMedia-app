package com.mypost.postservice.postInfo;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PostLikeAssembler implements RepresentationModelAssembler<PostLike, EntityModel<PostLike>> {

    @Override
    public EntityModel<PostLike> toModel(PostLike postLike) {
        return EntityModel.of(postLike,
                // Link to PostLike
                linkTo(methodOn(LikeController.class).getPostLike(postLike.getPost().getId(),postLike.getId())).withSelfRel(),
                linkTo(UserClient.class).slash(postLike.getUserId()).withRel("user")
                // Add more links as needed
        );
    }
}
