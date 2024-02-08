package com.devlukas.hogwartsartifactsonline.wizard;

import com.devlukas.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.devlukas.hogwartsartifactsonline.wizard.WizardUtils.generateWizard;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WizardServiceTest {

    @InjectMocks
    private WizardService wizardService;


    @Mock
    private WizardRepository wizardRepository;

    private final List<Wizard> wizards = new ArrayList<>();

    @BeforeEach
    void setUp() {
        wizards.addAll(List.of(
                generateWizard(1, "Harry Potter"),
                generateWizard(2, "Albus Dumbledore"),
                generateWizard(3, "Hermione Granger")
        ));
    }

    @AfterEach
    void tearDown() {
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
}