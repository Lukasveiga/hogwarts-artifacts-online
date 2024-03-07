package com.devlukas.hogwartsartifactsonline.artifact;

import com.devlukas.hogwartsartifactsonline.artifact.dto.ArtifactDto;
import com.devlukas.hogwartsartifactsonline.artifact.utils.IdWorker;
import com.devlukas.hogwartsartifactsonline.client.ia.chat.ChatClient;
import com.devlukas.hogwartsartifactsonline.client.ia.chat.dto.ChatRequest;
import com.devlukas.hogwartsartifactsonline.client.ia.chat.dto.ChatResponse;
import com.devlukas.hogwartsartifactsonline.client.ia.chat.dto.Message;
import com.devlukas.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.annotation.Timed;
import io.micrometer.observation.annotation.Observed;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ArtifactService {

    private final ArtifactRepository artifactRepository;

    private final ChatClient chatClient;

    private final IdWorker idWorker;

    public ArtifactService(ArtifactRepository repository, ChatClient chatClient, IdWorker idWorker) {
        this.artifactRepository = repository;
        this.chatClient = chatClient;
        this.idWorker = idWorker;
    }

    @Observed(name = "artifact", contextualName = "findByIdService")
    public Artifact findById(String artifactId) {
        return this.artifactRepository
                .findById(artifactId)
                .orElseThrow(() -> new ObjectNotFoundException(Artifact.class.getSimpleName() ,artifactId));
    }

    @Timed(value = "findAllArtifactsService.time")
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

    public String summarize(List<ArtifactDto> artifactDtos) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonArray = objectMapper.writeValueAsString(artifactDtos);

        var prompt = "Your task is to generate a short summary of a given JSON array";

        var messages = List.of(new Message("system", prompt), new Message("user", jsonArray));
        var chatRequest = new ChatRequest("gpt-3.5-turbo", messages);

        ChatResponse chatResponse = this.chatClient.generate(chatRequest);

        return chatResponse.choices().get(0).message().content();
    }
}
