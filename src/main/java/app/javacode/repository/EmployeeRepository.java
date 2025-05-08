package app.javacode.repository;

import app.javacode.model.Employee;
import app.javacode.projection.EmployeeProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query("SELECT e.firstName || ' ' || e.lastName as fullName, e.position as position, " +
            "e.department.name as departmentName FROM Employee e")
    List<EmployeeProjection> findAllProjectedBy();
}
