package com.user.userservice.profileInfo;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;



@Component
public class ProfileModelAssembler implements RepresentationModelAssembler<Profile, EntityModel<Profile>> {

    @Override
    public EntityModel<Profile> toModel(Profile profile) {
        return EntityModel.of(profile,
                linkTo(methodOn(profileController.class).getProfile(profile.getId(),profile.getUser().getId())).withSelfRel());
        // Add more links if needed for related resources
    }
}
