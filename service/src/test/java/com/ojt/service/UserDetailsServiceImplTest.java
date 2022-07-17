package com.ojt.service;

import com.ojt.model.User;
import com.ojt.persistence.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    Authentication authentication;

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

    @Test
    public void canDeleteUser() throws Exception{
        Long id = 1L;
        User user = new User(
                "username",
                "password",
                "ADMIN"
        );

        given(userRepository.findById(any())).willReturn(Optional.of(user));
        given(authentication.getName()).willReturn("String");
        //given(securityContextHolder.getContext().getAuthentication().getName()).willReturn("String");
        Boolean actual = underTest.delete(authentication,id);
        verify(userRepository).delete(user);
        assertTrue(actual);
    }

    @Test
    public void cannotDeleteSelf() throws Exception{
        Long id = 1L;
        User user = new User(
                "username",
                "password",
                "ADMIN"
        );

        given(userRepository.findById(any())).willReturn(Optional.of(user));
        given(authentication.getName()).willReturn("username");
        assertThatThrownBy(() -> underTest.delete(authentication, id))
                .hasMessage("Cannot delete self.");
        verify(userRepository, never()).delete(any());
    }

    @Test
    public void cannotDeleteNotExistingUser() throws Exception{
        //given
        Long id = 1L;
        //when
        //then
        assertThatThrownBy(() -> underTest.delete(authentication, id))
                .hasMessage("User with id: "+ id + " does not exists.");
        verify(userRepository, never()).delete(any());
    }

    @Test
    public void canUpdateUser() throws Exception{
        //given
        Long id = 1L;
        User user = new User(
                "username",
                "password",
                "ADMIN"
        );
        //when
        given(userRepository.findById(any())).willReturn(Optional.of(user));
        underTest.update(id, user);
        //then
        ArgumentCaptor<User> userArgumentCaptor =
                ArgumentCaptor.forClass(User.class);

        verify(userRepository)
                .save(userArgumentCaptor.capture());

        User capturedUser = userArgumentCaptor.getValue();
        assertThat(capturedUser).isEqualTo(user);

    }

    @Test
    public void cannotUpdateNotExistingUser() throws Exception{
        //given
        Long id = 1L;
        User user = new User(
                "username",
                "password",
                "ADMIN"
        );
        //when
        //then
        assertThatThrownBy(() -> underTest.update(id,user))
                .hasMessage("User with id: "+ id + " does not exists.");
        verify(userRepository, never()).save(any());
    }

}