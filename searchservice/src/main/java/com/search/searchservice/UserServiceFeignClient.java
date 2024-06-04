package com.search.searchservice;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@FeignClient(name = "userservice", url = "${userservice.url}", configuration = FeignConfig.class)
public interface UserServiceFeignClient {

    @GetMapping("/users/username")
 public Userr getUsersByUsername(@RequestParam("username") String username);
}