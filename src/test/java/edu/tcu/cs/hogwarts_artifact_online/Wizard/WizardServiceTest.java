package edu.tcu.cs.hogwarts_artifact_online.Wizard;

import edu.tcu.cs.hogwarts_artifact_online.Artifact.Artifact;
import edu.tcu.cs.hogwarts_artifact_online.Artifact.ArtifactRepository;
import edu.tcu.cs.hogwarts_artifact_online.System.Exception.ObjectNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
//@ActiveProfiles(value = "dev")
public class WizardServiceTest {
    @Mock
    WizardRepository wizardRepository;

    @Mock
    ArtifactRepository artifactRepository;

    @InjectMocks
    WizardService wizardService;

    List<Wizard> wizards;
    @BeforeEach
    void setUp(){
        this.wizards = new ArrayList<>();
        Wizard w1 = new Wizard();
        w1.setId(1);
        w1.setName("Roddy Col");
        this.wizards.add(w1);

        Wizard w2 = new Wizard();
        w2.setId(2);
        w2.setName("Hary Colman");
        this.wizards.add(w2);

        Wizard w3 = new Wizard();
        w3.setId(3);
        w3.setName("Tony Furry");
        this.wizards.add(w3);
    }
    @AfterEach
    void tearDown(){
    }
    @Test
    void findAllWizard(){
        given(this.wizardRepository.findAll()).willReturn(this.wizards);

        List<Wizard> actualWizards = wizardService.findAllWizard();
        assertThat(actualWizards.size()).isEqualTo(this.wizards.size());
        verify(wizardRepository, times(1)).findAll();
    }
    @Test
    void findWizardById(){
        Wizard w2 = new Wizard();
        w2.setId(2);
        w2.setName("Hary Colman");

        given(wizardRepository.findById(2)).willReturn(Optional.of(w2));
        Wizard returnedWizard = wizardService.findById(2);
        assertThat(returnedWizard.getId()).isEqualTo(w2.getId());
        assertThat(returnedWizard.getName()).isEqualTo(w2.getName());
        verify(wizardRepository, times(1)).findById(2);
    }
    @Test
    void findWizardByIdNotFound(){
        given(wizardRepository.findById(Mockito.any(Integer.class))).willReturn(Optional.empty());
        Throwable thrown = catchThrowable(() -> wizardService.findById(2));
        assertThat(thrown).isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find wizard with id 2 :(");
        verify(wizardRepository,times(1)).findById(2);
    }
    @Test
    void addWizard(){
        Wizard w2 = new Wizard();
        w2.setId(2);
        w2.setName("Hary Colman");
        given(wizardRepository.save(w2)).willReturn(w2);

        Wizard savedWizard = wizardService.addWizard(w2);
        assertThat(savedWizard.getId()).isEqualTo(w2.getId());
        assertThat(savedWizard.getName()).isEqualTo(w2.getName());
        verify(wizardRepository, times(1)).save(w2);
    }
    @Test
    void updateWizard(){
        Wizard w2 = new Wizard();
        w2.setId(2);
        w2.setName("Hary Colman");

        Wizard updated = new Wizard();
        updated.setId(2);
        updated.setName("Hary Poly");

        given(wizardRepository.findById(2)).willReturn(Optional.of(w2));
        given(wizardRepository.save(w2)).willReturn(w2);

        Wizard updatedWizard = wizardService.updateWizard(2,updated);
        assertThat(updatedWizard.getId()).isEqualTo(updated.getId());
        assertThat(updatedWizard.getName()).isEqualTo(updated.getName());
        verify(wizardRepository, times(1)).findById(2);
        verify(wizardRepository, times(1)).save(w2);
    }
    @Test
    void updateWizardNotFound(){
        Wizard w2 = new Wizard();
        w2.setId(2);
        w2.setName("Hary Colman");
        given(wizardRepository.findById(2)).willReturn(Optional.empty());
        assertThrows(ObjectNotFoundException.class, () -> wizardService.updateWizard(2,w2));
        verify(wizardRepository, times(1)).findById(2);
    }
    @Test
    void deleteWizard(){
        Wizard w2 = new Wizard();
        w2.setId(2);
        w2.setName("Hary Colman");
        given(wizardRepository.findById(2)).willReturn(Optional.of(w2));
        doNothing().when(wizardRepository).deleteById(2);
        wizardService.deleteWizard(2);
        verify(wizardRepository, times(1)).deleteById(2);
    }
    @Test
    void deleteWizardNotFound(){
        given(wizardRepository.findById(2)).willReturn(Optional.empty());
        assertThrows(ObjectNotFoundException.class, () -> wizardService.deleteWizard(2));
        verify(wizardRepository, times(1)).findById(2);
    }
    @Test
    void assignArtifact(){
        Artifact artifact  = new Artifact();
        artifact.setId("125732919271923");
        artifact.setName("Invisibility Cloak");
        artifact.setDescription("Invisibility Cloak is used to make the wearer invisible");
        artifact.setImageUrl("ImageUrl");

        Wizard w2 = new Wizard();
        w2.setId(2);
        w2.setName("Hary Colman");
        w2.addArtifact(artifact);

        Wizard w3 = new Wizard();
        w3.setId(3);
        w3.setName("Tony Furry");

        given(artifactRepository.findById("125732919271923")).willReturn(Optional.of(artifact));
        given(wizardRepository.findById(2)).willReturn(Optional.of(w2));

        wizardService.assignArtifact(2, "125732919271923");

        assertThat(artifact.getWizard().getId()).isEqualTo(2);
        assertThat(w2.getArtifacts()).contains(artifact);

    }
    @Test
    void assignArtifactNonExistingArtifactId(){

        given(artifactRepository.findById("125732919271923")).willReturn(Optional.empty());

        Throwable thrown = assertThrows(ObjectNotFoundException.class, ()-> wizardService.assignArtifact(2,"125732919271923"));
        assertThat(thrown).isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find artifact with id 125732919271923 :(");
    }
    @Test
    void assignArtifactWithNonExistingWizardId(){
        Artifact artifact  = new Artifact();
        artifact.setId("125732919271923");
        artifact.setName("Invisibility Cloak");
        artifact.setDescription("Invisibility Cloak is used to make the wearer invisible");
        artifact.setImageUrl("ImageUrl");

        Wizard w2 = new Wizard();
        w2.setId(1);
        w2.setName("Hary Colman");
        w2.addArtifact(artifact);


        given(artifactRepository.findById("125732919271923")).willReturn(Optional.of(artifact));
        given(wizardRepository.findById(2)).willReturn(Optional.empty());

        Throwable thrown = assertThrows(ObjectNotFoundException.class, ()-> wizardService.assignArtifact(2,"125732919271923"));
        assertThat(thrown).isInstanceOf(ObjectNotFoundException.class)
                        .hasMessage("Could not find wizard with id 2 :(");
        assertThat(artifact.getWizard().getId()).isEqualTo(1);
    }


}
