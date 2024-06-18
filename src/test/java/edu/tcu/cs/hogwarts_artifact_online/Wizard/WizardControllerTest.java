package edu.tcu.cs.hogwarts_artifact_online.Wizard;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tcu.cs.hogwarts_artifact_online.System.StatusCode;
import edu.tcu.cs.hogwarts_artifact_online.Artifact.Artifact;
import edu.tcu.cs.hogwarts_artifact_online.System.Exception.ObjectNotFoundException;
import edu.tcu.cs.hogwarts_artifact_online.Wizard.Dto.WizardDto;
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
//import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
//@ActiveProfiles(value = "dev")
public class WizardControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    WizardService wizardService;

    @Autowired
    ObjectMapper objectMapper;

    List<Wizard> wizards;
    List<Artifact> artifacts;
    @BeforeEach
    void setUp(){
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


        this.wizards = new ArrayList<>();
        Wizard w1 = new Wizard();
        w1.setId(1);
        w1.setName("Roddy Col");
        w1.addArtifact(a1);
        w1.addArtifact(a3);
        this.wizards.add(w1);

        Wizard w2 = new Wizard();
        w2.setId(2);
        w2.setName("Hary Colman");
        w1.addArtifact(a4);
        w1.addArtifact(a2);
        this.wizards.add(w2);

        Wizard w3 = new Wizard();
        w3.setId(3);
        w3.setName("Tony Furry");
        w1.addArtifact(a5);
        this.wizards.add(w3);
    }
    @AfterEach
    void tearDown(){
    }
    @Test
    void findAllWizards() throws Exception {
        given(this.wizardService.findAllWizard()).willReturn(this.wizards);
        this.mockMvc.perform(get("http://localhost:8080/api/v1/wizard").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Found Successfully"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(this.wizards.size())))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].name").value("Roddy Col"))
                .andExpect(jsonPath("$.data[1].id").value(2))
                .andExpect(jsonPath("$.data[1].name").value("Hary Colman"));
    }
    @Test
    void findWizardById() throws Exception {
        given(this.wizardService.findById(1)).willReturn(this.wizards.get(0));
        this.mockMvc.perform(get("http://localhost:8080/api/v1/wizard/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Wizard found successfully"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Roddy Col"));
    }
    @Test
    void findWizardNotFound() throws Exception {
        given(this.wizardService.findById(5)).willThrow(new ObjectNotFoundException("wizard", 5));
        this.mockMvc.perform(get("http://localhost:8080/api/v1/wizard/5").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find wizard with id 5 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }
    @Test
    void addWizard() throws Exception {
        WizardDto wizardDto = new WizardDto(null,"Jo kar",0);
        String json = objectMapper.writeValueAsString(wizardDto);
        Wizard w3 = new Wizard();
        w3.setId(3);
        w3.setName("Tony Furry");
        given(wizardService.addWizard(Mockito.any(Wizard.class))).willReturn(w3);
        this.mockMvc.perform(post("http://localhost:8080/api/v1/wizard").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Wizard added successfully"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.name").value(w3.getName()));
    }
    @Test
    void updateWizard() throws Exception {
        WizardDto wizardDto = new WizardDto(3,"Hary Colman",0);
        String json = objectMapper.writeValueAsString(wizardDto);
        Wizard w3 = new Wizard();
        w3.setId(3);
        w3.setName("Tony Furry");
        given(this.wizardService.updateWizard(eq(3),Mockito.any(Wizard.class))).willReturn(w3);
        this.mockMvc.perform(put("http://localhost:8080/api/v1/wizard/3").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Wizard updated successfully"))
                .andExpect(jsonPath("$.data.id").value(3))
                .andExpect(jsonPath("$.data.name").value(w3.getName()));
    }
    @Test
    void updateWizardNotFound() throws Exception {
        WizardDto wizardDto = new WizardDto(3,"Hary Colman",0);
        String json = objectMapper.writeValueAsString(wizardDto);
        given(this.wizardService.updateWizard(eq(3),Mockito.any(Wizard.class))).willThrow(new ObjectNotFoundException("wizard", 3));

        this.mockMvc.perform(put("http://localhost:8080/api/v1/wizard/3").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find wizard with id 3 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }
    @Test
    void deleteWizard() throws Exception {
        Mockito.doNothing().when(this.wizardService).deleteWizard(3);
        this.mockMvc.perform(delete("http://localhost:8080/api/v1/wizard/3").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Wizard deleted successfully"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
    @Test
    void deleteWizardNotFound() throws Exception {
        doThrow(new ObjectNotFoundException("wizard", 3)).when(this.wizardService).deleteWizard(3);
        this.mockMvc.perform(delete("http://localhost:8080/api/v1/wizard/3").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find wizard with id 3 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }
    @Test
    void assignArtifact() throws Exception {
        doNothing().when(this.wizardService).assignArtifact(2, "125732919271926");
        this.mockMvc.perform(put("http://localhost:8080/api/v1/wizard/2/artifacts/125732919271926").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Artifact assigned successfully"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
    @Test
    void assignArtifactWithNonExistingArtifactId() throws Exception {
        doThrow(new ObjectNotFoundException("artifact","125732919271929")).when(this.wizardService).assignArtifact(2,"125732919271929");
        this.mockMvc.perform(put("http://localhost:8080/api/v1/wizard/2/artifacts/125732919271929").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find artifact with id 125732919271929 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }
    @Test
    void assignArtifactWithNonExistingWizardId() throws Exception {
        doThrow(new ObjectNotFoundException("wizard", 5)).when(this.wizardService).assignArtifact(5,"125732919271926");
        this.mockMvc.perform(put("http://localhost:8080/api/v1/wizard/5/artifacts/125732919271926").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find wizard with id 5 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}
