package com.ojt.service;

import com.ojt.model.Employee;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public List<Employee> list();

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public Optional<Employee> view(Long id) throws Exception;

    @PreAuthorize("hasRole('ADMIN')")
    public void create(Employee employee) throws Exception;

    @PreAuthorize("hasRole('ADMIN')")
    public void delete(Long employeeId) throws Exception;

    @PreAuthorize("hasRole('ADMIN')")
    public void update(Long employeeId, Employee updatedEmployee) throws Exception;

    @PreAuthorize("hasRole('ADMIN')")
    public void assignTicket(Long employeeId, Long ticketId) throws Exception;



}
