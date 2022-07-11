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
    public Ticket getTicketById(@PathVariable("ticketId") Long ticketId) throws Exception{
        return ticketServiceImpl.view(ticketId);
    }

    @PostMapping("manage/ticket/add")
    public Ticket createTicket(@RequestBody Ticket ticket) throws Exception{
        return ticketServiceImpl.create(ticket);
    }

    @PutMapping ("manage/ticket/update/{id}")
    public Ticket updateTicket(@PathVariable("id") Long id,
                                             @RequestBody Ticket updatedTicket) throws Exception{
        return ticketServiceImpl.update(id, updatedTicket);
    }

    @DeleteMapping("manage/ticket/delete/{id}")
    public Boolean deleteTicket(@PathVariable("id")Long id) throws Exception{
        return ticketServiceImpl.delete(id);

    }

    @PutMapping("manage/ticket/{ticketId}/addWatcher/{employeeId}")
    public Ticket addWatcher(@PathVariable("employeeId") Long employeeId,
                                           @PathVariable("ticketId") Long ticketId) throws Exception{
        return ticketServiceImpl.addWatcher(ticketId, employeeId);
    }

    @PutMapping("manage/ticket/{ticketId}/removeWatcher/{employeeId}")
    public Ticket removeWatcher(@PathVariable("employeeId") Long employeeId,
                                           @PathVariable("ticketId") Long ticketId) throws Exception{
        return ticketServiceImpl.removeWatcher(ticketId, employeeId);
    }

    @GetMapping("tickets/viewWatchedBy/{employeeNumber}")
    public Set<Ticket> viewTicketsWatchedByEmployeeNumber (@PathVariable("employeeNumber") long employeeNumber) throws Exception{
        return ticketServiceImpl.viewTicketsWatchedByEmployeeNumber(employeeNumber);
    }

    @GetMapping("tickets/viewAssignedTo/{employeeNumber}")
    public Set<Ticket> viewTicketsAssignedToEmployeeNumber (@PathVariable("employeeNumber") long employeeNumber) throws Exception{
        return ticketServiceImpl.viewTicketsAssignedToEmployeeNumber(employeeNumber);
    }


}
