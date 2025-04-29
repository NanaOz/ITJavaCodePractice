package app.javacode.controller;

import app.javacode.model.Order;
import app.javacode.model.Views;
import app.javacode.repository.OrderRepository;
import app.javacode.repository.UserRepository;
import com.fasterxml.jackson.annotation.JsonView;
import org.aspectj.weaver.ast.Or;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

public class OrderController {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public OrderController(OrderRepository orderRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/user/{userId}")
    @JsonView(Views.OrderSummary.class)
    public ResponseEntity<List<Order>> getUserOrders(@PathVariable Long userId) {
        if(!userRepository.existsById(userId)) {
            return ResponseEntity.notFound().build();
        }

        List<Order> orders = orderRepository.findByUserId(userId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    @JsonView(Views.OrderDetails.class)
    public ResponseEntity<Order> getOrderById (@PathVariable Long id) {
        Optional<Order> optionalOrder = orderRepository.findById(id);

        if (optionalOrder.isPresent()) {
            return ResponseEntity.ok(optionalOrder.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
