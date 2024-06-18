package edu.tcu.cs.hogwarts_artifact_online.Wizard;

import edu.tcu.cs.hogwarts_artifact_online.Artifact.Artifact;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@Entity
public class Wizard implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "wizard")
    private List<Artifact> artifacts = new ArrayList<>();

    public Wizard(){}


    public void addArtifact(Artifact artifact) {
        artifact.setWizard(this);
        this.artifacts.add(artifact);
    }

    public Integer getNumberofArtifacts() {
        return this.artifacts.size();
    }
    public void removeAllArtifacts() {
        this.artifacts.stream().forEach(artifact -> artifact.setWizard(null));
        this.artifacts = new ArrayList<>();
    }
    public void removeArtifact(Artifact artifactToBeAssigned) {
        artifactToBeAssigned.setWizard(null);
        this.artifacts.remove(artifactToBeAssigned);
    }
}
