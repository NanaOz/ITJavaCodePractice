package app.javacode.controller;

import app.javacode.dto.OrderDTO;
import app.javacode.model.Order;
import app.javacode.model.Product;
import app.javacode.repository.CustomerRepository;
import app.javacode.repository.OrderRepository;
import app.javacode.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;

    public OrderController(OrderRepository orderRepository, CustomerRepository customerRepository, ProductRepository productRepository, ObjectMapper objectMapper) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getOrders() {
        List<Order> orders = orderRepository.findAll();
        List<OrderDTO> orderDTOs = new ArrayList<>();

        for (Order order : orders) {
            OrderDTO orderDTO = objectMapper.convertValue(order, OrderDTO.class);
            orderDTOs.add(orderDTO);
        }

        return new ResponseEntity<>(orderDTOs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable Long id) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            OrderDTO orderDTO = objectMapper.convertValue(order, OrderDTO.class);
            return ResponseEntity.ok(orderDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO) {
        if(!customerRepository.existsById(orderDTO.getCustomer().getCustomerId())){
            return ResponseEntity.notFound().build();
        }

        for (Product product : orderDTO.getProducts()) {
            if(!productRepository.existsById(product.getProductId())){
                return ResponseEntity.notFound().build();
            }
        }

        Order order = objectMapper.convertValue(orderDTO, Order.class);
        order.setOrderDate(LocalDate.now());
        Order savedOrder = orderRepository.save(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(objectMapper.convertValue(savedOrder, OrderDTO.class));
    }
}
