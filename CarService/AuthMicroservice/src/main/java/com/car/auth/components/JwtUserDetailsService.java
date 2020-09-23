package com.car.auth.components;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

  private UserServices userService;

  JwtUserDetailsService(UserServices userService) {
    this.userService = userService;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    // Username is Email here
    User user = userService.getUserByEmail(username);

    if (user != null) {
      List<SimpleGrantedAuthority> authority = new ArrayList<>();
      authority.add(new SimpleGrantedAuthority("ROLE_"+user.getType()));
      return new org.springframework.security.core.userdetails.User(
          user.getEmail(),
          user.getPassword(),
          authority
      );
    } else {
      throw new UsernameNotFoundException("User not found with username: " + username);
    }
  }
}