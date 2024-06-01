package com.mypost.postservice.postInfo;

public class PostLikeNotFoundException extends RuntimeException {

    PostLikeNotFoundException(Integer id) {
      super("Could not find Like " + id);
    }
}