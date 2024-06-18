package edu.tcu.cs.hogwarts_artifact_online.Wizard;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tcu.cs.hogwarts_artifact_online.System.StatusCode;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
//import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Integration test for Wizard API endpoint")
@Tag("Integration")
//@ActiveProfiles(value = "dev")

public class WizardControllerIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    String token;
    @BeforeEach
    void setUp() throws Exception{
        ResultActions resultActions = this.mockMvc.perform(post("/api/v1/users/login")
                .with(httpBasic("tony","123456")));
        MvcResult mvcResult = resultActions.andDo(print()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        JSONObject json = new JSONObject(contentAsString);
        token = "Bearer " + json.getJSONObject("data").getString("token");
    }
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void findAllWizard() throws Exception {
        mockMvc.perform(get("/api/v1/wizard").header(HttpHeaders.AUTHORIZATION, token).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Found Successfully"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(3)));
    }
    @Test
    @DisplayName("Check addWizard with valid input (POST)")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void addWizard() throws Exception {
        Wizard w = new Wizard();
        w.setName("ElectWizard");

        String json = objectMapper.writeValueAsString(w);
        mockMvc.perform(post("/api/v1/wizard").contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, token).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Wizard added successfully"))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.name").value("ElectWizard"));
        mockMvc.perform(get("/api/v1/wizard").header(HttpHeaders.AUTHORIZATION, token).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Found Successfully"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(4)));
    }
    @Test
    void updateWizardById() throws Exception {
        Wizard w = new Wizard();
        w.setName("ElectWizard");

        String json = objectMapper.writeValueAsString(w);
        mockMvc.perform(put("/api/v1/wizard/2").contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, token).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Wizard updated successfully"))
                .andExpect(jsonPath("$.data.id").value(2))
                .andExpect(jsonPath("$.data.name").value(w.getName()));
    }
    @Test
    void updateWizardWithNonExistingId() throws Exception {
        Wizard w = new Wizard();
        w.setName("ElectWizard");

        String json = objectMapper.writeValueAsString(w);
        mockMvc.perform(put("/api/v1/wizard/5").contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, token).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find wizard with id 5 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }
    @Test
    void deleteWizard() throws Exception {
        mockMvc.perform(delete("/api/v1/wizard/2").header(HttpHeaders.AUTHORIZATION, token).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Wizard deleted successfully"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
    @Test
    void deleteWizardNotFound() throws Exception {
        mockMvc.perform(delete("/api/v1/wizard/5").header(HttpHeaders.AUTHORIZATION, token).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find wizard with id 5 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}
