package edu.tcu.cs.hogwarts_artifact_online.Wizard;

import edu.tcu.cs.hogwarts_artifact_online.Artifact.Artifact;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@Entity
public class Wizard {
    @Id
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
}
