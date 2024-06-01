package com.mypost.postservice.postInfo;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "userservice")
public interface UserClient {
 @GetMapping("/users/{id}")
    User getUserById(@PathVariable("id") Integer id);
}
