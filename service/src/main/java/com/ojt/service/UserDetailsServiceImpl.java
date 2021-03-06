package com.ojt.service;

import com.ojt.model.User;
import com.ojt.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;




    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> list() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public User create(User user) throws Exception {
        if(userRepository.existsByUsername(user.getUsername())){
            throw new Exception("User with username: " + user.getUsername() + " already exists.");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public Boolean delete(Authentication authentication, Long userId) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(()->new  Exception ("User with id: "+ userId + " does not exists."));

        String currentUser = authentication.getName();
        if(currentUser.equals(user.getUsername())){
            throw new Exception("Cannot delete self.");
        }

        userRepository.delete(user);
        return true;
    }



    @Override
    @Transactional
    public User update(Long userId, User updatedUser) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(()->new  Exception ("User with id: "+ userId + " does not exists."));

        user.setUsername(updatedUser.getUsername());
        user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        user.setRole(updatedUser.getRole());

        return userRepository.save(user);
    }
}
