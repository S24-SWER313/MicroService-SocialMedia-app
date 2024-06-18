package com.mypost.postservice;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;







@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  @Autowired
  private UserClient userClient; 

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Userr user = userClient.getUsersByUsername(username);

    return UserDetailsImpl.build(user);
  }

}
