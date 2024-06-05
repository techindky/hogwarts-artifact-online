package edu.tcu.cs.hogwarts_artifact_online.Wizard.Converter;

import edu.tcu.cs.hogwarts_artifact_online.Wizard.Dto.WizardDto;
import edu.tcu.cs.hogwarts_artifact_online.Wizard.Wizard;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class WizardtoWizardDtoConverter implements Converter<Wizard, WizardDto> {
    @Override
    public WizardDto convert(Wizard source) {
        WizardDto wizardDto = new WizardDto(source.getId(), source.getName(), source.getNumberofArtifacts());
        return wizardDto;
    }
}
