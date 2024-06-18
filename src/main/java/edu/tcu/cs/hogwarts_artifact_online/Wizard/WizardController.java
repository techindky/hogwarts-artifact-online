package edu.tcu.cs.hogwarts_artifact_online.Wizard;

import edu.tcu.cs.hogwarts_artifact_online.System.Result;
import edu.tcu.cs.hogwarts_artifact_online.System.StatusCode;
import edu.tcu.cs.hogwarts_artifact_online.Wizard.Converter.WizardDtoToWizardConverter;
import edu.tcu.cs.hogwarts_artifact_online.Wizard.Converter.WizardtoWizardDtoConverter;
import edu.tcu.cs.hogwarts_artifact_online.Wizard.Dto.WizardDto;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/wizard")
public class WizardController {
    private final WizardService wizardService;

    private final WizardtoWizardDtoConverter wizardtoWizardDtoConverter;

    private final WizardDtoToWizardConverter wizardDtoToWizardConverter;

    public WizardController(WizardService wizardService, WizardtoWizardDtoConverter wizardtoWizardDtoConverter, WizardDtoToWizardConverter wizardDtoToWizardConverter) {
        this.wizardService = wizardService;
        this.wizardtoWizardDtoConverter = wizardtoWizardDtoConverter;
        this.wizardDtoToWizardConverter = wizardDtoToWizardConverter;
    }
    @GetMapping
    public Result findAllWizards(){
        List<Wizard> wizards = wizardService.findAllWizard();
        List<WizardDto> wizardDtos = wizards.stream().map(this.wizardtoWizardDtoConverter::convert).collect(Collectors.toList());
        return new Result(true, StatusCode.SUCCESS,"Found Successfully", wizardDtos);
    }
    @GetMapping("/{wizardId}")
    public Result findWizardById(@PathVariable Integer wizardId){
        Wizard wizard = wizardService.findById(wizardId);
        WizardDto wizardDto = this.wizardtoWizardDtoConverter.convert(wizard);
        return new Result(true, StatusCode.SUCCESS, "Wizard found successfully", wizardDto);
    }
    @PostMapping
    public Result addWizard(@Valid @RequestBody WizardDto wizardDto){
        Wizard wizard = this.wizardDtoToWizardConverter.convert(wizardDto);
        Wizard savedWizard = this.wizardService.addWizard(wizard);
        WizardDto savedWizardDto = this.wizardtoWizardDtoConverter.convert(savedWizard);
        return new Result(true, StatusCode.SUCCESS, "Wizard added successfully", savedWizardDto);
    }
    @PutMapping("/{wizardId}")
    public Result updateWizard(@PathVariable Integer wizardId, @Valid @RequestBody WizardDto wizardDto){
        Wizard wizard = this.wizardDtoToWizardConverter.convert(wizardDto);
        Wizard updatedWizard = this.wizardService.updateWizard(wizardId, wizard);
        WizardDto updatedWizardDto = this.wizardtoWizardDtoConverter.convert(updatedWizard);
        return new Result(true, StatusCode.SUCCESS, "Wizard updated successfully", updatedWizardDto);
    }
    @DeleteMapping("/{wizardId}")
    public Result deleteWizardById(@PathVariable Integer wizardId){
        this.wizardService.deleteWizard(wizardId);
        return new Result(true, StatusCode.SUCCESS, "Wizard deleted successfully");
    }
    @PutMapping("/{wizardId}/artifacts/{artifactId}")
    public Result assignArtifact(@PathVariable Integer wizardId, @PathVariable String artifactId){
        this.wizardService.assignArtifact(wizardId, artifactId);
        return new Result(true, StatusCode.SUCCESS, "Artifact assigned successfully");
    }
}
