package com.ojt.service;

import com.ojt.model.Ticket;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TicketService {

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public List<Ticket> list();

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public Ticket view(Long id) throws Exception;

    @PreAuthorize("hasRole('ADMIN')")
    public Ticket create(Ticket ticket) throws Exception;

    @PreAuthorize("hasRole('ADMIN')")
    public Ticket update(Long ticketId, Ticket updatedTicket) throws Exception;

    @PreAuthorize("hasRole('ADMIN')")
    public Boolean delete(Long ticketId) throws Exception;

    @PreAuthorize("hasRole('ADMIN')")
    public Ticket addWatcher(Long ticketId, Long employeeId) throws Exception;

    @PreAuthorize("hasRole('ADMIN')")
    public Ticket removeWatcher(Long ticketId, Long employeeId) throws Exception;

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public Set<Ticket> viewTicketsAssignedToEmployeeNumber(long employeeNumber) throws Exception;

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public Set<Ticket> viewTicketsWatchedByEmployeeNumber(long employeeNumber) throws Exception;

}
