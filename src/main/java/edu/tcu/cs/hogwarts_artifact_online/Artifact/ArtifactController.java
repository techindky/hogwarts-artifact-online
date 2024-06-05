package edu.tcu.cs.hogwarts_artifact_online.Artifact;

import edu.tcu.cs.hogwarts_artifact_online.Artifact.Converter.ArtifactDtoToArtifactConverter;
import edu.tcu.cs.hogwarts_artifact_online.Artifact.Converter.ArtifacttoArtifactDtoConverter;
import edu.tcu.cs.hogwarts_artifact_online.Artifact.Dto.ArtifactDto;
import edu.tcu.cs.hogwarts_artifact_online.System.Result;
import edu.tcu.cs.hogwarts_artifact_online.System.StatusCode;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ArtifactController {
    private final ArtifactService artifactService;

    private final ArtifacttoArtifactDtoConverter artifacttoArtifactDtoConverter;

    private final ArtifactDtoToArtifactConverter artifactDtoToArtifactConverter;

    public ArtifactController(ArtifactService artifactService, ArtifacttoArtifactDtoConverter artifacttoArtifactDtoConverter, ArtifactDtoToArtifactConverter artifactDtoToArtifactConverter){
        this.artifactService = artifactService;
        this.artifacttoArtifactDtoConverter = artifacttoArtifactDtoConverter;
        this.artifactDtoToArtifactConverter = artifactDtoToArtifactConverter;
    }
    @GetMapping("/api/v1/artifacts/{artifactId}")
    public Result findByArtifactId(@PathVariable String artifactId){
        Artifact foundArtifact = this.artifactService.findById(artifactId);
        ArtifactDto artifactDto = this.artifacttoArtifactDtoConverter.convert(foundArtifact);
        return new Result(true, StatusCode.SUCCESS,"Found Successfully",artifactDto);
    }
    @GetMapping("/api/v1/artifacts")
    public Result findAllArtifacts(){
        List<Artifact> foundArtifacts = this.artifactService.findAllArtifact();
        List<ArtifactDto> artifactDtos = foundArtifacts.stream().map(this.artifacttoArtifactDtoConverter::convert).collect(Collectors.toList());
        return new Result(true, StatusCode.SUCCESS, "Artifacts Found", artifactDtos);
    }
    @PostMapping("/api/v1/artifacts")
    public Result addArtifact(@Valid @RequestBody ArtifactDto artifactDto){
        Artifact newArtifact = this.artifactDtoToArtifactConverter.convert(artifactDto);
        Artifact savedArtifact = this.artifactService.addArtifact(newArtifact);
        ArtifactDto savedArtifactDto = this.artifacttoArtifactDtoConverter.convert(savedArtifact);
        return new Result(true, StatusCode.SUCCESS, "Artifact Added Successfully", savedArtifactDto);
    }
    @PutMapping("/api/v1/artifacts/{artifactId}")
    public Result updateArtifact(@PathVariable String artifactId, @Valid @RequestBody ArtifactDto artifactDto){
        Artifact update = this.artifactDtoToArtifactConverter.convert(artifactDto);
        Artifact updatedArtifact = this.artifactService.updateArtifact(artifactId, update);
        ArtifactDto updatedArtifactDto = this.artifacttoArtifactDtoConverter.convert(updatedArtifact);
        return new Result(true,StatusCode.SUCCESS,"Artifact Updated Successfully",updatedArtifactDto);
    }
    @DeleteMapping("/api/v1/artifacts/{artifactId}")
    public Result deleteArtifact(@PathVariable String artifactId){
        this.artifactService.deleteArtifact(artifactId);
        return new Result(true,StatusCode.SUCCESS,"Artifact deleted successfully");
    }
}
