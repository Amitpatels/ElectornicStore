package com.lcwd.electronic.store.ElectronicStore.services.impl;

import com.lcwd.electronic.store.ElectronicStore.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.ElectronicStore.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(()-> new ResourceNotFoundException("user not found of given username "+username));
    }
}
