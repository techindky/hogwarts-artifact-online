package edu.tcu.cs.hogwarts_artifact_online.Artifact;

import edu.tcu.cs.hogwarts_artifact_online.Artifact.utils.IdWorker;
import edu.tcu.cs.hogwarts_artifact_online.System.Result;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ArtifactService {
    private final ArtifactRepository artifactRepository;

    private final IdWorker idWorker;

    public ArtifactService(ArtifactRepository artifactRepository, IdWorker idWorker){
        this.artifactRepository = artifactRepository;
        this.idWorker = idWorker;
    }
    public Artifact findById(String artifactId){
        return this.artifactRepository.findById(artifactId)
                .orElseThrow(() -> new ArtifactNotFoundException(artifactId));
    }
    public List<Artifact> findAllArtifact(){
        return this.artifactRepository.findAll();
    }
    public Artifact addArtifact(Artifact newArtifact){
        newArtifact.setId(idWorker.nextId() + "");
        return this.artifactRepository.save(newArtifact);
    }
    public Artifact updateArtifact(String artifactId, Artifact update){
        return this.artifactRepository.findById(artifactId)
                .map(oldArtifact -> {
                     oldArtifact.setName(update.getName());
                    oldArtifact.setDescription(update.getDescription());
                    oldArtifact.setImageUrl(update.getImageUrl());
                    return this.artifactRepository.save(oldArtifact);
                })
                .orElseThrow(() -> new ArtifactNotFoundException(artifactId));
    }
    public void deleteArtifact(String artifactId){
        this.artifactRepository.findById(artifactId)
                .orElseThrow(() -> new ArtifactNotFoundException(artifactId));
        this.artifactRepository.deleteById(artifactId);
    }
}
