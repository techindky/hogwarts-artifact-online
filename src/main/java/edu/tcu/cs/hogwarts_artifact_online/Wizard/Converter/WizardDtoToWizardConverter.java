package edu.tcu.cs.hogwarts_artifact_online.Wizard.Converter;

import edu.tcu.cs.hogwarts_artifact_online.Wizard.Dto.WizardDto;
import edu.tcu.cs.hogwarts_artifact_online.Wizard.Wizard;
import jakarta.persistence.Convert;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class WizardDtoToWizardConverter implements Converter<WizardDto, Wizard> {
    @Override
    public Wizard convert(WizardDto source) {
        Wizard wizard = new Wizard();
        wizard.setId(source.id());
        wizard.setName(source.name());
        return wizard;
    }
}
