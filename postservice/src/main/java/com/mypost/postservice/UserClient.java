package com.mypost.postservice;


import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "userservice")
public interface UserClient {

    @GetMapping("users/{userId}")
public Userr getUser(@PathVariable Integer userId);

@GetMapping("/users/username/{username}")
 public Userr getUsersByUsername(@PathVariable("username") String username);

}
