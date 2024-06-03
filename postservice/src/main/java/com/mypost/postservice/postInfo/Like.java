package com.mypost.postservice.postInfo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
// import com.project.proo.usreInfo.User;
import com.mypost.postservice.Userr;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "user_like")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "like_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    Boolean liked;
    
    // @ManyToOne
    // @JoinColumn(name = "user_id")
    // @JsonBackReference
    @Column
    private Integer userId;
}
