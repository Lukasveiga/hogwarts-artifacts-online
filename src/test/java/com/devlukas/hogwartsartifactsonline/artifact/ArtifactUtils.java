package com.devlukas.hogwartsartifactsonline.artifact;

import com.devlukas.hogwartsartifactsonline.artifact.dto.ArtifactDto;

public class ArtifactUtils {

    protected static Artifact generateArtifact(String id, String name, String description, String imageUrl) {
        var a = new Artifact();
        a.setId(id);
        a.setName(name);
        a.setDescription(description);
        a.setImageUrl(imageUrl);
        return a;
    }
}
