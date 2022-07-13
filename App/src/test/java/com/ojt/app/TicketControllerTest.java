package com.ojt.app;

import com.ojt.model.Employee;
import com.ojt.model.Ticket;
import com.ojt.model.enums.Department;
import com.ojt.model.enums.Severity;
import com.ojt.model.enums.Status;
import com.ojt.service.EmployeeServiceImpl;
import com.ojt.service.TicketServiceImpl;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static com.ojt.app.Utils.JsonUtil.toJson;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith({ RestDocumentationExtension.class, SpringExtension.class})
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
public class TicketControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private TicketServiceImpl ticketService;

    @BeforeEach
    private void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentationContextProvider){

        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentationContextProvider))
                .build();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void getAllTickets() throws Exception {
        Long ticketId = 1l;
        Ticket ticket = new Ticket(
                "Finished project",
                "The project is complete",
                Severity.Low,
                Status.Closed
        );
        ticket.setTicketNumber(ticketId);

        List<Ticket> ticketList = Arrays.asList(ticket);
        given(ticketService.list()).willReturn(ticketList);

        mockMvc.perform(get("/helpdesk/tickets/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", Matchers.is(1)))
                .andExpect(jsonPath("$[0].ticketNumber", Matchers.is(1)))
                .andDo(document("{ClassName}/{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].ticketNumber").description("Generated ticket id"),
                                fieldWithPath("[].title").description("Title of the ticket"),
                                fieldWithPath("[].description").description("Description of the ticket"),
                                fieldWithPath("[].severity").description("Severity of the ticket"),
                                fieldWithPath("[].status").description("Status of the ticket"),
                                subsectionWithPath("[].assignee").description("Details of the employee assigned to the ticket"),
                                subsectionWithPath("[].watchers").description("Details of the employee watching the ticket")
                        )));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void getOneTicket() throws Exception {
        Long ticketId = 1l;
        Ticket ticket = new Ticket(
                "Finished project",
                "The project is complete",
                Severity.Low,
                Status.Closed
        );
        ticket.setTicketNumber(ticketId);

        given(ticketService.view(any())).willReturn(ticket);
        mockMvc.perform(get("/helpdesk/tickets/"+ticketId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("ticketNumber", Matchers.is(1)))
                .andDo(document("{ClassName}/{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("ticketNumber").description("Generated ticket id"),
                                fieldWithPath("title").description("Title of the ticket"),
                                fieldWithPath("description").description("Description of the ticket"),
                                fieldWithPath("severity").description("Severity of the ticket"),
                                fieldWithPath("status").description("Status of the ticket"),
                                subsectionWithPath("assignee").description("Details of the employee assigned to the ticket"),
                                subsectionWithPath("watchers").description("Details of the employee watching the ticket")
                        )));

    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void addTicket() throws Exception{
        Long ticketId = 1l;
        Ticket ticket = new Ticket(
                "Finished project",
                "The project is complete",
                Severity.Low,
                Status.Closed
        );
        ticket.setTicketNumber(ticketId);

        given(ticketService.create(any())).willReturn(ticket);
        mockMvc.perform(post("/helpdesk/manage/ticket/add")
                 .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(ticket)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("ticketNumber", Matchers.is(1)))
                .andDo(document("{ClassName}/{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("ticketNumber").description("Generated ticket id"),
                                fieldWithPath("title").description("Title of the ticket"),
                                fieldWithPath("description").description("Description of the ticket"),
                                fieldWithPath("severity").description("Severity of the ticket"),
                                fieldWithPath("status").description("Status of the ticket"),
                                subsectionWithPath("assignee").description("Details of the employee assigned to the ticket"),
                                subsectionWithPath("watchers").description("Details of the employee watching the ticket")
                        )));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void deleteTicket() throws Exception{
        Long ticketId = 1l;

        given(ticketService.delete(any())).willReturn(true);

        mockMvc.perform(delete("/helpdesk/manage/ticket/delete/"+ticketId.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andDo(document("{ClassName}/{methodName}",
                        preprocessRequest(prettyPrint())
                        ));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void updateTicket() throws Exception{
        Long ticketId = 1l;
        Ticket ticket = new Ticket(
                "Finished project",
                "The project is complete",
                Severity.Low,
                Status.Closed
        );
        ticket.setTicketNumber(ticketId);

        given(ticketService.update(any(), any())).willReturn(ticket);
        mockMvc.perform(put("/helpdesk/manage/ticket/update/"+ticketId.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(ticket)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("ticketNumber", Matchers.is(1)))
                .andDo(document("{ClassName}/{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("ticketNumber").description("Generated ticket id"),
                                fieldWithPath("title").description("Title of the updated ticket"),
                                fieldWithPath("description").description("Description of the updated ticket"),
                                fieldWithPath("severity").description("Severity of the updated ticket"),
                                fieldWithPath("status").description("Status of the updated ticket"),
                                subsectionWithPath("assignee").description("Details of the employee assigned to the ticket"),
                                subsectionWithPath("watchers").description("Details of the employee watching the ticket")
                        )));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void addTicketWatcher() throws Exception {
        Long employeeId = 1L;
        Employee employee = new Employee(
                1000,
                "Kobi",
                "Glema",
                "Cayetano",
                Department.ADMIN
        );
        employee.setId(employeeId);
        Long ticketId = 1l;
        Ticket ticket = new Ticket(
                "Finished project",
                "The project is complete",
                Severity.Low,
                Status.Closed
        );
        ticket.setTicketNumber(ticketId);
        ticket.addWatcher(employee);

        given(ticketService.addWatcher(ticketId, employeeId)).willReturn(ticket);

        mockMvc.perform(put("/helpdesk/manage/ticket/"+ticketId.toString() +"/addWatcher/" + employeeId.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(ticket)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("watchers", Matchers.hasSize(1)))
                .andExpect(jsonPath("watchers[0].id", Matchers.is(1)))
                .andExpect(jsonPath("ticketNumber", Matchers.is(1)))
                .andDo(document("{ClassName}/{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("ticketNumber").description("Generated ticket id"),
                                fieldWithPath("title").description("Title of the updated ticket"),
                                fieldWithPath("description").description("Description of the updated ticket"),
                                fieldWithPath("severity").description("Severity of the updated ticket"),
                                fieldWithPath("status").description("Status of the updated ticket"),
                                subsectionWithPath("assignee").description("Details of the employee assigned to the ticket"),
                                subsectionWithPath("watchers").description("Details of the employee watching the ticket")
                        )));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void removeTicketWatcher() throws Exception {
        Long employeeId = 1L;
        Long ticketId = 1l;
        Ticket ticket = new Ticket(
                "Finished project",
                "The project is complete",
                Severity.Low,
                Status.Closed
        );
        ticket.setTicketNumber(ticketId);

        given(ticketService.removeWatcher(ticketId, employeeId)).willReturn(ticket);

        mockMvc.perform(put("/helpdesk/manage/ticket/"+ticketId.toString() +"/removeWatcher/" + employeeId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(ticket)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("watchers", Matchers.hasSize(0)))
                .andExpect(jsonPath("ticketNumber", Matchers.is(1)))
                .andDo(document("{ClassName}/{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("ticketNumber").description("Generated ticket id"),
                                fieldWithPath("title").description("Title of the updated ticket"),
                                fieldWithPath("description").description("Description of the updated ticket"),
                                fieldWithPath("severity").description("Severity of the updated ticket"),
                                fieldWithPath("status").description("Status of the updated ticket"),
                                subsectionWithPath("assignee").description("Details of the employee assigned to the ticket"),
                                subsectionWithPath("watchers").description("Details of the employee watching the ticket")
                        )));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void viewTicketWatchedByEmployeeNumber() throws Exception {
        Long employeeId = 1L;
        Employee employee = new Employee(
                1000,
                "Kobi",
                "Glema",
                "Cayetano",
                Department.ADMIN
        );
        employee.setId(employeeId);
        Long ticketId = 1l;
        Ticket ticket = new Ticket(
                "Finished project",
                "The project is complete",
                Severity.Low,
                Status.Closed
        );
        ticket.setTicketNumber(ticketId);
        ticket.addWatcher(employee);

        Set<Ticket> ticketSet = Set.of(ticket);
        given(ticketService.viewTicketsWatchedByEmployeeNumber(anyLong())).willReturn(ticketSet);


        mockMvc.perform(get("/helpdesk/tickets/viewWatchedBy/"+ String.valueOf(employee.getEmployeeNumber()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", Matchers.is(1)))
                .andDo(document("{ClassName}/{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].ticketNumber").description("Generated ticket id"),
                                fieldWithPath("[].title").description("Title of the updated ticket"),
                                fieldWithPath("[].description").description("Description of the updated ticket"),
                                fieldWithPath("[].severity").description("Severity of the updated ticket"),
                                fieldWithPath("[].status").description("Status of the updated ticket"),
                                subsectionWithPath("[].assignee").description("Details of the employee assigned to the ticket"),
                                subsectionWithPath("[].watchers").description("Details of the employee watching the ticket")
                        )));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void viewTicketAssignedToEmployeeNumber() throws Exception {
        Long employeeId = 1L;
        Employee employee = new Employee(
                1000,
                "Kobi",
                "Glema",
                "Cayetano",
                Department.ADMIN
        );
        employee.setId(employeeId);
        Long ticketId = 1l;
        Ticket ticket = new Ticket(
                "Finished project",
                "The project is complete",
                Severity.Low,
                Status.Closed
        );
        ticket.setTicketNumber(ticketId);
        ticket.setAssignee(employee);

        Set<Ticket> ticketSet = Set.of(ticket);

        given(ticketService.viewTicketsAssignedToEmployeeNumber(anyLong())).willReturn(ticketSet);


        mockMvc.perform(get("/helpdesk/tickets/viewAssignedTo/"+ String.valueOf(employee.getEmployeeNumber()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", Matchers.is(1)))
                .andDo(document("{ClassName}/{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].ticketNumber").description("Generated ticket id"),
                                fieldWithPath("[].title").description("Title of the updated ticket"),
                                fieldWithPath("[].description").description("Description of the updated ticket"),
                                fieldWithPath("[].severity").description("Severity of the updated ticket"),
                                fieldWithPath("[].status").description("Status of the updated ticket"),
                                subsectionWithPath("[].assignee").description("Details of the employee assigned to the ticket"),
                                subsectionWithPath("[].watchers").description("Details of the employee watching the ticket")
                        )));
    }


}
