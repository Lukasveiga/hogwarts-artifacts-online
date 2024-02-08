package com.devlukas.hogwartsartifactsonline.artifact;

import com.devlukas.hogwartsartifactsonline.artifact.utils.IdWorker;
import com.devlukas.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ArtifactService {

    private final ArtifactRepository artifactRepository;

    private final IdWorker idWorker;

    public ArtifactService(ArtifactRepository repository, IdWorker idWorker) {
        this.artifactRepository = repository;
        this.idWorker = idWorker;
    }

    public Artifact findById(String artifactId) {
        return this.artifactRepository
                .findById(artifactId)
                .orElseThrow(() -> new ObjectNotFoundException(Artifact.class.getSimpleName() ,artifactId));
    }

    public List<Artifact> findAll() {
        return this.artifactRepository.findAll();
    }

    public Artifact save(Artifact newArtifact) {
        newArtifact.setId(String.valueOf(idWorker.nextId()));
        return this.artifactRepository.save(newArtifact);
    }

    public Artifact update(String artifactId, Artifact update ) {
        var oldArtifact = findById(artifactId);
        oldArtifact.setName(update.getName());
        oldArtifact.setDescription(update.getDescription());
        oldArtifact.setImageUrl(update.getImageUrl());

        return this.artifactRepository.save(oldArtifact);
    }

    public void delete(String artifactId) {
        var artifact = findById(artifactId);
        this.artifactRepository.deleteById(artifactId);
    }
}
