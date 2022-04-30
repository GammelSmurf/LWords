//package org.example.authenticationmanager.Services.Impl;
//
//import org.example.authenticationmanager.Entities.User;
//import org.example.authenticationmanager.Security.MyUserDetails;
//import org.example.authenticationmanager.repos.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//@Service
//public class UserDetailsServiceImpl implements UserDetailsService {
//    @Autowired
//    private UserRepository userRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String username)
//            throws UsernameNotFoundException {
//        User user = userRepository.getUserByUsername(username);
//
//        if (user == null) {
//            throw new UsernameNotFoundException("Could not find user " + username);
//        }
//
//        return new MyUserDetails(user);
//    }
//}
