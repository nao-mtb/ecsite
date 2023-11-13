package jp.haru_idea.springboot.ec_site.services;

import java.util.Collections;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// @Service
// public class MyUserServiceBK implements UserDetailsService{

//     @Override
//     public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
    
//     if(username.equals("test1@test")){
//         return new User(username,"$2a$08$71yWnvzHyJa7IruW94lHAell7pMhXkRssEMkVdYg6TGFXYGxBaYle",
//         Collections.emptySet());
//     } else {
//         throw new UsernameNotFoundException("User is not found");
//         }
//     }
    
// }
