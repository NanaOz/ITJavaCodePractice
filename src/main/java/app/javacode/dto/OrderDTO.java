package app.javacode.dto;

import app.javacode.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long orderId;
    private Long customerId;
    private List<Long> productIds;
    private LocalDate orderDate;
    private String shippingAddress;
    private int totalPrice;
    private OrderStatus orderStatus;
}
