package com.search.searchservice;

// import jakarta.validation.constraints.Email;
// import jakarta.validation.constraints.NotBlank;
// import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;


import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Data
public class Userr {
   @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // @NotBlank
    // @Size(min = 3, max = 20, message = "The Size of username must be between 3 and 20")
    private String username;

    private String password;

    // @NotBlank
    // @Size(max = 50)
    // @Email
    private String email;

    public Userr() {}

    public Userr(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @JsonIgnore
    private List<Userr> friends = new ArrayList<>();

  
}
