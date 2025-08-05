package com.learningspringboot.samah.employees.service.impl;

import com.learningspringboot.samah.employees.model.MyUser;
import com.learningspringboot.samah.employees.repository.MyUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private MyUserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        //Optional<MyUser> myUser = repository.findByEmail(email);
        MyUser myUser = repository.findByEmail(email);
        if (myUser != null) {
            return new CustomUserDetails(MyUser.builder()
                    .email(myUser.getEmail())
                    .password(myUser.getPassword())
                    .role(myUser.getRole()) //"ADMIN"
                    .build());
        } else {
            throw new UsernameNotFoundException(email);
        }
    }

    private String[] getRoles(Enum<?>[] enums) {
        String[] stringArray = new String[enums.length];
        for (int i = 0; i < enums.length; i++) {
            stringArray[i] = enums[i].toString();
        }
        return stringArray;
    }
}
