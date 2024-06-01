package com.mypost.postservice.postInfo;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;



import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CommentLikeAssembler implements RepresentationModelAssembler<CommentLike, EntityModel<CommentLike>> {

    @Override
    public EntityModel<CommentLike> toModel(CommentLike commentLike) {
        return EntityModel.of(commentLike,
                
                linkTo(methodOn(LikeController.class).getCommentLike(commentLike.getComment().getPost().getId(),commentLike.getComment().getId(),commentLike.getId())).withSelfRel(),
                linkTo(UserClient.class).slash(commentLike.getUser().getId()).withRel("user")
        );
    }
}

