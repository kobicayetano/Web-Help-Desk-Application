package com.ojt.service;

import com.ojt.model.Employee;
import com.ojt.model.User;
import com.ojt.persistence.EmployeeRepository;
import com.ojt.persistence.TicketRepository;
import com.ojt.persistence.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith({MockitoExtension.class})
class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp(){
        underTest = new UserDetailsServiceImpl(userRepository,passwordEncoder);
    }

    private UserDetailsServiceImpl underTest;

    @Test
    void canGetAllUsers(){
        underTest.list();
        verify(userRepository).findAll();
    }

    @Test
    void canCreateNewUser() throws Exception{
        //Given
        User user = new User(
          "username",
          "password",
          "ADMIN"
        );
        //when
        underTest.create(user);
        //then
        ArgumentCaptor<User> userArgumentCaptor =
                ArgumentCaptor.forClass(User.class);

        verify(userRepository)
                .save(userArgumentCaptor.capture());

        User capturedUser = userArgumentCaptor.getValue();
        assertThat(capturedUser).isEqualTo(user);
    }

    @Test
    public void cannotCreateNewUserWithExistingUsername(){
        //Given
        User user = new User(
                "username",
                "password",
                "ADMIN"
        );
        //when
        given(userRepository.existsByUsername(any())).willReturn(true);
        assertThatThrownBy(()-> underTest.create(user))
                .hasMessage("User with username: " + user.getUsername() + " already exists.");
    }

    



}