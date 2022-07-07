package com.ojt.controller;

import com.ojt.model.Ticket;
import com.ojt.service.TicketServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("helpdesk/")
public class TicketController {

    private final TicketServiceImpl ticketServiceImpl;

    @Autowired
    public TicketController(TicketServiceImpl ticketServiceImpl){
        this.ticketServiceImpl = ticketServiceImpl;
    }

    @GetMapping("tickets/all")
    public List<Ticket> getAllTickets(){return ticketServiceImpl.list();}

    @GetMapping("tickets/{ticketId}")
    public Optional<Ticket> getTicketById(@PathVariable("ticketId") Long ticketId) throws Exception{
        return ticketServiceImpl.view(ticketId);
    }

    @PostMapping("manage/ticket/add")
    public String createTicket(@RequestBody Ticket ticket) throws Exception{
        ticketServiceImpl.create(ticket);
        return "Ticket "+ ticket.getTicketNumber() + " added.";
    }

    @PutMapping ("manage/ticket/update/{id}")
    public Map<String, Boolean> updateTicket(@PathVariable("id") Long id,
                                             @RequestBody Ticket updatedTicket) throws Exception{
        ticketServiceImpl.update(id, updatedTicket);
        Map<String, Boolean> response = new HashMap<>();
        response.put("--Employee Updated--", Boolean.TRUE);
        return response;
    }

    @DeleteMapping("manage/ticket/delete/{id}")
    public Map<String, Boolean> deleteTicket(@PathVariable("id")Long id) throws Exception{
        ticketServiceImpl.delete(id);
        Map<String, Boolean> response = new HashMap<>();
        response.put("--Employee Deleted--", Boolean.TRUE);
        return response;
    }

    @PutMapping("manage/ticket/{ticketId}/addWatcher/{employeeId}")
    public Map<String, Boolean> addWatcher(@PathVariable("employeeId") Long employeeId,
                                           @PathVariable("ticketId") Long ticketId) throws Exception{
        ticketServiceImpl.addWatcher(ticketId, employeeId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("--Added ticket watcher--", Boolean.TRUE);
        return response;
    }

    @PutMapping("manage/ticket/{ticketId}/removeWatcher/{employeeId}")
    public Map<String, Boolean> removeWatcher(@PathVariable("employeeId") Long employeeId,
                                           @PathVariable("ticketId") Long ticketId) throws Exception{
        ticketServiceImpl.removeWatcher(ticketId, employeeId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("--Removed ticket watcher--", Boolean.TRUE);
        return response;
    }

    @GetMapping("tickets/viewWatchedBy/{employeeNumber}")
    public Set<Ticket> viewTicketsWatchedByEmlployeeNumber (@PathVariable("employeeNumber") long employeeNumber) throws Exception{
        return ticketServiceImpl.viewTicketsWatchedByEmployeeNumber(employeeNumber);
    }

    @GetMapping("tickets/viewAssigned/{employeeNumber}")
    public Set<Ticket> viewTicketsAssignedToEmployeeNumber (@PathVariable("employeeNumber") long employeeNumber) throws Exception{
        return ticketServiceImpl.viewTicketsAssignedToEmployeeNumber(employeeNumber);
    }


}
