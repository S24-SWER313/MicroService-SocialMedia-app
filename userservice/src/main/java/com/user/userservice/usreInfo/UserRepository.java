package com.user.userservice.usreInfo;


import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;




public interface UserRepository  extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    Boolean existsByUsername(String username);
    
    Boolean existsByEmail(String email);
    List<User> findByUsernameContainingIgnoreCase(String username);
    User findOneUserByUsername(String username);
}
