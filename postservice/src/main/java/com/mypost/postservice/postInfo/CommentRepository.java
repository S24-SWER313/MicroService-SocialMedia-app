package com.mypost.postservice.postInfo;
import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {


    List<Comment> findByPost_Id(Integer postId);
    
}
