package com.ojt.service;

import com.ojt.model.Employee;
import com.ojt.model.Ticket;
import com.ojt.persistence.EmployeeRepository;
import com.ojt.persistence.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class TicketServiceImpl implements TicketService{

    private final EmployeeRepository employeeRepository;
    private final TicketRepository ticketRepository;

    @Autowired
    public TicketServiceImpl(EmployeeRepository employeeRepository, TicketRepository ticketRepository){
        this.ticketRepository = ticketRepository;
        this.employeeRepository = employeeRepository;
    }
    @Override
    public List<Ticket> list() {return ticketRepository.findAll();}

    @Override
    public Ticket view(Long id) throws Exception{
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(()->new  Exception ("Ticket with id: "+ id + " does not exist."));
        return ticket;
    }
    @Override
    @Transactional
    public Ticket create(Ticket ticket) throws Exception{
        return ticketRepository.save(ticket);
    }
    @Override
    @Transactional
    public Ticket update(Long ticketId, Ticket updatedTicket) throws Exception{
        Ticket ticket = ticketRepository.findById(ticketId)
                        .orElseThrow(()->new  Exception ("Ticket with id: "+ ticketId + " does not exist."));
        ticket.setDescription(updatedTicket.getDescription());
        ticket.setSeverity(updatedTicket.getSeverity());
        ticket.setStatus(updatedTicket.getStatus());
        ticket.setTitle(updatedTicket.getTitle());
        return ticketRepository.save(ticket);
    }

    @Override
    @Transactional
    public Boolean delete(Long ticketId) throws Exception{
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(()-> new Exception("Ticket with id: "+ ticketId + " does not exist."));
        //delete associated rows in ticket_watchers
        if(ticket.getWatchers().size()>0){
            ticket.removeAllWatchers();
        }
        //delete assignee in table so employee will not get deleted
        if(ticket.getAssignee()!=null){
            ticket.getAssignee().removeAssignedTicket(ticket);
        }
        ticketRepository.delete(ticket);
        return true;
    }

    @Override
    @Transactional
    public Ticket addWatcher(Long ticketId, Long employeeId) throws Exception{
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(()-> new Exception("Ticket with id: "+ ticketId + " does not exist."));
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(()-> new Exception("Employee with id: "+ employeeId + " does not exist."));
        if(ticket.getWatchers().contains(employee)){
           throw new Exception("Employee with id: "+ employeeId + " is already listed as watcher.");
        }
        ticket.addWatcher(employee);
        return ticketRepository.save(ticket);
    }

    @Override
    @Transactional
    public Ticket removeWatcher(Long ticketId, Long employeeId) throws Exception{
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(()-> new Exception("Ticket with id: "+ ticketId + " does not exist."));
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(()-> new Exception("Employee with id: "+ employeeId + " does not exist."));
        if(ticket.getWatchers().contains(employee)){
            ticket.removeWatcher(employee);
            ticketRepository.save(ticket);
        }else{
            throw new Exception("Employee with id: "+employeeId+" is not a watcher.");
        }
        return ticket;
    }

    @Override
    public Set<Ticket> viewTicketsAssignedToEmployeeNumber(long employeeNumber) throws Exception{
        Employee employee = employeeRepository.findByEmployeeNumber(employeeNumber)
                .orElseThrow(()-> new Exception("Employee with employee number: "+ employeeNumber + " does not exist."));
        return employee.getTicketAssigned();
    }

    @Override
    public Set<Ticket> viewTicketsWatchedByEmployeeNumber(long employeeNumber) throws Exception{
        Employee employee = employeeRepository.findByEmployeeNumber(employeeNumber)
                .orElseThrow(()-> new Exception("Employee with employee number: "+ employeeNumber + " does not exist."));
        return employee.getTicketWatched();
    }



}
