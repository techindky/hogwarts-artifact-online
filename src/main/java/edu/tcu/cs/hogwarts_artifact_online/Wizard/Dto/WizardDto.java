package edu.tcu.cs.hogwarts_artifact_online.Wizard.Dto;

import jakarta.validation.constraints.NotEmpty;

public record WizardDto(Integer id,
                        @NotEmpty(message = "name is required")
                        String name,
                        Integer numberofArtifacts) {
}
