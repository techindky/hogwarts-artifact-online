package edu.tcu.cs.hogwarts_artifact_online.Artifact;

public class ArtifactNotFoundException extends RuntimeException{
    public ArtifactNotFoundException(String id){
        super("Could not find artifact by id " + id + " :(");
    }
}
