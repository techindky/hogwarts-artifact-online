package edu.tcu.cs.hogwarts_artifact_online.Artifact.Dto;

import edu.tcu.cs.hogwarts_artifact_online.Wizard.Dto.WizardDto;
import jakarta.validation.constraints.NotEmpty;


public record ArtifactDto (String id,
                           @NotEmpty(message = "name is required.")
                           String name,
                           @NotEmpty(message = "description is required.")
                           String description,
                           @NotEmpty(message = "imageUrl is required")
                           String imageUrl,
                           WizardDto wizard){

}
