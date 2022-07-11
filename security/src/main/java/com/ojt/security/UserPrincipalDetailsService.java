package com.ojt.persistence;

import com.ojt.model.User;
import com.ojt.persistence.UserPrincipal;
import com.ojt.persistence.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserPrincipalDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    public UserPrincipalDetailsService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findUserByUsername(username);
        UserPrincipal userPrincipal = new UserPrincipal(user);

        return userPrincipal;
    }
}
