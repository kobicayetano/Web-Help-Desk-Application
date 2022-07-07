package com.ojt.persistence;

import com.ojt.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Boolean existsByEmployeeNumber(Long employeeNumber);

    Optional<Employee> findByEmployeeNumber(Long employeeNumber);
}
