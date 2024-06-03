package com.mypost.postservice.postInfo;


import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.mypost.postservice.Userr;

@FeignClient(name = "userservice")
public interface UserClient {

    @GetMapping("users/{userId}")
public Userr getUser(@PathVariable Integer userId);

}
