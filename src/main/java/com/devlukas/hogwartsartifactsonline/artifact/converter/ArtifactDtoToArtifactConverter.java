package com.devlukas.hogwartsartifactsonline.artifact.converter;

import com.devlukas.hogwartsartifactsonline.artifact.Artifact;
import com.devlukas.hogwartsartifactsonline.artifact.dto.ArtifactDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ArtifactDtoToArtifactConverter implements Converter<ArtifactDto, Artifact> {
    @Override
    public Artifact convert(ArtifactDto source) {
        var a = new Artifact();
        a.setId(source.id());
        a.setName(source.name());
        a.setDescription(source.description());
        a.setImageUrl(source.imageUrl());
        return a;
    }
}
