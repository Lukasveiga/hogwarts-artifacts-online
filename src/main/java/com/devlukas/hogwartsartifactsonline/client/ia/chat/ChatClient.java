package com.devlukas.hogwartsartifactsonline.client.ia.chat;

import com.devlukas.hogwartsartifactsonline.client.ia.chat.dto.ChatRequest;
import com.devlukas.hogwartsartifactsonline.client.ia.chat.dto.ChatResponse;

public interface ChatClient {

    ChatResponse generate(ChatRequest chatRequest);
}
