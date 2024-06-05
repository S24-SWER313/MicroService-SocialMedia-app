package com.search.searchservice;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class searchController {

  @Autowired
  private SearchService searchService;
  
  @GetMapping("/search/{username}")
  public ResponseEntity<Userr> searchUsers(@PathVariable("username") String username) {
Userr users = searchService.searchUsersByUsername(username);
    return ResponseEntity.ok(users);
  }
}