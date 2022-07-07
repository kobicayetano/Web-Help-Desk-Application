package com.ojt.controller;



import com.ojt.model.Ticket;
import com.ojt.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("helpdesk/tickets")
public class TicketController {

    private final TicketService ticketService;


    @Autowired
    public TicketController(TicketService ticketService){
        this.ticketService = ticketService;
    }

    @GetMapping("/all")
    public List<Ticket> getAllTickets(){return ticketService.list();}

    @GetMapping("/{ticketId}")
    public Optional<Ticket> getTicketById(@PathVariable("ticketId") Long ticketId) throws Exception{
        return ticketService.view(ticketId);
    }

    @PostMapping("add")
    public String createTicket(@RequestBody Ticket ticket) throws Exception{
        ticketService.create(ticket);
        return "Ticket "+ ticket.getTicketNumber() + " added.";
    }

    @PutMapping ("/update/{id}")
    public Map<String, Boolean> updateTicket(@PathVariable("id") Long id,
                                             @RequestBody Ticket updatedTicket) throws Exception{
        ticketService.update(id, updatedTicket);
        Map<String, Boolean> response = new HashMap<>();
        response.put("--Employee Updated--", Boolean.TRUE);
        return response;
    }

    @DeleteMapping("delete/{id}")
    public Map<String, Boolean> deleteTicket(@PathVariable("id")Long id) throws Exception{
        ticketService.delete(id);
        Map<String, Boolean> response = new HashMap<>();
        response.put("--Employee Deleted--", Boolean.TRUE);
        return response;
    }

    @PutMapping("{ticketId}/addWatcher/{employeeId}")
    public Map<String, Boolean> addWatcher(@PathVariable("employeeId") Long employeeId,
                                           @PathVariable("ticketId") Long ticketId) throws Exception{
        ticketService.addWatcher(ticketId, employeeId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("--Added ticket watcher--", Boolean.TRUE);
        return response;
    }

    @PutMapping("{ticketId}/removeWatcher/{employeeId}")
    public Map<String, Boolean> removeWatcher(@PathVariable("employeeId") Long employeeId,
                                           @PathVariable("ticketId") Long ticketId) throws Exception{
        ticketService.removeWatcher(ticketId, employeeId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("--Removed ticket watcher--", Boolean.TRUE);
        return response;
    }

    @GetMapping("/viewWatched/{employeeNumber}")
    public Set<Ticket> viewTicketsWatchedByEmlployeeNumber (@PathVariable("employeeNumber") long employeeNumber) throws Exception{
        return ticketService.viewTicketsWatchedByEmlployeeNumber(employeeNumber);
    }

    @GetMapping("viewAssigned/{employeeNumber}")
    public Set<Ticket> viewTicketsAssignedToEmployeeNumber (@PathVariable("employeeNumber") long employeeNumber) throws Exception{
        return ticketService.viewTicketsAssignedToEmployeeNumber(employeeNumber);
    }


}
