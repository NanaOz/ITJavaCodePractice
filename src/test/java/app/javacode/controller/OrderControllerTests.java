package app.javacode.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import app.javacode.model.Order;
import app.javacode.model.OrderStatus;
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

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

public class OrderControllerTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderController orderController;

    private MockMvc mockMvc;
    private ObjectMapper mapper;
    private Order order;
    private User user;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
        mapper = new ObjectMapper();

        user = new User();
        user.setId(1L);
        user.setName("Anna A");
        user.setEmail("anna@example.com");

        order = new Order();
        order.setId(1L);
        order.setUser(user);
        order.setTotalAmount(new BigDecimal("100.00"));
        order.setStatus(OrderStatus.PROCESSING);
    }

    @Test
    public void getOrdersShouldReturnOrderSummary() throws Exception {
        when(userRepository.existsById(1L)).thenReturn(true);
        when(orderRepository.findByUserId(1L)).thenReturn(Arrays.asList(order));

        mockMvc.perform(get("/api/orders/user/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(order.getId()))
                .andExpect(jsonPath("$[0].totalAmount").value("100.0"))
                .andExpect(jsonPath("$[0].status").value(OrderStatus.PROCESSING.toString()))
                .andExpect(jsonPath("$[0].user").doesNotExist());
    }

    @Test
    public void getOrderByIdShouldOrderDetails() throws Exception {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        mockMvc.perform(get("/api/orders/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(order.getId()))
                .andExpect(jsonPath("$.totalAmount").value("100.0"))
                .andExpect(jsonPath("$.status").value(OrderStatus.PROCESSING.toString()))
                .andExpect(jsonPath("$.user.id").value(user.getId()));
    }

    @Test
    public void getOrderByIdOrderNotFound() throws Exception {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/orders/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createOrderInvalidUserShouldReturnBadRequest() throws Exception {
        order.setUser(null);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(order)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateOrderShouldUpdatedOrder() throws Exception {
        Order existingOrder = new Order();
        existingOrder.setId(1L);
        existingOrder.setUser(user);
        existingOrder.setTotalAmount(new BigDecimal("100.0"));
        existingOrder.setStatus(OrderStatus.PROCESSING);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Order updatedOrder = new Order();
        updatedOrder.setTotalAmount(new BigDecimal("150.0"));
        updatedOrder.setStatus(OrderStatus.CANCELLED);

        mockMvc.perform(put("/api/orders/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updatedOrder)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalAmount").value("150.0"))
                .andExpect(jsonPath("$.status").value(OrderStatus.CANCELLED.toString()));
    }

    @Test
    public void updateOrderOrderNotFoundShouldReturnNotFound() throws Exception {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        Order updatedOrder = new Order();
        updatedOrder.setTotalAmount(new BigDecimal("150.00"));
        updatedOrder.setStatus(OrderStatus.SHIPPED);

        mockMvc.perform(put("/api/orders/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updatedOrder)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteOrderShouldNoContent() throws Exception {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        mockMvc.perform(delete("/api/orders/1"))
                .andExpect(status().isNoContent());

        verify(orderRepository, times(1)).delete(order);
    }

    @Test
    public void deleteOrderOrderNotFoundShouldNotFound() throws Exception {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/orders/1"))
                .andExpect(status().isNotFound());
    }
}
