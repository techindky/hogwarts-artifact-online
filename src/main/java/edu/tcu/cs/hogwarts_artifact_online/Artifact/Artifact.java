package edu.tcu.cs.hogwarts_artifact_online.Artifact;

import edu.tcu.cs.hogwarts_artifact_online.Wizard.Wizard;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Artifact {
    @Id
    private String id;
    private String name;
    private String description;
    private String imageUrl;

    @ManyToOne
    private Wizard wizard;

    public Artifact(){}
}
