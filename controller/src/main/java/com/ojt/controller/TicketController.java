package com.ojt.controller;

import com.ojt.model.Ticket;
import com.ojt.service.TicketServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Ticket> getTicketById(@PathVariable("ticketId") Long ticketId) throws Exception{
        return new ResponseEntity<>(ticketServiceImpl.view(ticketId), HttpStatus.OK);
    }

    @PostMapping("manage/ticket/add")
    public ResponseEntity<Ticket> createTicket(@RequestBody Ticket ticket) throws Exception{
        return new ResponseEntity<>(ticketServiceImpl.create(ticket), HttpStatus.CREATED);
    }

    @PutMapping ("manage/ticket/update/{id}")
    public ResponseEntity<Ticket> updateTicket(@PathVariable("id") Long id,
                                             @RequestBody Ticket updatedTicket) throws Exception{
        return new ResponseEntity<>(ticketServiceImpl.update(id, updatedTicket), HttpStatus.OK);
    }

    @DeleteMapping("manage/ticket/delete/{id}")
    public ResponseEntity<Boolean> deleteTicket(@PathVariable("id")Long id) throws Exception{
        return new ResponseEntity<>(ticketServiceImpl.delete(id), HttpStatus.NO_CONTENT);

    }

    @PutMapping("manage/ticket/{ticketId}/addWatcher/{employeeId}")
    public ResponseEntity<Ticket> addWatcher(@PathVariable("employeeId") Long employeeId,
                                           @PathVariable("ticketId") Long ticketId) throws Exception{
        return new ResponseEntity<>(ticketServiceImpl.addWatcher(ticketId, employeeId), HttpStatus.OK);
    }

    @PutMapping("manage/ticket/{ticketId}/removeWatcher/{employeeId}")
    public ResponseEntity<Ticket> removeWatcher(@PathVariable("employeeId") Long employeeId,
                                           @PathVariable("ticketId") Long ticketId) throws Exception{
        return new ResponseEntity<>(ticketServiceImpl.removeWatcher(ticketId, employeeId), HttpStatus.OK);
    }

    @GetMapping("tickets/viewWatchedBy/{employeeNumber}")
    public ResponseEntity<Set<Ticket>> viewTicketsWatchedByEmployeeNumber (@PathVariable("employeeNumber") long employeeNumber) throws Exception{
        return new ResponseEntity<>(ticketServiceImpl.viewTicketsWatchedByEmployeeNumber(employeeNumber), HttpStatus.OK);
    }

    @GetMapping("tickets/viewAssignedTo/{employeeNumber}")
    public ResponseEntity<Set<Ticket>> viewTicketsAssignedToEmployeeNumber (@PathVariable("employeeNumber") long employeeNumber) throws Exception{
        return new ResponseEntity<>(ticketServiceImpl.viewTicketsAssignedToEmployeeNumber(employeeNumber), HttpStatus.OK);
    }


}
