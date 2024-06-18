package edu.tcu.cs.hogwarts_artifact_online.Artifact;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tcu.cs.hogwarts_artifact_online.Artifact.Dto.ArtifactDto;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Integration tests for Artifact API endpoint")
@Tag("integration")
public class ArtifactControllerIntegrationTest {
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
        JSONObject jsonObject = new JSONObject(contentAsString);
        this.token = "Bearer " + jsonObject.getJSONObject("data").getString("token");
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void findAllArtifacts() throws Exception {
        this.mockMvc.perform(get("/api/v1/artifacts").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Artifacts Found"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(6)));
    }
    @Test
    @DisplayName("Check addArtifact with valid input (POST)")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void addArtifact() throws Exception {
        Artifact a = new Artifact();
        a.setName("Rememberable");
        a.setDescription("A Rememberable was a magical large marble-sized glass ball that contained a smoke which turned red when its owner or user had forgotten something. It turned clear once whatever was forgotten was remembered");
        a.setImageUrl("ImageUrl");

        String json = this.objectMapper.writeValueAsString(a);

        this.mockMvc.perform(post("/api/v1/artifacts").contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Artifact Added Successfully"))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.name").value("Rememberable"))
                .andExpect(jsonPath("$.data.description").value("A Rememberable was a magical large marble-sized glass ball that contained a smoke which turned red when its owner or user had forgotten something. It turned clear once whatever was forgotten was remembered"))
                .andExpect(jsonPath("$.data.imageUrl").value("ImageUrl"));
        this.mockMvc.perform(get("/api/v1/artifacts").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Artifacts Found"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(7)));
    }
    @Test
    void findArtifactById() throws Exception {
        this.mockMvc.perform(get("/api/v1/artifacts/125732919271923").header(HttpHeaders.AUTHORIZATION, this.token).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Found Successfully"))
                .andExpect(jsonPath("$.data.id").value("125732919271923"))
                .andExpect(jsonPath("$.data.name").value("Invisibility Cloak"))
                .andExpect(jsonPath("$.data.description").value("Invisibility Cloak is used to make the wearer invisible"))
                .andExpect(jsonPath("$.data.imageUrl").value("ImageUrl"));
    }@Test
    void findArtifactNotFound() throws Exception {
        this.mockMvc.perform(get("/api/v1/artifacts/125732919271929").header(HttpHeaders.AUTHORIZATION, this.token).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find artifact with id 125732919271929 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }
    @Test
    void updateArtifact() throws Exception {
        ArtifactDto artifactDto = new ArtifactDto("125732919271923","Invisibility Cloak","A new description","ImageUrl",null);
        String json = this.objectMapper.writeValueAsString(artifactDto);
        this.mockMvc.perform(put("/api/v1/artifacts/125732919271923").contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Artifact Updated Successfully"))
                .andExpect(jsonPath("$.data.id").value(artifactDto.id()))
                .andExpect(jsonPath("$.data.name").value(artifactDto.name()))
                .andExpect(jsonPath("$.data.description").value(artifactDto.description()))
                .andExpect(jsonPath("$.data.imageUrl").value(artifactDto.imageUrl()));
    }
    @Test
    void updateArtifactNotFound() throws Exception {
        ArtifactDto artifactDto = new ArtifactDto("125732919271929","Invisibility Cloak","A new description","ImageUrl",null);
        String json = this.objectMapper.writeValueAsString(artifactDto);
        this.mockMvc.perform(put("/api/v1/artifacts/125732919271929").contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, this.token).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find artifact with id 125732919271929 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }
    @Test
    void deleteArtifact() throws Exception {
        this.mockMvc.perform(delete("/api/v1/artifacts/125732919271923").header(HttpHeaders.AUTHORIZATION, token).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Artifact deleted successfully"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
    @Test
    void deleteArtifactNotFound() throws Exception {
        this.mockMvc.perform(delete("/api/v1/artifacts/125732919271929").header(HttpHeaders.AUTHORIZATION,token).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find artifact with id 125732919271929 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}
