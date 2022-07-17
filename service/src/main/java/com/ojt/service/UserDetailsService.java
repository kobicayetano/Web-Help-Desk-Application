package com.ojt.service;

import com.ojt.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface UserDetailsService {

    @PreAuthorize("hasRole('ADMIN')")
    public List<User> list();

    @PreAuthorize("hasRole('ADMIN')")
    public User create(User user) throws Exception;

    @PreAuthorize("hasRole('ADMIN')")
    public Boolean delete(Authentication authentication,Long userId) throws Exception;

    @PreAuthorize("hasRole('ADMIN')")
    public User update(Long userId, User updatedUser) throws Exception;

}
