package com.example.userAuthorisation.service;

import com.example.userAuthorisation.model.Role;
import com.example.userAuthorisation.model.User;
import com.example.userAuthorisation.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser_NewUser_Success() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("testpassword");

        when(userRepository.findByUsername("testuser")).thenReturn(null);
        when(passwordEncoder.encode("testpassword")).thenReturn("encodedpassword");
        when(userRepository.save(user)).thenReturn(user);

        User registeredUser = userService.registerUser(user);

        assertNotNull(registeredUser);
        assertEquals("encodedpassword", registeredUser.getPassword());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testRegisterUser_ExistingUser_Failure() {
        User user = new User();
        user.setUsername("existinguser");

        when(userRepository.findByUsername("existinguser")).thenReturn(new User());

        User registeredUser = userService.registerUser(user);

        assertNull(registeredUser);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testGetAllUsers() {
        User user1 = new User();
        User user2 = new User();

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<User> users = userService.getAllUsers();

        assertEquals(2, users.size());
        assertTrue(users.contains(user1));
        assertTrue(users.contains(user2));
    }

    @Test
    void testAuthenticateUser_ValidCredentials_Success() {
        User user = new User();
        user.setUsername("validuser");
        user.setPassword("encodedpassword");

        when(userRepository.findByUsername("validuser")).thenReturn(user);
        when(passwordEncoder.matches("testpassword", "encodedpassword")).thenReturn(true);

        boolean isAuthenticated = userService.authenticateUser("validuser", "testpassword");

        assertTrue(isAuthenticated);
    }

    @Test
    void testAuthenticateUser_InvalidCredentials_Failure() {
        User user = new User();
        user.setUsername("validuser");
        user.setPassword("encodedpassword");

        when(userRepository.findByUsername("validuser")).thenReturn(user);
        when(passwordEncoder.matches("wrongpassword", "encodedpassword")).thenReturn(false);

        boolean isAuthenticated = userService.authenticateUser("validuser", "wrongpassword");

        assertFalse(isAuthenticated);
    }

    @Test
    void testGetUserRole_ExistingUser_Success() {
        User user = new User();
        user.setUsername("userwithrole");
        Role expectedRole = Role.COACH; // Use the enum constant
        user.setRole(expectedRole);

        when(userRepository.findByUsername("userwithrole")).thenReturn(user);

        Role actualRole = userService.getUserRole("userwithrole");

        assertNotNull(actualRole);
        assertEquals(expectedRole, actualRole); // Directly compare the enum values
    }


    @Test
    void testGetUserRole_NonExistingUser_Failure() {
        when(userRepository.findByUsername("nonexistinguser")).thenReturn(null);

        Role userRole = userService.getUserRole("nonexistinguser");

        assertNull(userRole);
    }
}
