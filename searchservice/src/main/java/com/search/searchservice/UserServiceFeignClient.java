package com.search.searchservice;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@FeignClient(name = "userservice")
public interface UserServiceFeignClient {

   
@GetMapping("/users/username/{username}")
 public Userr getUsersByUsername(@PathVariable("username") String username);
}