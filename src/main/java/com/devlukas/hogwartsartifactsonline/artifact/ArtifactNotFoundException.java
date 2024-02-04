package com.devlukas.hogwartsartifactsonline.artifact;

public class ArtifactNotFoundException extends RuntimeException {
    public ArtifactNotFoundException(String id) {
        super("Could not found artifact with Id %s".formatted(id));
    }
}
