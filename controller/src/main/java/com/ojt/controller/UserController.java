package com.ojt.controller;


import com.ojt.model.User;
import com.ojt.service.UserDetailsService;
import com.ojt.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<User> getAllUsers(){
        return userDetailsServiceImpl.list();
    }

    @PostMapping("add")
    public User addUser(@RequestBody  User user) throws Exception{
        return userDetailsServiceImpl.create(user);
    }

    @PutMapping("update/{id}")
    public User updateUser(@PathVariable ("id") Long id,
                            @RequestBody User user) throws Exception{

        return userDetailsServiceImpl.update(id, user);
    }

    @DeleteMapping("delete/{id}")
    public Boolean deleteUser(@PathVariable ("id") Long id) throws Exception{
        return userDetailsServiceImpl.delete(id);
    }


}
