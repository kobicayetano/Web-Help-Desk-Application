package com.ojt.app;
import com.ojt.model.User;
import com.ojt.service.UserDetailsServiceImpl;
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

import static com.ojt.app.Utils.JsonUtil.toJson;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith({ RestDocumentationExtension.class, SpringExtension.class})
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
public class UserControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    private void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentationContextProvider){

        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentationContextProvider))
                .build();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void getAllUsers() throws Exception {
        User user = new User(
                "username",
                "password",
                "ADMIN"
        );
        user.setId(1L);

        List<User> users = Arrays.asList(user);
        given(userDetailsService.list()).willReturn(users);

        mockMvc.perform(get("/users/all")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.size()", Matchers.is(1)))
                        .andExpect(jsonPath("$[0].id", Matchers.is(1)))
                        .andDo(document("{ClassName}/{methodName}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                responseFields(
                                        fieldWithPath("[].id").description("Generated user id"),
                                        fieldWithPath("[].username").description("User's username credentials"),
                                        fieldWithPath("[].password").description("User's password credentials"),
                                        fieldWithPath("[].role").description("User's assigned role")
                        )));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void createUser() throws Exception {
        User user = new User(
                "username",
                "password",
                "ADMIN"
        );
        user.setId(1L);

        given(userDetailsService.create(any())).willReturn(user);

        mockMvc.perform(post("/users/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(user)))
                        .andDo(print())
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("id", Matchers.is(1)))
                        .andDo(document("{ClassName}/{methodName}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                responseFields(
                                        fieldWithPath("id").description("Generated user id"),
                                        fieldWithPath("username").description("User's username credentials"),
                                        fieldWithPath("password").description("User's password credentials"),
                                        fieldWithPath("role").description("User's assigned role")
                        )));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void updateUser() throws Exception {
        Long userId = 1L;
        User user = new User(
                "username",
                "password",
                "ADMIN"
        );
        user.setId(userId);

        given(userDetailsService.update(any(), any())).willReturn(user);

        mockMvc.perform(put("/users/update/"+userId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(user)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", Matchers.is(1)))
                .andDo(document("{ClassName}/{methodName}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("id").description("Generated user id"),
                                fieldWithPath("username").description("User's username credentials"),
                                fieldWithPath("password").description("User's password credentials"),
                                fieldWithPath("role").description("User's assigned role")
                        )));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void deleteUser() throws Exception {
        Long userId = 1L;
        given(userDetailsService.delete(any())).willReturn(true);
        mockMvc.perform(delete("/users/delete/" + userId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andDo(document("{ClassName}/{methodName}",
                        preprocessRequest(prettyPrint())
                ));
    }
}
