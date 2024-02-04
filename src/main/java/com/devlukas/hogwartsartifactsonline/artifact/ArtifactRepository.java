package com.devlukas.hogwartsartifactsonline.artifact;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtifactRepository extends JpaRepository<Artifact, String> {
}
