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
    void testGetAllUsers_NoContent() {
        when(userService.getAllUsers()).thenReturn(Collections.emptyList());

        ResponseEntity<List<User>> response = userController.getAllUsers();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void testRegisterUser_Success() {
        User user = new User();
        user.setUsername("testuser");

        when(userService.registerUser(user)).thenReturn(user);

        ResponseEntity<User> response = userController.registerUser(user);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("testuser", response.getBody().getUsername());
        verify(userService, times(1)).registerUser(user);
    }

    @Test
    void testRegisterUser_Failure() {
        User user = new User();
        user.setUsername("existinguser");

        when(userService.registerUser(user)).thenReturn(null);

        ResponseEntity<User> response = userController.registerUser(user);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
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

        ResponseEntity<Role> response = userController.loginUser(loginData);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(Role.ADMIN, response.getBody());
    }

    @Test
    void testLoginUser_Failure() {
        Map<String, String> loginData = new HashMap<>();
        loginData.put("username", "testuser");
        loginData.put("password", "wrongpassword");

        when(userService.authenticateUser("testuser", "wrongpassword")).thenReturn(false);

        ResponseEntity<Role> response = userController.loginUser(loginData);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService, never()).getUserRole("testuser");
    }

    @Test
    void testGetUserRole_Success() {
        Map<String, String> requestData = new HashMap<>();
        requestData.put("username", "testuser");

        Role role = Role.ADMIN;

        when(userService.getUserRole("testuser")).thenReturn(role);

        ResponseEntity<Role> response = userController.getUserRole(requestData);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(Role.ADMIN, response.getBody());
    }

    @Test
    void testGetUserRole_Failure() {
        Map<String, String> requestData = new HashMap<>();
        requestData.put("username", "nonexistinguser");

        when(userService.getUserRole("nonexistinguser")).thenReturn(null);

        ResponseEntity<Role> response = userController.getUserRole(requestData);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }
}
