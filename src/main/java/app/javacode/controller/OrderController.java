package app.javacode.controller;

import app.javacode.dto.OrderDTO;
import app.javacode.model.Customer;
import app.javacode.model.Order;
import app.javacode.model.Product;
import app.javacode.repository.CustomerRepository;
import app.javacode.repository.OrderRepository;
import app.javacode.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable Long id) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            OrderDTO orderDTO = convertToDTO(order);
            return ResponseEntity.ok(orderDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO) {
        Customer customer = customerRepository.findById(orderDTO.getCustomerId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer not found"));

        List<Product> products = productRepository.findAllById(orderDTO.getProductIds());
        if (products.size() != orderDTO.getProductIds().size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Some products not found");
        }

        Order order = new Order();
        order.setCustomer(customer);
        order.setProducts(products);
        order.setShippingAddress(orderDTO.getShippingAddress());
        order.setTotalPrice(orderDTO.getTotalPrice());
        order.setOrderStatus(orderDTO.getOrderStatus());
        order.setOrderDate(LocalDate.now());

        Order savedOrder = orderRepository.save(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(savedOrder));
    }

    private OrderDTO convertToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setOrderId(order.getOrderId());
        dto.setCustomerId(order.getCustomer().getCustomerId());
        dto.setOrderDate(order.getOrderDate());
        dto.setShippingAddress(order.getShippingAddress());
        dto.setTotalPrice(order.getTotalPrice());
        dto.setOrderStatus(order.getOrderStatus());

        List<Long> productIds = order.getProducts().stream()
                .map(Product::getProductId)
                .collect(Collectors.toList());
        dto.setProductIds(productIds);

        return dto;
    }
}
