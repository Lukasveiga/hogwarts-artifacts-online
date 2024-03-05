package com.devlukas.hogwartsartifactsonline.wizard;

import com.devlukas.hogwartsartifactsonline.ServiceTestConfig;
import com.devlukas.hogwartsartifactsonline.artifact.Artifact;
import com.devlukas.hogwartsartifactsonline.artifact.ArtifactRepository;
import com.devlukas.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.devlukas.hogwartsartifactsonline.wizard.WizardUtils.generateWizard;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;


class WizardServiceTest extends ServiceTestConfig {

    @InjectMocks
    private WizardService wizardService;


    @Mock
    private WizardRepository wizardRepository;

    @Mock
    private ArtifactRepository artifactRepository;

    private final List<Wizard> wizards = new ArrayList<>();

    @BeforeEach
    void setUp() {
        wizards.addAll(List.of(
                generateWizard(1, "Harry Potter"),
                generateWizard(2, "Albus Dumbledore"),
                generateWizard(3, "Hermione Granger")
        ));
    }

    @Test
    void testFindByIdSuccess() {
        // Given
        var wizard = generateWizard(1, "Harry Potter");

        when(this.wizardRepository.findById(wizard.getId()))
                .thenReturn(Optional.of(wizard));

        // When
        var existingWizard = this.wizardService.findById(wizard.getId());

        // Then
        assertThat(existingWizard.getId()).isEqualTo(wizard.getId());
        assertThat(existingWizard.getName()).isEqualTo(wizard.getName());
        verify(this.wizardRepository, times(1)).findById(wizard.getId());
    }

    @Test
    void testFindByIdNotFound() {
        // Given
        var wizardId = 1;

        when(this.wizardRepository.findById(anyInt()))
                .thenReturn(Optional.empty());

        // When - Then
        assertThatThrownBy(() -> this.wizardService.findById(wizardId))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not found Wizard with Id %d".formatted(wizardId));
        verify(this.wizardRepository, times(1)).findById(wizardId);
    }

    @Test
    void testFindAllSuccess() {
        // Given
        when(this.wizardRepository.findAll())
                .thenReturn(wizards);

        // When
        var wizardsList = this.wizardService.findAll();

        // Then
        assertThat(wizardsList.size()).isEqualTo(wizards.size());
        verify(this.wizardRepository, times(1)).findAll();
    }

    @Test
    void testSaveSuccess() {
        // Given
        var wizard = wizards.get(0);
        when(this.wizardRepository.save(wizard))
                .thenReturn(wizard);

        // When
        var savedWizard = this.wizardService.save(wizard);

        // Then
        assertThat(savedWizard.getId()).isEqualTo(wizard.getId());
        assertThat(savedWizard.getName()).isEqualTo(wizard.getName());
        verify(this.wizardRepository, times(1)).save(wizard);
    }

    @Test
    void testUpdateSuccess() {
        // Given
        var oldWizard = wizards.get(0);

        var updateWizard = generateWizard(1, "Harry Potter Update");

        when(this.wizardRepository.findById(anyInt()))
                .thenReturn(Optional.of(oldWizard));

        when(this.wizardRepository.save(oldWizard))
                .thenReturn(oldWizard);

        // When
        var updatedWizard = this.wizardService.update(1, updateWizard);

        // Then
        assertThat(updatedWizard.getId()).isEqualTo(oldWizard.getId());
        assertThat(updatedWizard.getName()).isEqualTo(oldWizard.getName());
        verify(this.wizardRepository, times(1)).findById(1);
        verify(this.wizardRepository, times(1)).save(oldWizard);
    }

    @Test
    void testUpdateNotFound() {
        // Given
        var wizardId = 1;

        var updateWizard = generateWizard(1, "Harry Potter Update");

        when(this.wizardRepository.findById(anyInt()))
                .thenReturn(Optional.empty());

        // When - Then
        assertThatThrownBy(() -> this.wizardService.update(wizardId, updateWizard))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not found Wizard with Id %d".formatted(wizardId));
        verify(this.wizardRepository, times(1)).findById(wizardId);
        verify(this.wizardRepository, times(0)).save(any(Wizard.class));
    }

    @Test
    void testDeleteSuccess() {
        // Given
        var wizard = wizards.get(0);

        when(this.wizardRepository.findById(wizard.getId()))
                .thenReturn(Optional.of(wizard));

        doNothing().when(this.wizardRepository)
                .deleteById(wizard.getId());

        // When
        this.wizardService.delete(wizard.getId());

        // Then
        verify(this.wizardRepository, times(1)).findById(wizard.getId());
        verify(this.wizardRepository, times(1)).deleteById(wizard.getId());
    }

    @Test
    void testDeleteNotFound() {
        //Given
        var wizardId = 1;
        when(this.wizardRepository.findById(wizardId))
                .thenReturn(Optional.empty());

        // When - Then
        assertThatThrownBy(() -> this.wizardService.delete(wizardId))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not found Wizard with Id %d".formatted(wizardId));
        verify(this.wizardRepository, times(1)).findById(wizardId);
        verify(this.wizardRepository, times(0)).save(any(Wizard.class));
    }

    @Test
    void testAssignArtifactSuccess() {
        // Given
        var artifact = new Artifact();
        artifact.setId("123456");
        artifact.setName("Invisibility Cloak");
        artifact.setDescription("Description...");
        artifact.setImageUrl("imageUrl");

        var w1 = generateWizard(1, "Harry Potter");
        w1.addArtifact(artifact);

        var w2 = generateWizard(2, "Nevile Longbottom");

        when(this.artifactRepository.findById(artifact.getId()))
                .thenReturn(Optional.of(artifact));

        when(this.wizardRepository.findById(w2.getId()))
                .thenReturn(Optional.of(w2));

        // When
        this.wizardService.assignArtifact(w2.getId(), artifact.getId());

        // Then
        assertThat(artifact.getOwner().getId()).isEqualTo(w2.getId());
        assertThat(w2.getArtifacts()).contains(artifact);
    }

    @Test
    void testAssignArtifactErrorWithNonExistentWizardId() {
        // Given
        var artifact = new Artifact();
        artifact.setId("123456");
        artifact.setName("Invisibility Cloak");
        artifact.setDescription("Description...");
        artifact.setImageUrl("imageUrl");

        var w1 = generateWizard(1, "Harry Potter");
        w1.addArtifact(artifact);

        var wizardId = 2;

        when(this.artifactRepository.findById(artifact.getId()))
                .thenReturn(Optional.of(artifact));

        when(this.wizardRepository.findById(anyInt()))
                .thenReturn(Optional.empty());

        // When - Then
        assertThatThrownBy(() -> this.wizardService.assignArtifact(wizardId, artifact.getId()))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not found Wizard with Id %d".formatted(wizardId));
        assertThat(artifact.getOwner().getId()).isEqualTo(w1.getId());
    }

    @Test
    void testAssignArtifactErrorWithNonExistentArtifactId() {
        // Given
        var artifactId = "123456";

        var w2 = generateWizard(2, "Nevile Longbottom");

        when(this.artifactRepository.findById(anyString()))
                .thenReturn(Optional.empty());

        // When - Then
        assertThatThrownBy(() -> this.wizardService.assignArtifact(w2.getId(), artifactId))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not found Artifact with Id %s".formatted(artifactId));
    }
}