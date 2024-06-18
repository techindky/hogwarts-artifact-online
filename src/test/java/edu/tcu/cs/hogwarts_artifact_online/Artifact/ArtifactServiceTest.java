package edu.tcu.cs.hogwarts_artifact_online.Artifact;

import edu.tcu.cs.hogwarts_artifact_online.Artifact.utils.IdWorker;
import edu.tcu.cs.hogwarts_artifact_online.System.Exception.ObjectNotFoundException;
import edu.tcu.cs.hogwarts_artifact_online.Wizard.Wizard;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ArtifactServiceTest {
    @Mock
    ArtifactRepository artifactRepository;
    @Mock
    IdWorker idWorker;
    @InjectMocks
    ArtifactService artifactService;

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
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findByArtifactId() {
        //Given. Arrange inputs and targets. Define the behaviour of Mock object artifactRepository.
        Artifact a = new Artifact();
        a.setId("125732919271923");
        a.setName("Invisibility Cloak");
        a.setDescription("Invisibility Cloak is used to make the wearer invisible");
        a.setImageUrl("ImageUrl");

        Wizard w = new Wizard();
        w.setId(2);
        w.setName("Harry Potter");

        a.setWizard(w);
        given(artifactRepository.findById("125732919271923")).willReturn(Optional.of(a));//Defines the behaviour of Mock object.

        //When. Act on the target behaviour. When steps should cover the method to be tested.
        Artifact returnedArtifact = artifactService.findById("125732919271923");

        //Then. Assert expected outcomes.
        assertThat(returnedArtifact.getId()).isEqualTo(a.getId());
        assertThat(returnedArtifact.getName()).isEqualTo(a.getName());
        assertThat(returnedArtifact.getDescription()).isEqualTo(a.getDescription());
        assertThat(returnedArtifact.getImageUrl()).isEqualTo(a.getImageUrl());
        verify(artifactRepository, times(1)).findById("125732919271923");

    }
    @Test
    void findByIdNotFound(){
        //Given
        given(artifactRepository.findById(Mockito.any(String.class))).willReturn(Optional.empty());
        //When
        Throwable thrown = catchThrowable(() -> artifactService.findById("125732919271923"));
        //Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find artifact with id 125732919271923 :(");
        verify(artifactRepository, times(1)).findById("125732919271923");
    }
    @Test
    void findAllArtifacts(){
        given(artifactRepository.findAll()).willReturn(this.artifacts);

        List<Artifact> actualArtifact = artifactService.findAllArtifact();

        assertThat(actualArtifact.size()).isEqualTo(this.artifacts.size());
        verify(artifactRepository, times(1)).findAll();
    }
    @Test
    void addArtifact(){
        Artifact newArtifact = new Artifact();
        newArtifact.setName("Artifact 3");
        newArtifact.setDescription(("Description..."));
        newArtifact.setImageUrl("ImageUrl");

        given(idWorker.nextId()).willReturn(123456L);
        given(artifactRepository.save(newArtifact)).willReturn(newArtifact);

        Artifact savedArtifact = artifactService.addArtifact(newArtifact);
        assertThat(savedArtifact.getId()).isEqualTo("123456");
        assertThat(savedArtifact.getName()).isEqualTo(newArtifact.getName());
        assertThat(savedArtifact.getDescription()).isEqualTo(newArtifact.getDescription());
        assertThat(savedArtifact.getImageUrl()).isEqualTo(newArtifact.getImageUrl());
        verify(artifactRepository, times(1)).save(newArtifact);

    }
    @Test
    void updateArtifact() {
        Artifact oldArtifact = new Artifact();
        oldArtifact.setId("125732919271923");
        oldArtifact.setName("Invisibility Cloak");
        oldArtifact.setDescription("Invisibility Cloak is used to make the wearer invisible");
        oldArtifact.setImageUrl("ImageUrl");

        Artifact updated = new Artifact();
        updated.setId("125732919271923");
        updated.setName("Invisibility Cloak");
        updated.setDescription("A new description");
        updated.setImageUrl("ImageUrl");

        given(this.artifactRepository.findById("125732919271923")).willReturn(Optional.of(oldArtifact));
        given(this.artifactRepository.save(oldArtifact)).willReturn(oldArtifact);

        Artifact updatedArtifact = artifactService.updateArtifact("125732919271923", updated);
        assertThat(updatedArtifact.getId()).isEqualTo(updated.getId());
        assertThat(updatedArtifact.getDescription()).isEqualTo(updated.getDescription());
        verify(artifactRepository, times(1)).findById("125732919271923");
        verify(artifactRepository, times(1)).save(oldArtifact);
    }

    @Test
    void updateArtifactNotFound(){
        Artifact updated = new Artifact();
        updated.setName("Invisibility Cloak");
        updated.setDescription("A new description");
        updated.setImageUrl("ImageUrl");

        given(artifactRepository.findById("125732919271923")).willReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> artifactService.updateArtifact("125732919271923",updated));
        verify(this.artifactRepository, times(1)).findById("125732919271923");
    }
    @Test
    void deleteArtifact(){
        Artifact artifact = new Artifact();
        artifact.setId("125732919271923");
        artifact.setName("Invisibility Cloak");
        artifact.setDescription("Invisibility Cloak is used to make the wearer invisible");
        artifact.setImageUrl("ImageUrl");

        given(this.artifactRepository.findById("125732919271923")).willReturn(Optional.of(artifact));
        Mockito.doNothing().when(artifactRepository).deleteById("125732919271923");
        artifactService.deleteArtifact("125732919271923");

        verify(artifactRepository, times(1)).deleteById("125732919271923");
    }
    @Test
    void deleteArtifactNotFound(){
        given(this.artifactRepository.findById("125732919271923")).willReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, ()-> artifactService.deleteArtifact("125732919271923"));

        verify(artifactRepository, times(1)).findById("125732919271923");

    }
}