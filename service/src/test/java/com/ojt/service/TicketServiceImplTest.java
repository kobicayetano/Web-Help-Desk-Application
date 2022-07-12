package com.ojt.service;

import com.ojt.model.Employee;
import com.ojt.model.Ticket;
import com.ojt.model.enums.Department;
import com.ojt.model.enums.Severity;
import com.ojt.model.enums.Status;
import com.ojt.persistence.EmployeeRepository;
import com.ojt.persistence.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith({MockitoExtension.class})
class TicketServiceImplTest {
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private TicketRepository ticketRepository;


    @BeforeEach
    void setUp(){
        underTest = new TicketServiceImpl(employeeRepository, ticketRepository);
    }

    private TicketServiceImpl underTest;

    @Test
    public void canGetListOfTickets() {
        //when
        underTest.list();
        //then
        verify(ticketRepository).findAll();
    }

    @Test
    public void canViewOneTicket() throws Exception{
        //given
        Long id = 1L;
        Ticket ticket = new Ticket(
                "Project Ticket",
                "New project ticket",
                Severity.Low,
                Status.Closed
        );
        //when
        given(ticketRepository.findById(id)).willReturn(Optional.of(ticket));
        underTest.view(id);
        //then
        verify(ticketRepository).findById(id);
    }

    @Test
    public void cannotViewNotExistingTicket() throws Exception{
        //given
        Long id = 1L;
        //when
        //then
        assertThatThrownBy(() -> underTest.view(id))
                .hasMessage("Ticket with id: "+ id + " does not exist.");
    }

    @Test
    public void canAddNewTicket() throws Exception{
        //Given
        Ticket ticket = new Ticket(
                "Project Ticket",
                "New project ticket",
                Severity.Low,
                Status.Closed
        );
        //when
        underTest.create(ticket);
        //then
        ArgumentCaptor<Ticket> ticketArgumentCaptor =
                ArgumentCaptor.forClass(Ticket.class);

        verify(ticketRepository)
                .save(ticketArgumentCaptor.capture());

        Ticket capturedTicket = ticketArgumentCaptor.getValue();
        assertThat(capturedTicket).isEqualTo(ticket);
    }

    @Test
    public void canDeleteTicket() throws Exception{
        //given
        Long id = 1L;
        Ticket ticket = new Ticket(
                "Project Ticket",
                "New project ticket",
                Severity.Low,
                Status.Closed
        );
        //when
        given(ticketRepository.findById(id)).willReturn(Optional.of(ticket));
        Boolean actual = underTest.delete(id);
        //then
        verify(ticketRepository).delete(ticket);
        assertTrue(actual);
    }

    @Test
    public void canDeleteTicketWithWatcherAndAssignee() throws Exception{
        //given
        Long id = 1L;
        Ticket ticket = new Ticket(
                "Project Ticket",
                "New project ticket",
                Severity.Low,
                Status.Closed
        );
        Long employeeId = 1L;
        Employee employee = new Employee(
                1000,
                "Kobi",
                "Glema",
                "Cayetano",
                Department.ADMIN
        );
        Long employeeId1 = 2L;
        Employee employee1 = new Employee(
                2000,
                "Kyle",
                "Cayetano",
                "Glema",
                Department.IT
        );
        //when
        given(ticketRepository.findById(id)).willReturn(Optional.of(ticket));
        ticket.setAssignee(employee);
        ticket.addWatcher(employee1);
        Boolean actual = underTest.delete(id);
        //then
        verify(ticketRepository).delete(ticket);
        assertTrue(actual);
    }



    @Test
    public void cannotDeleteNotExistingTicket() throws Exception{
        //given
        Long id = 1L;
        //when
        //then
        assertThatThrownBy(() -> underTest.delete(id))
                .hasMessage("Ticket with id: "+ id + " does not exist.");
    }

    @Test
    public void canUpdateEmployee() throws Exception{
        //given
        Long id = 1L;
        Ticket ticket = new Ticket(
                "Project Ticket",
                "New project ticket",
                Severity.Low,
                Status.Closed
        );
        //when
        given(ticketRepository.findById(any())).willReturn(Optional.of(ticket));
        underTest.update(id, ticket);
        //then
        ArgumentCaptor<Ticket> ticketArgumentCaptor =
                ArgumentCaptor.forClass(Ticket.class);

        verify(ticketRepository)
                .save(ticketArgumentCaptor.capture());

        Ticket capturedTicket = ticketArgumentCaptor.getValue();
        assertThat(capturedTicket).isEqualTo(ticket);
    }

