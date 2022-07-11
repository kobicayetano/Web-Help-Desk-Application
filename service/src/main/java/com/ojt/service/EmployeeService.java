package com.ojt.service;

import com.ojt.model.Employee;
import com.ojt.model.Ticket;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public List<Employee> list();

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public Employee view(Long id) throws Exception;

    @PreAuthorize("hasRole('ADMIN')")
    public Employee create(Employee employee) throws Exception;

    @PreAuthorize("hasRole('ADMIN')")
    public Boolean delete(Long employeeId) throws Exception;

    @PreAuthorize("hasRole('ADMIN')")
    public Employee update(Long employeeId, Employee updatedEmployee) throws Exception;

    @PreAuthorize("hasRole('ADMIN')")
    public Ticket assignTicket(Long employeeId, Long ticketId) throws Exception;



}
