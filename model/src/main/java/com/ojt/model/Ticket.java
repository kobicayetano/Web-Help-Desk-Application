package com.ojt.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ojt.model.enums.Severity;
import com.ojt.model.enums.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter @Getter @NoArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_number")
    private Long ticketNumber;


    private String title;

    private String description;

    private Severity severity;

    private Status status;

    @ManyToOne
    @JoinColumn(name = "assignee_id", referencedColumnName = "id")
    //@JsonIgnore
    private Employee assignee;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name="ticket_watchers", joinColumns = {@JoinColumn( name="ticket_id")},
            inverseJoinColumns = {@JoinColumn(name="employee_id")})
    //@JsonIgnore
    private List<Employee> watchers = new ArrayList<>();

    public Ticket(String title, String description, Severity severity, Status status) {
        this.title = title;
        this.description = description;
        this.severity = severity;
        this.status = status;
    }

    public void removeWatcher(Employee employee){
        this.watchers.remove(employee);
    }

    public void removeAllWatchers(){
        for(Employee watcher: this.watchers){
            watcher.removeWatchedTicket(this);
        }
    }

    public void addWatcher(Employee employee){
        this.watchers.add(employee);
    }

}
