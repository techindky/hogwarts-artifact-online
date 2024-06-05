package edu.tcu.cs.hogwarts_artifact_online.Artifact.Converter;

import edu.tcu.cs.hogwarts_artifact_online.Artifact.Artifact;
import edu.tcu.cs.hogwarts_artifact_online.Artifact.Dto.ArtifactDto;
import edu.tcu.cs.hogwarts_artifact_online.Wizard.Converter.WizardtoWizardDtoConverter;
import edu.tcu.cs.hogwarts_artifact_online.Wizard.Dto.WizardDto;
import edu.tcu.cs.hogwarts_artifact_online.Wizard.Wizard;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ArtifacttoArtifactDtoConverter implements Converter<Artifact, ArtifactDto> {
    private final WizardtoWizardDtoConverter wizardtoWizardDtoConverter;

    public ArtifacttoArtifactDtoConverter(WizardtoWizardDtoConverter wizardtoWizardDtoConverter) {
        this.wizardtoWizardDtoConverter = wizardtoWizardDtoConverter;
    }

    @Override
    public ArtifactDto convert(Artifact source) {
        ArtifactDto artifactDto = new ArtifactDto(source.getId(), source.getName(),
                source.getDescription(), source.getImageUrl(),
                source.getWizard() != null ? wizardtoWizardDtoConverter.convert(source.getWizard()):null);
        return artifactDto;
    }
}
