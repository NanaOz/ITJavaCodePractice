package app.javacode.service;

import app.javacode.model.Employee;
import app.javacode.projection.EmployeeProjection;
import app.javacode.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id).orElse(null);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(Employee employee, Long id) {
        Employee employeeToUpdate = employeeRepository.findById(id).orElse(null);
        if (employeeToUpdate != null) {
            employeeToUpdate.setFirstName(employee.getFirstName());
            employeeToUpdate.setLastName(employee.getLastName());
            employeeToUpdate.setPosition(employee.getPosition());
            employeeToUpdate.setSalary(employee.getSalary());
            employeeToUpdate.setDepartment(employee.getDepartment());
            return employeeRepository.save(employeeToUpdate);
        }
        return null;
    }

    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

    public List<EmployeeProjection> getAllEmployeesProjection() {
        return employeeRepository.findAllProjectedBy();
    }
}
