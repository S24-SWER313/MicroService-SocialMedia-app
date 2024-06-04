package com.search.searchservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SearchService {

  @Autowired
  private UserServiceFeignClient userServiceFeignClient;

  public Userr searchUsersByUsername(String username) {
    return userServiceFeignClient.getUsersByUsername(username);
  }
}
