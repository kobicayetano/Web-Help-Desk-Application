package com.ojt.service;

import com.ojt.model.Employee;
import com.ojt.model.Ticket;
import com.ojt.model.enums.Department;
import com.ojt.model.enums.Severity;
import com.ojt.model.enums.Status;
import com.ojt.persistence.EmployeeRepository;
import com.ojt.persistence.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
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
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private TicketRepository ticketRepository;


    @BeforeEach
    void setUp(){
        underTest = new EmployeeServiceImpl(employeeRepository, ticketRepository);
    }

    private EmployeeServiceImpl underTest;



    @Test
    public void canGetListOfEmployees() {
        //when
        underTest.list();
        //then
        verify(employeeRepository).findAll();

    }

    @Test
    public void canViewOneEmployee() throws Exception {
        //Given
        Long id = 1L;
        Employee employee = new Employee(
                1000,
                "Kobi",
                "Glema",
                "Cayetano",
                Department.ADMIN
        );
        //when
        given(employeeRepository.findById(any())).willReturn(Optional.of(employee));
        underTest.view(id);
        //then
        verify(employeeRepository).findById(id);
    }

    @Test
    public void cannotViewOneEmployee() throws Exception {
        //Given
        Long id = 1L;
        //when
        //then
        assertThatThrownBy(() -> underTest.view(id))
                .hasMessage("Employee with id: "+ id + " does not exists.");
    }

    @Test
    public void canAddNewEmployee() throws Exception{
        //Given
        Employee employee = new Employee(
                1000,
                "Kobi",
                "Glema",
                "Cayetano",
                Department.ADMIN
        );

        //when
        underTest.create(employee);

        //then
        ArgumentCaptor<Employee> employeeArgumentCaptor =
                ArgumentCaptor.forClass(Employee.class);

        verify(employeeRepository)
                .save(employeeArgumentCaptor.capture());

        Employee capturedStudent = employeeArgumentCaptor.getValue();
        assertThat(capturedStudent).isEqualTo(employee);

    }

    @Test
    public void cannotAddNewEmployeeWithExistingEmployeeNumber() throws Exception{
        //Given
        Employee employee = new Employee(
                1000,
                "Kobi",
                "Glema",
                "Cayetano",
                Department.ADMIN
        );

        given(employeeRepository.existsByEmployeeNumber(employee.getEmployeeNumber()))
                .willReturn(true);
        //when
        //then
        assertThatThrownBy(() -> underTest.create(employee))
                .hasMessage("Employee Number: " + employee.getEmployeeNumber() + " already exists.");


        verify(employeeRepository, never()).save(any());
    }

    @Test
    public void canDeleteOneEmployee() throws Exception{
        //Given
        Long id = 1L;
        Employee employee = new Employee(
                1000,
                "Kobi",
                "Glema",
                "Cayetano",
                Department.ADMIN
        );
        //when
        given(employeeRepository.findById(any())).willReturn(Optional.of(employee));
        Boolean actual = underTest.delete(id);
        //then
        verify(employeeRepository).delete(employee);
        assertTrue(actual);
    }

    @Test
    public void cannotDeleteEmployeeThatDoesntExist(){
        //Given
        Long id = 1L;
        //when
        //then
        assertThatThrownBy(() -> underTest.delete(id))
                .hasMessage("Employee with id: "+ id + " does not exists.");
        verify(employeeRepository, never()).delete(any());
    }

    @Test
    public void cannotDeleteEmployeeThatHasTicket(){
        //Given
        Long id = 1L;
        Employee employee = new Employee(
                1000,
                "Kobi",
                "Glema",
                "Cayetano",
                Department.ADMIN
        );

        Ticket ticket = new Ticket(
                "Project Ticket",
                "New project ticket",
                Severity.Low,
                Status.Closed
        );
        Set<Ticket> ticketAssigned = new HashSet<>();
        ticketAssigned.add(ticket);
        employee.setTicketAssigned(ticketAssigned);
        given(employeeRepository.findById(any())).willReturn(Optional.of(employee));
        assertThatThrownBy(() -> underTest.delete(id))
                .hasMessage("Cannot delete employee with assigned ticket.");
        verify(employeeRepository, never()).save(any());
    }

    @Test
    public void canUpdateEmployee() throws Exception{
        //Given
        Long id = 1L;
        Employee employee = new Employee(
                1000,
                "Kobi",
                "Glema",
                "Cayetano",
                Department.ADMIN
        );
        //when
        given(employeeRepository.findById(any())).willReturn(Optional.of(employee));
        given(employeeRepository.existsById(any())).willReturn(false);
        underTest.update(id, employee);
        //then
        ArgumentCaptor<Employee> employeeArgumentCaptor =
                ArgumentCaptor.forClass(Employee.class);

        verify(employeeRepository)
                .save(employeeArgumentCaptor.capture());

        Employee capturedStudent = employeeArgumentCaptor.getValue();
        assertThat(capturedStudent).isEqualTo(employee);

    }

    @Test
    public void cannotUpdateNotExistingEmployee() throws Exception{
        //Given
        Long id = 1L;
        Employee employee = new Employee(
                1000,
                "Kobi",
                "Glema",
                "Cayetano",
                Department.ADMIN
        );
        //when
        //then
        assertThatThrownBy(() -> underTest.update(id, employee))
                .hasMessage("Employee with id: "+ id + " does not exists.");
        verify(employeeRepository, never()).save(any());

    }

    @Test
    public void cannotUpdateEmployeeWithExistingEmployeeNumber() throws Exception{
        //Given
        Long id = 1L;
        Employee employee = new Employee(
                1000,
                "Kobi",
                "Glema",
                "Cayetano",
                Department.ADMIN
        );
        //when
        //then
        given(employeeRepository.findById(any())).willReturn(Optional.of(employee));
        given(employeeRepository.existsById(any())).willReturn(true);
        assertThatThrownBy(() -> underTest.update(id, employee))
                .hasMessage("Employee with employee number: "+ employee.getEmployeeNumber() + " already exists.");
        verify(employeeRepository, never()).save(any());
    }

    @Test
    public void canAssignTicketToEmployee() throws Exception{
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
        underTest.assignTicket(employeeId, ticketId);
        //then
        verify(ticketRepository).save(ticket);
        assertEquals(employee.getEmployeeNumber(), ticket.getAssignee().getEmployeeNumber());
    }

    @Test
    public void cannotAssignTicketToNotExistingEmployee() throws Exception{
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
        assertThatThrownBy(() -> underTest.assignTicket(employeeId, ticketId))
                .hasMessage("Employee with id: "+ employeeId + " does not exists.");

        verify(ticketRepository, never()).save(ticket);
        assertNull(ticket.getAssignee());
    }

    @Test
    public void cannotAssignNotExistingTicketToEmployee() throws Exception{
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
        given(employeeRepository.findById(any())).willReturn(Optional.of(employee));
        assertThatThrownBy(() -> underTest.assignTicket(employeeId, ticketId))
                .hasMessage("Ticket with id: "+ ticketId + " does not exists.");
        //then
        verify(ticketRepository, never()).save(ticket);
        assertNull(ticket.getAssignee());
    }

    @Test
    public void cannotAssignTicketToAnotherEmployee() throws Exception{
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
        given(employeeRepository.findById(any())).willReturn(Optional.of(employee));
        given(ticketRepository.findById(any())).willReturn(Optional.of(ticket));
        ticket.setAssignee(employee);
        assertThatThrownBy(() -> underTest.assignTicket(employeeId, ticketId))
                .hasMessage("Ticket with id: "+ ticketId + " is already assigned to a user.");
        //then
        verify(ticketRepository, never()).save(ticket);
        assertNotNull(ticket.getAssignee());
    }


}