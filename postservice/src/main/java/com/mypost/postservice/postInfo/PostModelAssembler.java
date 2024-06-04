package com.mypost.postservice.postInfo;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.mypost.postservice.UserClient;



@Component
public class PostModelAssembler implements RepresentationModelAssembler<Post, EntityModel<Post>> {

    @Override
  public EntityModel<Post> toModel(Post post) {
   Integer userId = post.getUserId();

    return EntityModel.of(post, //
        linkTo(methodOn(PostController.class).one(userId,post.getId())).withSelfRel(),
        linkTo(methodOn(PostController.class).all(userId)).withRel("Posts"),
        linkTo(UserClient.class).slash(post.getUserId()).withRel("user"));


  }
 
    
}