package com.mypost.postservice.postInfo;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;

import java.util.List;



@Data
    public class User {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;
    
        @NotBlank
        @Size(min = 3, max = 20,message = "The Size of username mustbe between 3 and 20")
        private String username;
        
    //     @Size(min = 6, max = 40, message = "The size must be between 6 and 40!")
    //   @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{6,40}$",
    //           message = "Password must contain at least one digit, one lowercase letter, one uppercase letter, one special character, and be 6-40 characters long!")
      private String password;
    
      @NotBlank
      @Size(max = 50)
      @Email
      private String email;
    
        public User(){}
    
        public User(String username, String email, String password) {
            this.username = username;
            this.email = email;
            this.password = password;
        }
    
      
        private List<User> friends = new ArrayList<>();
    
    
    

    
        
        private List<Post> posts;
     
        // @OneToMany(mappedBy = "user")
        // @JsonIgnore
        // private List<Post> sharedPosts;
    
    
        private List<Like> likes;
    

        private List<Comment> comments;
      
    }
    
    
