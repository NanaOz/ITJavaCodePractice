package app.javacode.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import app.javacode.model.User;
import app.javacode.repository.OrderRepository;
import app.javacode.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;


public class UserControllerTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;
    private ObjectMapper mapper;
    private User user;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        mapper = new ObjectMapper();

        user = new User();
        user.setId(1L);
        user.setName("Anna A");
        user.setEmail("anna@example.com");
    }

    @Test
    public void getAllUsersShowUserSummary() throws Exception {
        when(userRepository.findAll()).thenReturn(Arrays.asList(user));

        mockMvc.perform(get("/api/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Anna A"))
                .andExpect(jsonPath("$[0].email").value("anna@example.com"));
    }

    @Test
    public void getUserByIdShouldShowUserDetails() throws Exception {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/users/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Anna A"))
                .andExpect(jsonPath("$.email").value("anna@example.com"));
    }

    @Test
    public void getUserByIdUserNotFounds() throws Exception {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createUserShouldReturnCreatedUser() throws Exception {
        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setName("Anna A");
        savedUser.setEmail("anna@example.com");

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/users/" + user.getId()))
                .andExpect(jsonPath("$.name").value("Anna A"));
    }

    @Test
    public void updateUserShouldReturnUpdatedUser() throws Exception {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        User updatedUser = new User();
        updatedUser.setName("Anna B");
        updatedUser.setEmail("annab@example.com");

        mockMvc.perform(put("/api/users/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Anna B"));
    }

    @Test
    public void updateUserUserNotFound() throws Exception {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        User updatedUser = new User();
        updatedUser.setName("Anna B");
        updatedUser.setEmail("annab@example.com");

        mockMvc.perform(put("/api/users" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updatedUser)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteUserShouldReturnDeletedUser() throws Exception {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(delete("/api/users/" + user.getId()))
                .andExpect(status().isNoContent());

        verify(userRepository, times(1)).delete(user);
    }

    @Test
    public void deleteUserUserNotFound() throws Exception {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/users/" + user.getId()))
                .andExpect(status().isNotFound());
    }
}
