package edu.tcu.cs.hogwarts_artifact_online.HogwartsUser;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tcu.cs.hogwarts_artifact_online.HogwartsUser.Dto.UserDto;
import edu.tcu.cs.hogwarts_artifact_online.System.Exception.ObjectNotFoundException;
import edu.tcu.cs.hogwarts_artifact_online.System.StatusCode;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;



    List<HogwartsUser> users;

    @BeforeEach
    void setUp() {
        users = new ArrayList<>();
        HogwartsUser u1 = new HogwartsUser();
        u1.setId(1);
        u1.setUsername("tony");
        u1.setPassword("123456");
        u1.setEnabled(true);
        u1.setRoles("admin user");
        users.add(u1);

        HogwartsUser u2 = new HogwartsUser();
        u2.setId(2);
        u2.setUsername("eric");
        u2.setPassword("654321");
        u2.setEnabled(true);
        u2.setRoles("user");
        users.add(u2);

        HogwartsUser u3 = new HogwartsUser();
        u3.setId(3);
        u3.setUsername("tom");
        u3.setPassword("qwerty");
        u3.setEnabled(false);
        u3.setRoles("user");
        users.add(u3);
    }

    @Test
    void findUserById() throws Exception {
        given(userService.findUserById(1)).willReturn(users.get(0));
        mockMvc.perform(get("/api/v1/users/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("User found successfully"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.username").value("tony"));
    }
    @Test
    void findUserNotFound() throws Exception {
        given(userService.findUserById(4)).willThrow(new ObjectNotFoundException("user",4));
        mockMvc.perform(get("/api/v1/users/4").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find user with id 4 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }
    @Test
    void findAllUsers() throws Exception {
        given(userService.findAllUsers()).willReturn(users);
        mockMvc.perform(get("/api/v1/users").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Found successfully"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(3)))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].username").value("tony"))
                .andExpect(jsonPath("$.data[1].id").value(2))
                .andExpect(jsonPath("$.data[1].username").value("eric"));
    }
    @Test
    void addUser() throws Exception {
        HogwartsUser u4 = new HogwartsUser();
        u4.setId(4);
        u4.setUsername("jerry");
        u4.setPassword("qwerty");
        u4.setEnabled(true);
        u4.setRoles("user");

        String json = objectMapper.writeValueAsString(u4);

        given(userService.addUser(Mockito.any(HogwartsUser.class))).willReturn(u4);

        mockMvc.perform(post("/api/v1/users").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("User added successfully"))
                .andExpect(jsonPath("$.data.id").value(4))
                .andExpect(jsonPath("$.data.username").value("jerry"))
                .andExpect(jsonPath("$.data.enabled").value(true))
                .andExpect(jsonPath("$.data.roles").value("user"));
    }
    @Test
    void updateUser() throws Exception {
        UserDto userDto = new UserDto(2,"jerry",true,"admin user");
        HogwartsUser updatedUser = new HogwartsUser();
        updatedUser.setId(2);
        updatedUser.setUsername("jerry");
        updatedUser.setEnabled(true);
        updatedUser.setRoles("admin user");

        String json = objectMapper.writeValueAsString(userDto);

        given(userService.update(eq(2),Mockito.any(HogwartsUser.class))).willReturn(updatedUser);
        mockMvc.perform(put("/api/v1/users/2").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("User updated successfully"))
                .andExpect(jsonPath("$.data.id").value(2))
                .andExpect(jsonPath("$.data.username").value("jerry"))
                .andExpect(jsonPath("$.data.enabled").value(true))
                .andExpect(jsonPath("$.data.roles").value("admin user"));
    }
    @Test
    void updateUserNotFound() throws Exception {
        UserDto userDto = new UserDto(4,"jerry",true,"admin user");
        String json = objectMapper.writeValueAsString(userDto);
        given(userService.update(eq(4),Mockito.any(HogwartsUser.class))).willThrow(new ObjectNotFoundException("user", 4));
        mockMvc.perform(put("/api/v1/users/4").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find user with id 4 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }
    @Test
    void deleteUser() throws Exception {
        doNothing().when(userService).delete(3);
        mockMvc.perform(delete("/api/v1/users/3").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("User deleted successfully"));
    }
    @Test
    void deleteUserNotFound() throws Exception {
        doThrow(new ObjectNotFoundException("user", 4)).when(userService).delete(4);
        mockMvc.perform(delete("/api/v1/users/4").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find user with id 4 :("));
    }
}
