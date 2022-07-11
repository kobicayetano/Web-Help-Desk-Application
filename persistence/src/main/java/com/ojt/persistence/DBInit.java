package com.ojt.persistence;

import com.ojt.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class DBInit implements CommandLineRunner {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public DBInit(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        //this.userRepository.deleteAll();

        User admin = new User("account.admin", passwordEncoder.encode("password.admin"), "ADMIN");
        User admin1 = new User("account.admin1", passwordEncoder.encode("password.admin1"), "ADMIN");
        User user = new User("account.user", passwordEncoder.encode("password.user"), "USER");

        List<User> userList = Arrays.asList(admin, admin1, user);

        userRepository.saveAll(userList);


    }
}
