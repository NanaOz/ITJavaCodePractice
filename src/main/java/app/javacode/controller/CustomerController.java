package app.javacode.controller;

import app.javacode.dto.CustomerDTO;
import app.javacode.model.Customer;
import app.javacode.repository.CustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    private final CustomerRepository customerRepository;
    private final ObjectMapper objectMapper;

    public CustomerController(CustomerRepository customerRepository, ObjectMapper objectMapper) {
        this.customerRepository = customerRepository;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustumers() {
        List<Customer> customers = customerRepository.findAll();
        List<CustomerDTO> customerDTOList = new ArrayList<>();

        for (Customer customer : customers) {
            CustomerDTO customerDTO = objectMapper.convertValue(customer, CustomerDTO.class);
            customerDTOList.add(customerDTO);
        }
        return ResponseEntity.ok(customerDTOList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustumerById(@PathVariable long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        if (customer.isPresent()) {
            Customer customerEntity = customer.get();
            CustomerDTO customerDTO = objectMapper.convertValue(customerEntity, CustomerDTO.class);
            return ResponseEntity.ok(customerDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CustomerDTO customerDTO) {
        if (customerDTO.getEmail() == null || customerDTO.getContactNumber() == null) {
            return ResponseEntity.badRequest().build();
        }
        Customer customer = objectMapper.convertValue(customerDTO, Customer.class);
        Customer savedCustomer = customerRepository.save(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(objectMapper.convertValue(savedCustomer, CustomerDTO.class));
    }
}
