package com.devlukas.hogwartsartifactsonline.wizard;

import com.devlukas.hogwartsartifactsonline.artifact.Artifact;
import com.devlukas.hogwartsartifactsonline.artifact.ArtifactRepository;
import com.devlukas.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
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

    public Wizard findById(Integer wizardId) {
        return this.wizardRepository.findById(wizardId).
                orElseThrow(() -> new ObjectNotFoundException(Wizard.class.getSimpleName() ,wizardId));
    }

    public List<Wizard> findAll() {
        return this.wizardRepository.findAll();
    }

    public Wizard save(Wizard newWizard) {
        return this.wizardRepository.save(newWizard);
    }

    public Wizard update(Integer wizardId, Wizard update) {
        var oldWizard = this.findById(wizardId);
        oldWizard.setName(update.getName());
        return this.wizardRepository.save(oldWizard);
    }

    public void delete(Integer wizardId) {
        var wizardToBeDeleted = findById(wizardId);
        wizardToBeDeleted.removeAllArtifacts();
        this.wizardRepository.deleteById(wizardId);
    }

    public void assignArtifact(Integer wizardId, String artifactId) {
        var artifactToBeAssigned = this.artifactRepository.findById(artifactId)
                .orElseThrow(() -> new ObjectNotFoundException(Artifact.class.getSimpleName(), artifactId));

        var wizard = findById(wizardId);

        if(artifactToBeAssigned.getOwner() != null) {
            artifactToBeAssigned.getOwner().removeArtifact(artifactToBeAssigned);
        }

        wizard.addArtifact(artifactToBeAssigned);
    }
}
