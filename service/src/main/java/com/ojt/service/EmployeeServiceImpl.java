package com.ojt.service;

import com.ojt.model.Employee;
import com.ojt.model.Ticket;
import com.ojt.persistence.EmployeeRepository;
import com.ojt.persistence.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final TicketRepository ticketRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, TicketRepository ticketRepository){
        this.ticketRepository = ticketRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public List<Employee> list(){
        return employeeRepository.findAll();
    }

    @Override
    public Optional<Employee> view(Long id) throws Exception{
        if(employeeRepository.existsById(id)){
            return employeeRepository.findById(id);
        }else{
            throw new Exception("com.ojt.model.Employee with id: "+id+" does not exist.");
        }
    }
    @Override
    public void create(Employee employee) throws Exception {
        if(employeeRepository.existsByEmployeeNumber(employee.getEmployeeNumber())){
            throw new Exception("com.ojt.model.Employee Number: " + employee.getEmployeeNumber() + " already exists.");
        }else{
            employeeRepository.save(employee);
        }
    }
    @Override
    public void delete(Long employeeId) throws Exception{
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(()-> new Exception("com.ojt.model.Employee with id: "+ employeeId + " does not exists."));

        if(employee.getTicketAssigned().size()>0){
            throw new Exception("Cannot delete employee with assigned ticket.");
        }else{
            //delete associated rows in ticket_watchers
            employee.removeAllWatchedTickets();
            employeeRepository.delete(employee);
        }
    }
    @Override
    public void update(Long employeeId, Employee updatedEmployee) throws Exception{
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(()-> new Exception("com.ojt.model.Employee with id: "+ employeeId + " does not exists."));
        if(employeeRepository.existsById(updatedEmployee.getEmployeeNumber())){
            throw new Exception("com.ojt.model.Employee with employee number: "+ updatedEmployee.getEmployeeNumber() + " already exists.");
        }
        employee.setEmployeeNumber(updatedEmployee.getEmployeeNumber());
        employee.setFirstName(updatedEmployee.getFirstName());
        employee.setMiddleName(updatedEmployee.getMiddleName());
        employee.setLastName(updatedEmployee.getLastName());
        employee.setDepartment(updatedEmployee.getDepartment());
        employeeRepository.save(employee);
    }
    @Override
    public void assignTicket(Long employeeId, Long ticketId) throws Exception{
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(()-> new Exception("com.ojt.model.Employee with id: "+ employeeId + " does not exists."));
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(()-> new Exception("com.ojt.model.Ticket with id: "+ ticketId + " does not exists."));

        if(ticket.getAssignee()!=null){
            throw new Exception("com.ojt.model.Ticket with id: "+ ticketId + " is already assigned to a user.");
        }else{
            ticket.setAssignee(employee);
            ticketRepository.save(ticket);
        }
    }




}
