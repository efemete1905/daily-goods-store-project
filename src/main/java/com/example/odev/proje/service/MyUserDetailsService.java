package com.example.odev.proje.service;


import com.example.odev.proje.dao.usersRepository;
import com.example.odev.proje.entity.MyUserDetails;
import com.example.odev.proje.entity.users;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    usersRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<users> theusers = userRepository.findByUsername(userName);

        theusers.orElseThrow(() -> new UsernameNotFoundException("Not found: " + userName));

        return theusers.map(MyUserDetails::new).get();
    }

}
