package edu.tcu.cs.hogwarts_artifact_online.HogwartsUser;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tcu.cs.hogwarts_artifact_online.HogwartsUser.Dto.UserDto;
import edu.tcu.cs.hogwarts_artifact_online.System.StatusCode;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Integration test for User API endpoint")
@Tag("Integration")
public class UserControllerIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    String token;
    @BeforeEach
    void setUp() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(post("/api/v1/users/login")
                .with(httpBasic("tony","123456")));
        MvcResult mvcResult = resultActions.andDo(print()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(contentAsString);
        this.token = "Bearer " + jsonObject.getJSONObject("data").getString("token");
    }
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void findAllUsers() throws Exception {
        this.mockMvc.perform(get("/api/v1/users").header(HttpHeaders.AUTHORIZATION, token).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Found successfully"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(3)));
    }
    @Test
    @DisplayName("Check addArtifact with valid input (POST)")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void addUser() throws Exception {
        HogwartsUser u1 = new HogwartsUser();
        u1.setId(4);
        u1.setUsername("jerry");
        u1.setPassword("12345689");
        u1.setEnabled(true);
        u1.setRoles("admin user");

        String json = this.objectMapper.writeValueAsString(u1);

        this.mockMvc.perform(post("/api/v1/users").contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("User added successfully"))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.username").value("jerry"))
                .andExpect(jsonPath("$.data.enabled").value(true))
                .andExpect(jsonPath("$.data.roles").value("admin user"));
        this.mockMvc.perform(get("/api/v1/users").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Found successfully"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(4)));
    }
    @Test
    void findUserById() throws Exception {
        this.mockMvc.perform(get("/api/v1/users/2").header(HttpHeaders.AUTHORIZATION, this.token).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("User found successfully"))
                .andExpect(jsonPath("$.data.id").value(2))
                .andExpect(jsonPath("$.data.username").value("eric"));
    }@Test
    void findUserNotFound() throws Exception {
        this.mockMvc.perform(get("/api/v1/users/5").header(HttpHeaders.AUTHORIZATION, this.token).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find user with id 5 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }
    @Test
    void updateUser() throws Exception {
        UserDto userDto = new UserDto(3,"eric-updated",true,"user");
        String json = this.objectMapper.writeValueAsString(userDto);
        this.mockMvc.perform(put("/api/v1/users/3").contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("User updated successfully"))
                .andExpect(jsonPath("$.data.id").value(userDto.id()))
                .andExpect(jsonPath("$.data.username").value(userDto.username()))
                .andExpect(jsonPath("$.data.enabled").value(userDto.enabled()))
                .andExpect(jsonPath("$.data.roles").value(userDto.roles()));
    }
    @Test
    void updateUserNotFound() throws Exception {
        UserDto userDto = new UserDto(5,"eric-updated",true,"user");
        String json = this.objectMapper.writeValueAsString(userDto);
        this.mockMvc.perform(put("/api/v1/users/5").contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find user with id 5 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }
    @Test
    void deleteUser() throws Exception {
        this.mockMvc.perform(delete("/api/v1/users/3").header(HttpHeaders.AUTHORIZATION, token).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("User deleted successfully"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
    @Test
    void deleteUserNotFound() throws Exception {
        this.mockMvc.perform(delete("/api/v1/users/5").header(HttpHeaders.AUTHORIZATION,token).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find user with id 5 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}
