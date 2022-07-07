package com.ojt.service;

import com.ojt.model.Employee;
import com.ojt.model.Ticket;
import com.ojt.persistence.EmployeeRepository;
import com.ojt.persistence.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TicketService {

    private final EmployeeRepository employeeRepository;
    private final TicketRepository ticketRepository;

    @Autowired
    public TicketService(EmployeeRepository employeeRepository, TicketRepository ticketRepository){
        this.ticketRepository = ticketRepository;
        this.employeeRepository = employeeRepository;
    }

    public List<Ticket> list() {return ticketRepository.findAll();}

    public Optional<Ticket> view(Long id) throws Exception{
        if(ticketRepository.existsById(id)){
            return ticketRepository.findById(id);
        }else{
            throw new Exception("com.ojt.model.Ticket with id: "+id+" does not exist.");
        }
    }

    @Transactional
    public void create(Ticket ticket) throws Exception{
        ticketRepository.save(ticket);
    }

    @Transactional
    public void update(Long ticketId, Ticket updatedTicket) throws Exception{
        Ticket ticket = ticketRepository.findById(ticketId)
                        .orElseThrow(()->new  Exception ("com.ojt.model.Ticket with id: "+ ticketId + " does not exists."));
        ticket.setDescription(updatedTicket.getDescription());
        ticket.setSeverity(updatedTicket.getSeverity());
        ticket.setStatus(updatedTicket.getStatus());
        ticket.setTitle(updatedTicket.getTitle());
        ticketRepository.save(ticket);
    }

    @Transactional
    public void delete(Long ticketId) throws Exception{
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(()-> new Exception("com.ojt.model.Ticket with id: "+ ticketId + " does not exists."));
        //delete associated rows in ticket_watchers
        if(ticket.getWatchers().size()>0){
            ticket.removeAllWatchers();
        }
        //delete assignee in table so employee will not get deleted
        if(ticket.getAssignee()!=null){
            ticket.getAssignee().removeAssignedTicket(ticket);
        }
        ticketRepository.delete(ticket);
    }

    @Transactional
    public void addWatcher(Long ticketId, Long employeeId) throws Exception{
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(()-> new Exception("com.ojt.model.Ticket with id: "+ ticketId + " does not exists."));
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(()-> new Exception("com.ojt.model.Employee with id: "+ employeeId + " does not exists."));
        if(ticket.getWatchers().contains(employee)){
           throw new Exception("com.ojt.model.Employee with id: "+ employeeId + " is already listed as watcher.");
        }
        ticket.addWatcher(employee);
        ticketRepository.save(ticket);
    }

    @Transactional
    public void removeWatcher(Long ticketId, Long employeeId) throws Exception{
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(()-> new Exception("com.ojt.model.Ticket with id: "+ ticketId + " does not exists."));
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(()-> new Exception("com.ojt.model.Employee with id: "+ employeeId + " does not exists."));
        if(ticket.getWatchers().contains(employee)){
            ticket.removeWatcher(employee);
            ticketRepository.save(ticket);
        }else{
            throw new Exception("com.ojt.model.Employee with id: "+employeeId+" is not a watcher.");
        }
    }

    public Set<Ticket> viewTicketsAssignedToEmployeeNumber(long employeeNumber) throws Exception{
        Employee employee = employeeRepository.findByEmployeeNumber(employeeNumber)
                .orElseThrow(()-> new Exception("com.ojt.model.Employee with employee number: "+ employeeNumber + " does not exists."));
        return employee.getTicketAssigned();
    }

    public Set<Ticket> viewTicketsWatchedByEmlployeeNumber(long employeeNumber) throws Exception{
        Employee employee = employeeRepository.findByEmployeeNumber(employeeNumber)
                .orElseThrow(()-> new Exception("com.ojt.model.Employee with employee number: "+ employeeNumber + " does not exists."));
        return employee.getTicketWatched();
    }



}
