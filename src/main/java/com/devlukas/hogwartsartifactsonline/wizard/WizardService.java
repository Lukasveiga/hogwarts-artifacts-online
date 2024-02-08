package com.devlukas.hogwartsartifactsonline.wizard;

import com.devlukas.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class WizardService {

    private final WizardRepository wizardRepository;

    public WizardService(WizardRepository wizardRepository) {
        this.wizardRepository = wizardRepository;
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
}
