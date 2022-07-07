package com.ojt.controller;


import com.ojt.model.Employee;
import com.ojt.service.EmployeeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    public Optional<Employee> getEmployeeById(@PathVariable("employeeId") Long employeeId) throws Exception{
        return employeeServiceImpl.view(employeeId);
    }

    @Transactional
    @PostMapping("manage/employee/add")
    public String createEmployee(@RequestBody Employee employee) throws Exception{
        employeeServiceImpl.create(employee);
        return "Employee "+ employee.getId() + " added.";
    }

    @Transactional
    @DeleteMapping ("manage/employee/delete/{id}")
    public Map<String, Boolean> deleteEmployee(@PathVariable("id") Long id) throws Exception{
        employeeServiceImpl.delete(id);
        Map<String, Boolean> response = new HashMap<>();
        response.put("--Employee Deleted--", Boolean.TRUE);
        return response;
    }

    @Transactional
    @PutMapping("manage/employee/update/{id}")
    public Map<String, Boolean> updateEmployee(@PathVariable("id") Long id,
                                               @RequestBody Employee updatedEmployee) throws Exception{
        employeeServiceImpl.update(id, updatedEmployee);
        Map<String, Boolean> response = new HashMap<>();
        response.put("--Employee Updated--", Boolean.TRUE);
        return response;
    }

    @Transactional
    @PutMapping("manage/employee/{employeeId}/assignTicket/{ticketId}")
    public Map<String, Boolean> assignTicket(@PathVariable("employeeId") Long employeeId,
                                               @PathVariable("ticketId") Long ticketId) throws Exception{
        employeeServiceImpl.assignTicket(employeeId, ticketId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("--Ticket assigned to employee--", Boolean.TRUE);
        return response;
    }






}
