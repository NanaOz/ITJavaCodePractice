package app.javacode.controller;

import app.javacode.model.Order;
import app.javacode.model.Views;
import app.javacode.repository.OrderRepository;
import app.javacode.repository.UserRepository;
import com.fasterxml.jackson.annotation.JsonView;
import org.aspectj.weaver.ast.Or;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
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

    @PostMapping
    public ResponseEntity<Order> createOrder (@RequestBody Order order) {
        if (order.getUser() == null || !userRepository.existsById(order.getUser().getId())) {
            return ResponseEntity.badRequest().build();
        }

        Order savedOrder = orderRepository.save(order);
        return ResponseEntity.created(URI.create("/api/orders/" + savedOrder.getId())).body(savedOrder);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody Order order) {
        Optional<Order> optionalOrder = orderRepository.findById(id);

        if (optionalOrder.isPresent()) {
            Order orderToUpdate = optionalOrder.get();
            orderToUpdate.setTotalAmount(order.getTotalAmount());
            orderToUpdate.setStatus(order.getStatus());
            Order savedOrder = orderRepository.save(orderToUpdate);
            return ResponseEntity.ok(savedOrder);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Order> deleteOrder(@PathVariable Long id) {
        Optional<Order> optionalOrder = orderRepository.findById(id);

        if (optionalOrder.isPresent()) {
            orderRepository.delete(optionalOrder.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
