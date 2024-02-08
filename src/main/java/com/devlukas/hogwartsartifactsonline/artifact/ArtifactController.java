package com.devlukas.hogwartsartifactsonline.artifact;

import com.devlukas.hogwartsartifactsonline.artifact.converter.ArtifactDtoToArtifactConverter;
import com.devlukas.hogwartsartifactsonline.artifact.converter.ArtifactToArtifactDtoConverter;
import com.devlukas.hogwartsartifactsonline.artifact.dto.ArtifactDto;
import com.devlukas.hogwartsartifactsonline.system.Result;
import com.devlukas.hogwartsartifactsonline.system.StatusCode;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("${api.endpoint.base-url}/artifacts")
public class ArtifactController {

    private final ArtifactService artifactService;

    private final ArtifactToArtifactDtoConverter artifactToArtifactDtoConverter;

    private final ArtifactDtoToArtifactConverter artifactDtoToArtifactConverter;


    public ArtifactController(ArtifactService service, ArtifactToArtifactDtoConverter artifactToArtifactDtoConverter, ArtifactDtoToArtifactConverter artifactDtoToArtifactConverter) {
        this.artifactService = service;
        this.artifactToArtifactDtoConverter = artifactToArtifactDtoConverter;
        this.artifactDtoToArtifactConverter = artifactDtoToArtifactConverter;
    }

    @GetMapping("/{artifactId}")
    public Result findArtifactById(@PathVariable String artifactId) {
        var foundArtifact = this.artifactService.findById(artifactId);
        return new Result(true,
                StatusCode.SUCCESS,
                "Find one success",
                artifactToArtifactDtoConverter.convert(foundArtifact));
    }

    @GetMapping
    public Result findAllArtifacts() {
        var foundArtifacts = this.artifactService.findAll();
        var artifactsDtos = foundArtifacts.stream().map(artifactToArtifactDtoConverter::convert).toList();
        return new Result(
                true,
                StatusCode.SUCCESS,
                "Find all success",
                artifactsDtos
        );
    }

    @PostMapping
    public Result addArtifact(@Valid @RequestBody  ArtifactDto artifactDto) {
        var newArtifact = this.artifactDtoToArtifactConverter.convert(artifactDto);
        Artifact savedArtifact = this.artifactService.save(Objects.requireNonNull(newArtifact));
        ArtifactDto savedArtifactDto = this.artifactToArtifactDtoConverter.convert(savedArtifact);
        return new Result(true,
                StatusCode.SUCCESS,
                "Add success",
                savedArtifactDto);
    }

    @PutMapping("/{artifactId}")
    public Result updateArtifact(@PathVariable String artifactId, @Valid @RequestBody ArtifactDto artifactDto) {
        var update = this.artifactDtoToArtifactConverter.convert(artifactDto);
        var updatedArtifact = this.artifactService.update(artifactId, update);
        var updatedArtifactDto = this.artifactToArtifactDtoConverter.convert(updatedArtifact);
        return new Result(true,
                StatusCode.SUCCESS,
                "Update success",
                updatedArtifactDto);
    }

    @DeleteMapping("/{artifactId}")
    public Result deleteArtifact(@PathVariable String artifactId) {
        this.artifactService.delete(artifactId);
        return new Result(true,
                StatusCode.SUCCESS,
                "Delete success");
    }
}
