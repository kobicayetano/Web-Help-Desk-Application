package com.ojt.controller;


import com.ojt.model.Employee;
import com.ojt.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/helpdesk/employees")
public class EmployeeController {

    private final EmployeeService employeeService;


    @Autowired
    public EmployeeController(EmployeeService employeeService){
        this.employeeService = employeeService;
    }


     @GetMapping("/all")
     public List<Employee> getAllEmployees(){
     return employeeService.list();
     }

    @GetMapping("/{employeeId}")
    public Optional<Employee> getEmployeeById(@PathVariable("employeeId") Long employeeId) throws Exception{
        return employeeService.view(employeeId);
    }

    @Transactional
    @PostMapping("/add")
    public String createEmployee(@RequestBody Employee employee) throws Exception{
        employeeService.create(employee);
        return "Employee "+ employee.getId() + " added.";
    }

    @Transactional
    @DeleteMapping ("/delete/{id}")
    public Map<String, Boolean> deleteEmployee(@PathVariable("id") Long id) throws Exception{
        employeeService.delete(id);
        Map<String, Boolean> response = new HashMap<>();
        response.put("--Employee Deleted--", Boolean.TRUE);
        return response;
    }

    @Transactional
    @PutMapping("/update/{id}")
    public Map<String, Boolean> updateEmployee(@PathVariable("id") Long id,
                                               @RequestBody Employee updatedEmployee) throws Exception{
        employeeService.update(id, updatedEmployee);
        Map<String, Boolean> response = new HashMap<>();
        response.put("--Employee Updated--", Boolean.TRUE);
        return response;
    }

    @Transactional
    @PutMapping("/{employeeId}/assign/{ticketId}")
    public Map<String, Boolean> assignTicket(@PathVariable("employeeId") Long employeeId,
                                               @PathVariable("ticketId") Long ticketId) throws Exception{
        employeeService.assignTicket(employeeId, ticketId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("--Ticket assigned to employee--", Boolean.TRUE);
        return response;
    }






}
