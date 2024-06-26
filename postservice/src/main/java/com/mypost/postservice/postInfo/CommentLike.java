package com.mypost.postservice.postInfo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Data;
@Data
@Entity
@Table(name = "comment_like")
@DiscriminatorValue("comment")
public class CommentLike extends Like {
    @ManyToOne(optional = false) 
    @JoinColumn(name = "comment_id")
       //@JsonBackReference
        @JsonIgnore
    private Comment comment;

    @Column
    private Integer userId;
}