    @Test
    public void cannotUpdateNotExistingEmployee() throws Exception{
        //given
        Long id = 1L;
        Ticket ticket = new Ticket(
                "Project Ticket",
                "New project ticket",
                Severity.Low,
                Status.Closed
        );
        //when
        //then
        assertThatThrownBy(() -> underTest.delete(id))
                .hasMessage("Ticket with id: "+ id + " does not exist.");
    }

    @Test
    public void canAddWatcher() throws Exception{
        //Given
        Long employeeId = 1L;
        Employee employee = new Employee(
                1000,
                "Kobi",
                "Glema",
                "Cayetano",
                Department.ADMIN
        );
        Long ticketId = 1L;
        Ticket ticket = new Ticket(
                "Project Ticket",
                "New project ticket",
                Severity.Low,
                Status.Closed
        );
        //when
        given(employeeRepository.findById(any())).willReturn(Optional.of(employee));
        given(ticketRepository.findById(any())).willReturn(Optional.of(ticket));
        underTest.addWatcher(ticketId, employeeId);
        //then
        verify(ticketRepository).save(ticket);
        assertNotNull(ticket.getWatchers());
    }

    @Test
    public void cannotAddWatcherToNotExistingEmployee() throws Exception{
        //Given
        Long employeeId = 1L;
        Long ticketId = 1L;
        Ticket ticket = new Ticket(
                "Project Ticket",
                "New project ticket",
                Severity.Low,
                Status.Closed
        );
        //when
        //then
        given(ticketRepository.findById(any())).willReturn(Optional.of(ticket));
        assertThatThrownBy(() -> underTest.addWatcher(ticketId, employeeId))
                .hasMessage("Employee with id: "+ employeeId + " does not exist.");
        List<Employee> emptyList = new ArrayList<Employee>();
        verify(ticketRepository, never()).save(ticket);
        assertEquals(emptyList, ticket.getWatchers());
    }

    @Test
    public void cannotAddWatcherToNotExistingTicket() throws Exception{
        //Given
        Long employeeId = 1L;
        Employee employee = new Employee(
                1000,
                "Kobi",
                "Glema",
                "Cayetano",
                Department.ADMIN
        );
        Long ticketId = 1L;
        //when
        //then
        assertThatThrownBy(() -> underTest.addWatcher(ticketId, employeeId))
                .hasMessage("Ticket with id: "+ employeeId + " does not exist.");
        verify(ticketRepository, never()).save(any());
    }

    @Test
    public void cannotAddExistingWatcherAsWatcher() throws Exception{
        //Given
        Long employeeId = 1L;
        Employee employee = new Employee(
                1000,
                "Kobi",
                "Glema",
                "Cayetano",
                Department.ADMIN
        );
        Long ticketId = 1L;
        Ticket ticket = new Ticket(
                "Project Ticket",
                "New project ticket",
                Severity.Low,
                Status.Closed
        );
        //when
        given(employeeRepository.findById(any())).willReturn(Optional.of(employee));
        given(ticketRepository.findById(any())).willReturn(Optional.of(ticket));
        underTest.addWatcher(ticketId, employeeId);
        //then
        assertThatThrownBy(() -> underTest.addWatcher(ticketId, employeeId))
                .hasMessage("Employee with id: "+ employeeId + " is already listed as watcher.");
    }

    @Test
    public void canRemoveWatcher() throws Exception {
        //Given
        Long employeeId = 1L;
        Employee employee = new Employee(
                1000,
                "Kobi",
                "Glema",
                "Cayetano",
                Department.ADMIN
        );
        Long ticketId = 1L;
        Ticket ticket = new Ticket(
                "Project Ticket",
                "New project ticket",
                Severity.Low,
                Status.Closed
        );
        //when
        ticket.addWatcher(employee);
        given(employeeRepository.findById(any())).willReturn(Optional.of(employee));
        given(ticketRepository.findById(any())).willReturn(Optional.of(ticket));
        underTest.removeWatcher(ticketId, employeeId);
        //then
        verify(ticketRepository).save(ticket);
        List<Employee> emptyList = new ArrayList<>();
        assertEquals(emptyList, ticket.getWatchers());
    }

    @Test
    public void cannotRemoveNotExistingEmployeeAsWatcher() throws Exception {
        //Given
        Long employeeId = 1L;
        Employee employee = new Employee(
                1000,
                "Kobi",
                "Glema",
                "Cayetano",
                Department.ADMIN
        );
        Long ticketId = 1L;
        Ticket ticket = new Ticket(
                "Project Ticket",
                "New project ticket",
                Severity.Low,
                Status.Closed
        );
        //when
        given(ticketRepository.findById(any())).willReturn(Optional.of(ticket));
        //then
        assertThatThrownBy(() -> underTest.removeWatcher(ticketId, employeeId))
                .hasMessage("Employee with id: "+ employeeId + " does not exist.");
        verify(ticketRepository, never()).save(ticket);
    }

