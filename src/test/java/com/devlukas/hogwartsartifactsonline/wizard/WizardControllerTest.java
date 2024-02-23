package com.devlukas.hogwartsartifactsonline.wizard;

import com.devlukas.hogwartsartifactsonline.ControllerTestConfig;
import com.devlukas.hogwartsartifactsonline.artifact.Artifact;
import com.devlukas.hogwartsartifactsonline.system.StatusCode;
import com.devlukas.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import com.devlukas.hogwartsartifactsonline.wizard.dto.WizardDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static com.devlukas.hogwartsartifactsonline.wizard.WizardUtils.generateWizard;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


class WizardControllerTest extends ControllerTestConfig {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    WizardService wizardService;

    @Value("${api.endpoint.base-url}/wizards")
    String BASE_URL;

    List<Wizard> wizards = new ArrayList<>();

    @Autowired
    ObjectMapper objectMapper;

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
    void TestfindWizardByIdSuccess() throws Exception {
        // Given
        var wizard = WizardUtils.generateWizard(1, "Harry Potter");

        when(this.wizardService.findById(wizard.getId()))
                .thenReturn(wizard);

        // When - Then
        this.mockMvc.perform(get(BASE_URL + "/" + wizard.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find one success"))
                .andExpect(jsonPath("$.data.id").value(wizard.getId()))
                .andExpect(jsonPath("$.data.name").value(wizard.getName()))
                .andExpect(jsonPath("$.data.numberOfArtifacts").value(0));
    }

    @Test
    void testFindWizardByIdNotFound() throws Exception {
        // Given
        var wizardId = 1;
        when(this.wizardService.findById(wizardId))
                .thenThrow(new ObjectNotFoundException(Wizard.class.getSimpleName(), wizardId));

        // When - Then
        this.mockMvc.perform(
                        get(BASE_URL + "/" + wizardId).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not found Wizard with Id %d".formatted(wizardId)))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testFindAllWizards() throws Exception {
        // Given
        when(this.wizardService.findAll())
                .thenReturn(wizards);

        // When - Then
        this.mockMvc.perform(get(BASE_URL).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find all success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(wizards.size())))
                .andExpect(jsonPath("$.data[0].id").value(wizards.get(0).getId()))
                .andExpect(jsonPath("$.data[0].name").value(wizards.get(0).getName()))
                .andExpect(jsonPath("$.data[0].numberOfArtifacts").value(0));
    }

    @Test
    void testAddWizardSuccess() throws Exception {
        // Given
        var wizardDto = new WizardDto(null,
                "Harry Potter",
                null);

        var wizardDtoJson = objectMapper.writeValueAsString(wizardDto);

        var savedWizard = wizards.get(0);

        when(this.wizardService.save(any(Wizard.class)))
                .thenReturn(savedWizard);

        // When - Then
        this.mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON).content(wizardDtoJson).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add success"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.name").value(savedWizard.getName()))
                .andExpect(jsonPath("$.data.numberOfArtifacts").value(0));
    }

    @Test
    void testAddWizardFaildWithEmptyProvidedName() throws Exception {
        // Given
        var wizardDto = new WizardDto(null,
                "",
                null);

        var wizardDtoJson = objectMapper.writeValueAsString(wizardDto);

        var savedWizard = wizards.get(0);

        when(this.wizardService.save(any(Wizard.class)))
                .thenReturn(savedWizard);

        // When - Then
        this.mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON).content(wizardDtoJson).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.INVALID_ARGUMENT))
                .andExpect(jsonPath("$.message").value("Provided arguments are invalid, see data for details"))
                .andExpect(jsonPath("$.data.name").value("Name is required"));
    }

    @Test
    void testUpdateWizardSuccess() throws Exception {
        // Given
        var wizardDto = new WizardDto(1, "Harry Potter Update", null);

        var wizardDtoJson = objectMapper.writeValueAsString(wizardDto);

        var updatedWizard = generateWizard(1, wizardDto.name());

        when(this.wizardService.update(anyInt() , Mockito.any(Wizard.class)))
                .thenReturn(updatedWizard);

        // When - Then
        this.mockMvc.perform(put(BASE_URL + "/" + wizardDto.id())
                        .contentType(MediaType.APPLICATION_JSON).content(wizardDtoJson).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update success"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.name").value(updatedWizard.getName()))
                .andExpect(jsonPath("$.data.numberOfArtifacts").value(0));
    }

    @Test
    void testUpdateWizardNotFound() throws Exception {
        // Given
        var wizardDto = new WizardDto(1, "Harry Potter Update", null);

        var wizardDtoJson = objectMapper.writeValueAsString(wizardDto);

        var updatedWizard = generateWizard(1, wizardDto.name());

        when(this.wizardService.update(anyInt() , Mockito.any(Wizard.class)))
                .thenThrow(new ObjectNotFoundException(Wizard.class.getSimpleName() ,1));

        // When - Then
        this.mockMvc.perform(put(BASE_URL + "/" + wizardDto.id())
                        .contentType(MediaType.APPLICATION_JSON).content(wizardDtoJson).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not found Wizard with Id %d".formatted(1)))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testUpdateWizardFaildWithEmptyProvidedName() throws Exception {
        // Given
        var wizardDto = new WizardDto(1, "", null);

        var wizardDtoJson = objectMapper.writeValueAsString(wizardDto);

        var updatedWizard = generateWizard(1, wizardDto.name());

        when(this.wizardService.update(anyInt() , Mockito.any(Wizard.class)))
                .thenReturn(updatedWizard);

        // When - Then
        this.mockMvc.perform(put(BASE_URL + "/" + wizardDto.id())
                        .contentType(MediaType.APPLICATION_JSON).content(wizardDtoJson).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.INVALID_ARGUMENT))
                .andExpect(jsonPath("$.message").value("Provided arguments are invalid, see data for details"))
                .andExpect(jsonPath("$.data.name").value("Name is required"));
    }

    @Test
    void testDeleteAWizardSuccess() throws Exception {
        // Given
        var wizardId = 1;

        doNothing().when(this.wizardService)
                .delete(wizardId);

        // When - Then
        this.mockMvc.perform(delete(BASE_URL + "/" + wizardId)
                       .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete success"));
    }

    @Test
    void testDeleteWizardNotFound() throws Exception {
        // Given
        var wizardId = 1;

        doThrow(new ObjectNotFoundException(Wizard.class.getSimpleName() ,wizardId))
                .when(this.wizardService).delete(wizardId);

        // When - Then
        this.mockMvc.perform(delete(BASE_URL + "/" + wizardId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not found Wizard with Id %d".formatted(wizardId)))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testAssignArtifactSuccess() throws Exception {
        // Given
        var artifactId = "123456";
        var wizardId = 1;

        doNothing().when(this.wizardService)
                .assignArtifact(anyInt(), anyString());

        // When - Then
        this.mockMvc.perform(put(BASE_URL + "/" + wizardId + "/artifacts/" + artifactId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Artifact assignment success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testAssignArtifactErrorWithNonExistentWizardId() throws Exception {
        // Given
        var artifactId = "123456";
        var wizardId = 1;

        doThrow(new ObjectNotFoundException(Wizard.class.getSimpleName() ,wizardId))
                .when(this.wizardService).assignArtifact(anyInt(), anyString());

        // When - Then
        this.mockMvc.perform(put(BASE_URL + "/" + wizardId + "/artifacts/" + "12456")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not found Wizard with Id %d".formatted(wizardId)))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testAssignArtifactErrorWithNonExistentArtifactId() throws Exception {
        // Given
        var artifactId = "123456";
        var wizardId = 1;

        doThrow(new ObjectNotFoundException(Artifact.class.getSimpleName() ,artifactId))
                .when(this.wizardService).assignArtifact(anyInt(), anyString());

        // When - Then
        this.mockMvc.perform(put(BASE_URL + "/" + wizardId + "/artifacts/" + artifactId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not found Artifact with Id %s".formatted(artifactId)))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}