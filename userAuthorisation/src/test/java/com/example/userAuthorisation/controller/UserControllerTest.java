package com.example.userAuthorisation.controller;

import com.example.userAuthorisation.model.Role;
import com.example.userAuthorisation.model.User;
import com.example.userAuthorisation.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void testGetAllUsers() {
        User user1 = new User();
        User user2 = new User();
        List<User> userList = Arrays.asList(user1, user2);

        when(userService.getAllUsers()).thenReturn(userList);

        ResponseEntity<List<User>> response = userController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, Objects.requireNonNull(response.getBody()).size());
        assertTrue(response.getBody().contains(user1));
        assertTrue(response.getBody().contains(user2));
    }

    @Test
    void testRegisterUser_Success() {
        User user = new User();
        user.setUsername("testuser");

        when(userService.registerUser(user)).thenReturn(user);

        User registeredUser = userController.registerUser(user);

        assertNotNull(registeredUser);
        assertEquals("testuser", registeredUser.getUsername());
        verify(userService, times(1)).registerUser(user);
    }

    @Test
    void testRegisterUser_Failure() {
        User user = new User();
        user.setUsername("existinguser");

        when(userService.registerUser(user)).thenReturn(null);

        User registeredUser = userController.registerUser(user);

        assertNull(registeredUser);
        verify(userService, times(1)).registerUser(user);
    }

    @Test
    void testLoginUser_Success() {
        Map<String, String> loginData = new HashMap<>();
        loginData.put("username", "testuser");
        loginData.put("password", "testpassword");

        Role role = Role.ADMIN;

        when(userService.authenticateUser("testuser", "testpassword")).thenReturn(true);
        when(userService.getUserRole("testuser")).thenReturn(role);

        Role returnedRole = userController.loginUser(loginData);

        assertNotNull(returnedRole);
        assertEquals(Role.ADMIN, returnedRole);
    }

    @Test
    void testLoginUser_Failure() {
        Map<String, String> loginData = new HashMap<>();
        loginData.put("username", "testuser");
        loginData.put("password", "wrongpassword");

        when(userService.authenticateUser("testuser", "wrongpassword")).thenReturn(false);

        Role returnedRole = userController.loginUser(loginData);

        assertNull(returnedRole);
        verify(userService, never()).getUserRole("testuser");
    }

    @Test
    void testGetUserRole_Success() {
        Map<String, String> requestData = new HashMap<>();
        requestData.put("username", "testuser");

        Role role = Role.ADMIN;

        when(userService.getUserRole("testuser")).thenReturn(role);

        Role returnedRole = userController.getUserRole(requestData);

        assertNotNull(returnedRole);
        assertEquals(Role.ADMIN, returnedRole);
    }

    @Test
    void testGetUserRole_Failure() {
        Map<String, String> requestData = new HashMap<>();
        requestData.put("username", "nonexistinguser");

        when(userService.getUserRole("nonexistinguser")).thenReturn(null);

        Role returnedRole = userController.getUserRole(requestData);

        assertNull(returnedRole);
    }
}