    @Test
    public void cannotRemoveWatcherThatDoesNotExist() throws Exception {
        //Given
        Long employeeId = 1L;
        Employee employee = new Employee(
                1000,
                "Kobi",
                "Glema",
                "Cayetano",
                Department.ADMIN
        );
        Long ticketId = 1L;
        Ticket ticket = new Ticket(
                "Project Ticket",
                "New project ticket",
                Severity.Low,
                Status.Closed
        );
        //when
        given(employeeRepository.findById(any())).willReturn(Optional.of(employee));
        given(ticketRepository.findById(any())).willReturn(Optional.of(ticket));
        //then
        assertThatThrownBy(() -> underTest.removeWatcher(ticketId, employeeId))
                .hasMessage("Employee with id: "+employeeId+" is not a watcher.");
        verify(ticketRepository, never()).save(ticket);
    }

    @Test
    public void cannotRemoveWatcherOfNotExistingTicket() throws Exception {
        //Given
        Long employeeId = 1L;
        Employee employee = new Employee(
                1000,
                "Kobi",
                "Glema",
                "Cayetano",
                Department.ADMIN
        );
        Long ticketId = 1L;
        Ticket ticket = new Ticket(
                "Project Ticket",
                "New project ticket",
                Severity.Low,
                Status.Closed
        );
        //when
        //then
        assertThatThrownBy(() -> underTest.removeWatcher(ticketId, employeeId))
                .hasMessage("Ticket with id: "+ ticketId + " does not exist.");
        verify(ticketRepository, never()).save(ticket);
    }




    @Test
    public void canViewAllTicketsAssignedToEmployeeNumber() throws Exception{
        //Given
        Long employeeId = 1L;
        Employee employee = new Employee(
                1000,
                "Kobi",
                "Glema",
                "Cayetano",
                Department.ADMIN
        );
        Long ticketId = 1L;
        Ticket ticket = new Ticket(
                "Project Ticket",
                "New project ticket",
                Severity.Low,
                Status.Closed
        );
        //when
        given(employeeRepository.findByEmployeeNumber(any())).willReturn(Optional.of(employee));
        employee.setTicketAssigned(Set.of(ticket));
        //then
        assertEquals(Set.of(ticket), underTest.viewTicketsAssignedToEmployeeNumber(employee.getEmployeeNumber()));
    }

    @Test
    public void cannotViewAllTicketsAssignedToNotExistingEmployeeNumber(){
        //when
        Long employeeNumber = 1000L;
        assertThatThrownBy(() -> underTest.viewTicketsAssignedToEmployeeNumber(employeeNumber))
                .hasMessage("Employee with employee number: "+ employeeNumber + " does not exist.");
    }

    @Test
    public void canViewAllTicketsWatchedByEmployeeNumber() throws Exception{
        //Given
        Long employeeId = 1L;
        Employee employee = new Employee(
                1000,
                "Kobi",
                "Glema",
                "Cayetano",
                Department.ADMIN
        );
        Long ticketId = 1L;
        Ticket ticket = new Ticket(
                "Project Ticket",
                "New project ticket",
                Severity.Low,
                Status.Closed
        );
        //when
        given(employeeRepository.findByEmployeeNumber(any())).willReturn(Optional.of(employee));
        employee.setTicketWatched(Set.of(ticket));
        //then
        assertEquals(Set.of(ticket), underTest.viewTicketsWatchedByEmployeeNumber(employee.getEmployeeNumber()));
    }

    @Test
    public void cannotViewAllTicketsWatchedByNotExistingEmployeeNumber(){
        //when
        Long employeeNumber = 1000L;
        assertThatThrownBy(() -> underTest.viewTicketsWatchedByEmployeeNumber(employeeNumber))
                .hasMessage("Employee with employee number: "+ employeeNumber + " does not exist.");
    }




}
/**
    //Given
    Long employeeId = 1L;
    Employee employee = new Employee(
            1000,
            "Kobi",
            "Glema",
            "Cayetano",
            Department.ADMIN
    );
    Long ticketId = 1L;
    Ticket ticket = new Ticket(
            "Project Ticket",
            "New project ticket",
            Severity.Low,
            Status.Closed
    );

 **/