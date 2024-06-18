package edu.tcu.cs.hogwarts_artifact_online.Wizard;

import edu.tcu.cs.hogwarts_artifact_online.Artifact.Artifact;
import edu.tcu.cs.hogwarts_artifact_online.Artifact.ArtifactRepository;
import edu.tcu.cs.hogwarts_artifact_online.System.Exception.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class WizardService {
    private final WizardRepository wizardRepository;

    private final ArtifactRepository artifactRepository;

    public WizardService(WizardRepository wizardRepository, ArtifactRepository artifactRepository) {
        this.wizardRepository = wizardRepository;
        this.artifactRepository = artifactRepository;
    }

    public List<Wizard> findAllWizard(){
        return this.wizardRepository.findAll();
    }
    public Wizard findById(Integer wizardId){
        return this.wizardRepository.findById(wizardId)
                .orElseThrow(() -> new ObjectNotFoundException("wizard", wizardId));
    }
    public Wizard addWizard(Wizard newWizard){
        return this.wizardRepository.save(newWizard);
    }
    public Wizard updateWizard(Integer wizardId, Wizard wizard){
        return this.wizardRepository.findById(wizardId)
                .map(oldWizard -> {
                    oldWizard.setName(wizard.getName());
                    return this.wizardRepository.save(oldWizard);
                })
                .orElseThrow(() -> new ObjectNotFoundException("wizard", wizardId));
    }
    public void deleteWizard(Integer wizardId){
        Wizard wizard = this.wizardRepository.findById(wizardId)
                .orElseThrow(() -> new ObjectNotFoundException("wizard", wizardId));
        wizard.removeAllArtifacts();
        this.wizardRepository.deleteById(wizardId);
    }
    public void assignArtifact(Integer wizardId, String artifactId){
        Artifact artifact = this.artifactRepository.findById(artifactId)
                .orElseThrow(() -> new ObjectNotFoundException("artifact", artifactId));
        Wizard wizard = this.wizardRepository.findById(wizardId)
                .orElseThrow(() -> new ObjectNotFoundException("wizard", wizardId));
        if(artifact.getWizard()!=null){
            artifact.getWizard().removeArtifact(artifact);
        }
        wizard.addArtifact(artifact);
    }
}
