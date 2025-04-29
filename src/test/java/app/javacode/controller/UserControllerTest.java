package app.javacode.controller;

//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//
//import app.javacode.model.User;
//import app.javacode.repository.OrderRepository;
//import app.javacode.repository.UserRepository;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import java.util.Arrays;
//import java.util.Optional;


public class UserControllerTest {
//
//    private MockMvc mockMvc;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private OrderRepository orderRepository;
//
//    @InjectMocks
//    private UserController userController;
//
//    @BeforeEach
//    public void setup() {
//        MockitoAnnotations.openMocks(this);
//        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
//    }
//
//    @Test
//    public void testGetAllUsers_WithJsonView() throws Exception {
//        User user1 = new User();
//        user1.setId(1L);
//        user1.setName("John Doe");
//        user1.setEmail("john@example.com");
//        User user2 = new User();
//        user1.setId(2L);
//        user1.setName("Bella M");
//        user1.setEmail("bella@example.com");
//
//        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));
//
//        mockMvc.perform(get("/api/users")
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$[0].name").value("John Doe"))
//                .andExpect(jsonPath("$[0].email").doesNotExist()) // в UserSummary не должно быть email
//                .andExpect(jsonPath("$[1].name").value("Jane Doe"))
//                .andExpect(jsonPath("$[1].email").doesNotExist());
//    }


}
