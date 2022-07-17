package com.ojt.controller;


import com.ojt.model.User;
import com.ojt.service.UserDetailsService;
import com.ojt.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("users/")
public class UserController {

    private final UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    public UserController(UserDetailsServiceImpl userDetailsServiceImpl){
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    @GetMapping("all")
    public ResponseEntity<List<User>> getAllUsers(){
        return new ResponseEntity<>(userDetailsServiceImpl.list(), HttpStatus.OK);
    }
    @Transactional
    @PostMapping("add")
    public ResponseEntity<User> addUser(@RequestBody  User user) throws Exception{
        return new ResponseEntity<>(userDetailsServiceImpl.create(user), HttpStatus.CREATED);

    }
    @Transactional
    @PutMapping("update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable ("id") Long id,
                            @RequestBody User user) throws Exception{
        return new ResponseEntity<>(userDetailsServiceImpl.update(id, user), HttpStatus.OK);
    }
    @Transactional
    @DeleteMapping("delete/{id}")
    public ResponseEntity<Boolean> deleteUser(Authentication authentication, @PathVariable ("id") Long id) throws Exception{
        return new ResponseEntity<>(userDetailsServiceImpl.delete(authentication,id), HttpStatus.NO_CONTENT);
    }


}
