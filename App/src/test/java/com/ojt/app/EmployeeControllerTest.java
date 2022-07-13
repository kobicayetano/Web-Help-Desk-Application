package com.ojt.app;
import com.ojt.model.Employee;
import com.ojt.model.Ticket;
import com.ojt.model.enums.Department;
import com.ojt.model.enums.Severity;
import com.ojt.model.enums.Status;
import com.ojt.service.EmployeeServiceImpl;
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
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.ojt.app.Utils.JsonUtil.toJson;

@SpringBootTest
@ExtendWith({ RestDocumentationExtension.class, SpringExtension.class})
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
class EmployeeControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private EmployeeServiceImpl employeeService;

    @BeforeEach
    private void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentationContextProvider){

        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentationContextProvider))
                .build();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void getAllEmployees() throws Exception {
        Employee employee = new Employee(
                1000,
                "Kobi",
                "Glema",
                "Cayetano",
                Department.ADMIN
        );
        employee.setId(1L);
        Employee employee1 = new Employee(
                2000,
                "Shaun",
                "Glema",
                "Florendo",
                Department.ADMIN
        );
        employee1.setId(2L);

        List<Employee> allEmployees = Arrays.asList(employee, employee1);
        given(employeeService.list()).willReturn(allEmployees);

        mockMvc.perform(get("/helpdesk/employees/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", Matchers.is(2)))
                .andExpect(jsonPath("$[0].employeeNumber", Matchers.is(1000)))
                .andExpect(jsonPath("$[1].employeeNumber", Matchers.is(2000)))
                .andDo(document("{ClassName}/{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].id").description("Generated employee id"),
                                fieldWithPath("[].employeeNumber").description("Unique employee number"),
                                fieldWithPath("[].firstName").description("Employee's first name"),
                                fieldWithPath("[].middleName").description("Employee's middle name"),
                                fieldWithPath("[].lastName").description("Employee's last name"),
                                fieldWithPath("[].department").description("Employee's designated department")
                        )));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void getOneEmployee() throws Exception{
        Employee employee = new Employee(
                1000,
                "Kobi",
                "Glema",
                "Cayetano",
                Department.ADMIN
        );
        employee.setId(1L);

        given(employeeService.view(any())).willReturn(employee);

        mockMvc.perform(get("/helpdesk/employees/" + employee.getId().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("employeeNumber", Matchers.is(1000)))
                .andDo(document("{ClassName}/{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("id").description("Generated employee id"),
                                fieldWithPath("employeeNumber").description("Unique employee number"),
                                fieldWithPath("firstName").description("Employee's first name"),
                                fieldWithPath("middleName").description("Employee's middle name"),
                                fieldWithPath("lastName").description("Employee's last name"),
                                fieldWithPath("department").description("Employee's designated department")
                        )));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void addEmployee() throws Exception {
        Employee employee = new Employee(
                1000,
                "Kobi",
                "Glema",
                "Cayetano",
                Department.ADMIN
        );
        employee.setId(1L);

        given(employeeService.create(any())).willReturn(employee);

        mockMvc.perform(post("/helpdesk/manage/employee/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(employee)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("employeeNumber", Matchers.is(1000)))
                .andDo(document("{ClassName}/{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("id").description("Generated employee id"),
                                fieldWithPath("employeeNumber").description("Unique employee number"),
                                fieldWithPath("firstName").description("Employee's first name"),
                                fieldWithPath("middleName").description("Employee's middle name"),
                                fieldWithPath("lastName").description("Employee's last name"),
                                fieldWithPath("department").description("Employee's designated department")
                        )));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void deleteEmployee() throws Exception{
        Long employeeId = 1L;
        given(employeeService.delete(any())).willReturn(true);
        mockMvc.perform(delete("/helpdesk/manage/employee/delete/" + employeeId.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andDo(document("{ClassName}/{methodName}",
                        preprocessRequest(prettyPrint())
                ));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void updateEmployee() throws Exception {
        Long employeeId = 1L;
        Employee employee = new Employee(
                1000,
                "Kobi",
                "Glema",
                "Cayetano",
                Department.ADMIN
        );
        employee.setId(employeeId);

        given(employeeService.update(any(), any())).willReturn(employee);

        mockMvc.perform(put("/helpdesk/manage/employee/update/" + employeeId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(employee)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("employeeNumber", Matchers.is(1000)))
                .andDo(document("{ClassName}/{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("id").description("Generated employee id"),
                                fieldWithPath("employeeNumber").description("Updated  employee number"),
                                fieldWithPath("firstName").description("Employee's updated first name"),
                                fieldWithPath("middleName").description("Employee's updated middle name"),
                                fieldWithPath("lastName").description("Employee's updated last name"),
                                fieldWithPath("department").description("Employee's updated designated department")
                        )));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void assignTicketToEmployee() throws Exception {
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

        given(employeeService.assignTicket(employee.getId(), ticket.getTicketNumber())).willReturn(ticket);

        mockMvc.perform(put("/helpdesk/manage/employee/" + employeeId.toString() + "/assignTicket/" + ticketId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(employee)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("assignee.id", Matchers.is(1)))
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
                                fieldWithPath("watchers").description("Employees watching the ticket"),
                                subsectionWithPath("assignee").description("Details of the employee assigned to the ticket"),
                                subsectionWithPath("watchers").description("Details of the employee watching the ticket")
                        )));
    }

}