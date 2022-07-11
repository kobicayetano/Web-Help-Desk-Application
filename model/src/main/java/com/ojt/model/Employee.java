package com.ojt.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.ojt.model.enums.Department;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
@Getter @Setter @NoArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="employee_number", nullable = false, updatable = false, unique = true)
    private long employeeNumber;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "middle_name", nullable = false)
    private String middleName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "department", nullable = false)
    private Department department;

    @OneToMany(mappedBy = "assignee",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Ticket> ticketAssigned = new HashSet<>();

    @ManyToMany(mappedBy = "watchers",
                fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Ticket> ticketWatched = new HashSet<>();


    public Employee(long employeeNumber, String firstName, String middleName, String lastName, Department department) {
        this.employeeNumber = employeeNumber;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.department = department;
    }

    public void removeAllWatchedTickets(){
        for(Ticket ticket: ticketWatched){
            ticket.removeWatcher(this);
        }
    }

    public void removeWatchedTicket(Ticket ticket){
        this.ticketWatched.remove(ticket);
    }

    public void removeAssignedTicket(Ticket ticket){
        this.ticketAssigned.remove(ticket);
    }





}