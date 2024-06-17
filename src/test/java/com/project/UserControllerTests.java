package com.project;

import static org.hamcrest.CoreMatchers.any;  
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.user_man_app.controller.UserController;
import com.project.user_man_app.exceptions.UserNotFoundException;
import com.project.user_man_app.model.User;
import com.project.user_man_app.service.UserService;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testRegisterUser_Success() throws Exception {
        User newUser = new User(1,"testuser", "testuser@example.com", "password");

        // Mocking behavior of userService.registerUser
        when(userService.registerUser((User) any(User.class))).thenReturn(newUser);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(newUser)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("testuser"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("testuser@example.com"));
        
        // Verify that userService.registerUser was called once with newUser
        verify(userService, times(1)).registerUser(newUser);
    }
    @Before
    public void setup() throws UserNotFoundException {
        // Example of mocking a specific behavior of userService
        Mockito.when(userService.fetchUserByUsername("nonexistinguser")).thenReturn(null);
    }
    @Test
    public void testFetchUser_Success() throws Exception {
        User user = new User(1,"testuser", "test@example.com", "password123");
        Mockito.when(userService.fetchUserByUsername("testuser")).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/fetch")
                .param("username", "testuser"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", Matchers.is("testuser")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", Matchers.is("test@example.com")));
    }
    @Test
    public void testFetchUser_NotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/fetch")
                .param("username", "nonexistinguser"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    // Utility method to convert object to JSON string
    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
