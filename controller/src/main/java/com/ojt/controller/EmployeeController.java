package com.ojt.controller;


import com.ojt.model.Employee;
import com.ojt.model.Ticket;
import com.ojt.service.EmployeeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
     public ResponseEntity<List<Employee>> getAllEmployees(){
     return new ResponseEntity<>(employeeServiceImpl.list(), HttpStatus.OK);
     }

    @GetMapping("employees/{employeeId}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable("employeeId") Long employeeId) throws Exception{
        return new ResponseEntity<>(employeeServiceImpl.view(employeeId), HttpStatus.OK);
    }

    @Transactional
    @PostMapping("manage/employee/add")
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) throws Exception{
        return new ResponseEntity<>(employeeServiceImpl.create(employee), HttpStatus.CREATED);
    }

    @Transactional
    @DeleteMapping ("manage/employee/delete/{id}")
    public ResponseEntity<Boolean> deleteEmployee(@PathVariable("id") Long id) throws Exception{
        return new ResponseEntity<>(employeeServiceImpl.delete(id), HttpStatus.NO_CONTENT);
    }

    @Transactional
    @PutMapping("manage/employee/update/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable("id") Long id,
                                               @RequestBody Employee updatedEmployee) throws Exception{
        return new ResponseEntity<>(employeeServiceImpl.update(id, updatedEmployee), HttpStatus.OK);

    }

    @Transactional
    @PutMapping("manage/employee/{employeeId}/assignTicket/{ticketId}")
    public ResponseEntity<Ticket> assignTicket(@PathVariable("employeeId") Long employeeId,
                               @PathVariable("ticketId") Long ticketId) throws Exception{
        return new ResponseEntity<>(employeeServiceImpl.assignTicket(employeeId, ticketId), HttpStatus.OK);
    }






}
