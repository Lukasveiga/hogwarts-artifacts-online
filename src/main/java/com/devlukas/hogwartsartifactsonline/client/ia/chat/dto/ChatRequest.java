package com.devlukas.hogwartsartifactsonline.client.ia.chat.dto;

import java.util.List;

public record ChatRequest(String model, List<Message> messages) {
}
