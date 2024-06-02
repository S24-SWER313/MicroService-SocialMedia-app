package com.mypost.postservice.postInfo;

public class CommentLikeNotFoundException extends RuntimeException {

    CommentLikeNotFoundException(Integer id) {
      super("Could not find Like " + id);
    }

}