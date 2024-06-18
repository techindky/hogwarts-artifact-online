package edu.tcu.cs.hogwarts_artifact_online.Artifact;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tcu.cs.hogwarts_artifact_online.Artifact.Dto.ArtifactDto;
import edu.tcu.cs.hogwarts_artifact_online.System.Exception.ObjectNotFoundException;
import edu.tcu.cs.hogwarts_artifact_online.System.StatusCode;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class ArtifactControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    ArtifactService artifactService;

    @Autowired
    ObjectMapper objectMapper;

    List<Artifact> artifacts;
    @BeforeEach
    void setUp() {
        this.artifacts = new ArrayList<>();

        Artifact a1 = new Artifact();
        a1.setId("125732919271923");
        a1.setName("Invisibility Cloak");
        a1.setDescription("Invisibility Cloak is used to make the wearer invisible");
        a1.setImageUrl("ImageUrl");
        this.artifacts.add(a1);

        Artifact a2 = new Artifact();
        a2.setId("125732919271924");
        a2.setName("Ring of Power");
        a2.setDescription("The Ring of Power grants immense strength to its wearer");
        a2.setImageUrl("ImageUrl");
        this.artifacts.add(a2);


        Artifact a3 = new Artifact();
        a3.setId("125732919271925");
        a3.setName("Staff of Wisdom");
        a3.setDescription("The Staff of Wisdom enhances the magical abilities of its wielder");
        a3.setImageUrl("ImageUrl");
        this.artifacts.add(a3);


        Artifact a4 = new Artifact();
        a4.setId("125732919271926");
        a4.setName("Boots of Swiftness");
        a4.setDescription("Boots of Swiftness increase the speed and agility of the wearer");
        a4.setImageUrl("ImageUrl");
        this.artifacts.add(a4);


        Artifact a5 = new Artifact();
        a5.setId("125732919271927");
        a5.setName("Shield of Invincibility");
        a5.setDescription("The Shield of Invincibility provides unparalleled defense in battle");
        a5.setImageUrl("ImageUrl");
        this.artifacts.add(a5);


        Artifact a6 = new Artifact();
        a6.setId("125732919271928");
        a6.setName("Helm of Insight");
        a6.setDescription("The Helm of Insight grants the ability to see through illusions and deceptions");
        a6.setImageUrl("ImageUrl");
        this.artifacts.add(a6);


    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindByArtifactId() throws Exception {
        //Given.
        given(this.artifactService.findById("125732919271924")).willReturn(this.artifacts.get(1));
        //When and then.
        this.mockMvc.perform(get("/api/v1/artifacts/125732919271924").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Found Successfully"))
                .andExpect(jsonPath("$.data.id").value("125732919271924"))
                .andExpect(jsonPath("$.data.name").value("Ring of Power"));

    }
    @Test
    void testFindByIdNotFound() throws Exception {
        //Given.
        given(this.artifactService.findById("125732919271923")).willThrow(new ObjectNotFoundException("artifact","125732919271923"));
        //When and then.
        this.mockMvc.perform(get("/api/v1/artifacts/125732919271923").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find artifact with id 125732919271923 :("))
                .andExpect(jsonPath("$.data").isEmpty());

    }
    @Test
    void findAllArtifacts() throws Exception {
        given(this.artifactService.findAllArtifact()).willReturn(this.artifacts);
        this.mockMvc.perform(get("/api/v1/artifacts").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Artifacts Found"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(this.artifacts.size())))
                .andExpect(jsonPath("$.data[0].id").value("125732919271923"))
                .andExpect(jsonPath("$.data[0].name").value("Invisibility Cloak"))
                .andExpect(jsonPath("$.data[1].id").value("125732919271924"))
                .andExpect(jsonPath("$.data[1].name").value("Ring of Power"));

    }
    @Test
    void addArtifact() throws Exception {
        ArtifactDto artifactDto = new ArtifactDto(null,"Remebrall","Remembering artifact","ImageUrl",null);
        String json = this.objectMapper.writeValueAsString(artifactDto);
        Artifact savedArtifact = new Artifact();
        savedArtifact.setId("125732919271929");
        savedArtifact.setName("Remebrall");
        savedArtifact.setDescription("Remembering artifact");
        savedArtifact.setImageUrl("ImageUrl");

        given(this.artifactService.addArtifact(Mockito.any(Artifact.class))).willReturn(savedArtifact);
        this.mockMvc.perform(post("/api/v1/artifacts").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Artifact Added Successfully"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.name").value(savedArtifact.getName()))
                .andExpect(jsonPath("$.data.description").value(savedArtifact.getDescription()))
                .andExpect(jsonPath("$.data.imageUrl").value(savedArtifact.getImageUrl()));
    }
    @Test
    void updateArtifact() throws Exception {
        ArtifactDto artifactDto = new ArtifactDto("125732919271929","Invisibility Cloak","A new description","ImageUrl",null);
        String json = this.objectMapper.writeValueAsString(artifactDto);
        Artifact updated = new Artifact();
        updated.setId("125732919271929");
        updated.setName("Invisibility Cloak");
        updated.setDescription("A new description");
        updated.setImageUrl("ImageUrl");

        given(this.artifactService.updateArtifact(eq("125732919271929"),Mockito.any(Artifact.class))).willReturn(updated);
        this.mockMvc.perform(put("/api/v1/artifacts/125732919271929").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Artifact Updated Successfully"))
                .andExpect(jsonPath("$.data.id").value("125732919271929"))
                .andExpect(jsonPath("$.data.name").value(updated.getName()))
                .andExpect(jsonPath("$.data.description").value(updated.getDescription()))
                .andExpect(jsonPath("$.data.imageUrl").value(updated.getImageUrl()));
    }
    @Test
    void updateArtifactErrorWithNonExistenceId() throws Exception{
        ArtifactDto artifactDto = new ArtifactDto("125732919271929","Invisibility Cloak","A new description","ImageUrl",null);
        String json = this.objectMapper.writeValueAsString(artifactDto);

        given(this.artifactService.updateArtifact(eq("125732919271929"), Mockito.any(Artifact.class))).willThrow(new ObjectNotFoundException("artifact","125732919271929"));

        this.mockMvc.perform(put("/api/v1/artifacts/125732919271929").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find artifact with id 125732919271929 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }
    @Test
    void deleteArtifact() throws Exception{
        Mockito.doNothing().when(this.artifactService).deleteArtifact("125732919271923");
        this.mockMvc.perform(delete("/api/v1/artifacts/125732919271923").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Artifact deleted successfully"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
    @Test
    void deleteArtifactNotFound() throws Exception{
        doThrow(new ObjectNotFoundException("artifact","125732919271923")).when(this.artifactService).deleteArtifact("125732919271923");
        this.mockMvc.perform(delete("/api/v1/artifacts/125732919271923").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find artifact with id 125732919271923 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}