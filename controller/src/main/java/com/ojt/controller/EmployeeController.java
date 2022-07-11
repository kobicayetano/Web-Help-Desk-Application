package com.ojt.controller;


import com.ojt.model.Employee;
import com.ojt.model.Ticket;
import com.ojt.service.EmployeeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/helpdesk/")
public class EmployeeController {

    private final EmployeeServiceImpl employeeServiceImpl;


    @Autowired
    public EmployeeController(EmployeeServiceImpl employeeServiceImpl){
        this.employeeServiceImpl = employeeServiceImpl;
    }


     @GetMapping("employees/all")
     public List<Employee> getAllEmployees(){
     return employeeServiceImpl.list();
     }

    @GetMapping("employees/{employeeId}")
    public Employee getEmployeeById(@PathVariable("employeeId") Long employeeId) throws Exception{
        return employeeServiceImpl.view(employeeId);
    }

    @Transactional
    @PostMapping("manage/employee/add")
    public Employee createEmployee(@RequestBody Employee employee) throws Exception{
        return employeeServiceImpl.create(employee);
    }

    @Transactional
    @DeleteMapping ("manage/employee/delete/{id}")
    public Boolean deleteEmployee(@PathVariable("id") Long id) throws Exception{
        return employeeServiceImpl.delete(id);
    }

    @Transactional
    @PutMapping("manage/employee/update/{id}")
    public Employee updateEmployee(@PathVariable("id") Long id,
                                               @RequestBody Employee updatedEmployee) throws Exception{
        return employeeServiceImpl.update(id, updatedEmployee);

    }

    @Transactional
    @PutMapping("manage/employee/{employeeId}/assignTicket/{ticketId}")
    public Ticket assignTicket(@PathVariable("employeeId") Long employeeId,
                               @PathVariable("ticketId") Long ticketId) throws Exception{
        return employeeServiceImpl.assignTicket(employeeId, ticketId);
    }






}
